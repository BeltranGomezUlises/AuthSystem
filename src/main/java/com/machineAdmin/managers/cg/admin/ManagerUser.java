/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.admin;

import com.machineAdmin.managers.cg.ManagerMongoFacade;
import com.machineAdmin.daos.cg.admin.DaoUser;
import com.machineAdmin.entities.cg.admin.BinnacleAccess;
import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.entities.cg.admin.User.LoginAttempt;
import com.machineAdmin.managers.cg.exceptions.ContraseñaIncorrectaException;
import com.machineAdmin.managers.cg.exceptions.ParametroInvalidoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioBlockeadoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.models.cg.ModelRecoverCodeUser;
import com.machineAdmin.utils.UtilsBinnacle;
import com.machineAdmin.utils.UtilsConfig;
import com.machineAdmin.utils.UtilsDate;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsJson;
import com.machineAdmin.utils.UtilsMail;
import com.machineAdmin.utils.UtilsSMS;
import com.machineAdmin.utils.UtilsSecurity;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.EmailException;
import org.mongojack.DBQuery;
import org.mongojack.DBQuery.Query;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerUser extends ManagerMongoFacade<User> {

    public ManagerUser() {
        super(new DaoUser());
    }

    /**
     * Metodo de login para autentificar usuarios
     *
     * @param user -> el usuario puede contener en su atributo user el nombre de
     * usuario, el correo o el telefono como identificador
     * @return loged, usuario logeado
     * @throws UsuarioInexistenteException
     * @throws
     * com.machineAdmin.managers.cg.exceptions.ContraseñaIncorrectaException
     * @throws com.machineAdmin.managers.cg.exceptions.UsuarioBlockeadoException
     */
    public User login(User user) throws UsuarioInexistenteException, ContraseñaIncorrectaException, UsuarioBlockeadoException, Exception {
        Query q = DBQuery.is("pass", user.getPass());
        switch (getUserIdentifierType(user.getUser())) {
            case MAIL:
                q.is("mail", user.getUser());
                break;
            case PHONE:
                q.is("phone", user.getUser());
                break;
            default:
                q.is("user", user.getUser());
        }
        User loged = this.findOne(q);
        if (loged != null) {
            if (loged.getBlocked() != null) {
                if (loged.getBlocked().isBlocked() && loged.getBlocked().getBlockedUntilDate().after(new Date())) {
                    throw new UsuarioBlockeadoException("Usuario bloqueado hasta " + UtilsDate.format_D_MM_YYYY_HH_MM(loged.getBlocked().getBlockedUntilDate()));
                }
            }
            loged.setLoginAttempt(null);
            this.update(loged);

            //login exitoso, generar bitácora                                    
            new Thread(() -> {
                BinnacleAccess access = new BinnacleAccess(loged.getId());
                UtilsBinnacle.bitacorizar("cg.bitacora.accesos", access);
            }).start();

            return loged;
        } else { //ver si el usuario existe y verificar número de intentos
            this.numberAttemptVerification(user);
            throw new ContraseñaIncorrectaException("No se encontro un usuario con esa contraseña");
        }

    }

    public void logout(String token) throws IOException {
        User u = UtilsJson.jsonDeserialize(UtilsJWT.getBodyToken(token), User.class);
        BinnacleAccess exit = new BinnacleAccess(u.getId());
        UtilsBinnacle.bitacorizar("cg.bitacora.salidas", exit);
    }

    private void numberAttemptVerification(User usuario) throws UsuarioInexistenteException, UsuarioBlockeadoException {
        Query queryVerificacion = DBQuery.or(
                DBQuery.is("user", usuario.getUser()),
                DBQuery.is("mail", usuario.getUser()),
                DBQuery.is("phone", usuario.getUser())
        );
        User userAttemptedLogin = this.findOne(queryVerificacion);
        if (userAttemptedLogin != null) { //si es un usuario existente                                                                        
            //<editor-fold defaultstate="collapsed" desc="CRITERIOS DE VERIFICACION DE INTENTOS DE LOGIN">
//             aumentar numero de intentos para bloqueo temporal si el lapso de tiempo es mayor al configurado
//             si el numero de intentos realizados es nulo, inicializar y actualizar
//             si realiza un intento en un ranto menor a los segundos permitidos entre intentos aumentar intentos
//             si el numero de intentos es mayor al permitido dejar el usuario bloqueado por un timepo configurado
//             formula = (now() - lastUserAttemptLoginDate) > timeBetweenAttempts            
            //</editor-fold>   
            LoginAttempt loginAttempt = userAttemptedLogin.getLoginAttempt();
            if (loginAttempt == null) {
                userAttemptedLogin.setLoginAttempt(new User.LoginAttempt(new Date(), 1));
            } else {
                long intervaloDeIntento = (new Date().getTime() - loginAttempt.getLastLoginAttemptDate().getTime());
                if (intervaloDeIntento < UtilsConfig.getSecondsBetweenLoginAttempt() * 1000) { //es un intento fuera del rango permitido de tiempo
                    userAttemptedLogin.riseLoginAttemps();
                } else { //es un intento dentro del rango permitido de tiempo
                    userAttemptedLogin.getLoginAttempt().setNumberLoginAttempts(1);
                    userAttemptedLogin.getLoginAttempt().setLastLoginAttemptDate(new Date());
                }
            }
            /**
             * si el numero de intentos excede el permitido, bloquear usuario
             */
            if (userAttemptedLogin.getLoginAttempt().getNumberLoginAttempts() > UtilsConfig.getMaxNumberLoginAttempt()) {
                userAttemptedLogin.setBlocked(new User.BlockedUser(true, UtilsConfig.getDateUtilUserStillBlocked()));
                try {
                    this.update(usuario);
                } catch (Exception ex) {
                    Logger.getLogger(ManagerUser.class.getName()).log(Level.SEVERE, null, ex);
                }
                throw new UsuarioBlockeadoException("El usuario fue blockeado por el número de intentos fallidos hasta " + userAttemptedLogin.getBlocked().getBlockedUntilDate());
            }
            try {
                this.update(userAttemptedLogin);
            } catch (Exception ex) {
                Logger.getLogger(ManagerUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new UsuarioInexistenteException("La contraseña es incorrecta");
        }
    }

    public ModelRecoverCodeUser enviarCodigo(String identifier) throws UsuarioInexistenteException,
            ParametroInvalidoException, EmailException, MalformedURLException {
        //obtener el usuario registrado con ese numero de telefono
        ManagerUser managerUser = new ManagerUser();
        String tipoEnvio;
        Query q;
        switch (getUserIdentifierType(identifier)) {
            case MAIL:
                q = DBQuery.is("mail", identifier);
                break;
            case PHONE:
                q = DBQuery.is("phone", identifier);
                break;
            default:
                throw new ParametroInvalidoException("el identificador proporsionado no es váliodo. Debe de utilizar un correo electronico ó número de teléfono de 10 dígitos");
        }
        User u = managerUser.findOne(q);
        if (u != null) {
            Random r = new Random();
            //generar codigo de 8 digitos aleatorios
            String code = String.valueOf(r.nextInt(99));
            code += String.valueOf(r.nextInt(99));
            code += String.valueOf(r.nextInt(99));
            code += String.valueOf(r.nextInt(99));
            //enviar correo con codigo de recuperacion
            switch (getUserIdentifierType(identifier)) {
                case MAIL:
                    UtilsMail.sendRecuperarContraseñaHTMLMail(identifier, u.getUser(), code);
                    break;
                case PHONE:
                    UtilsSMS.sendSMS(identifier, "Hola " + u.getUser() + " su código de recuperacion de contraseña es: " + code);
                    break;
            }
            ModelRecoverCodeUser model = new ModelRecoverCodeUser();
            model.setCode(code);
            model.setIdUser(u.getId());

            return model;
        } else {
            throw new UsuarioInexistenteException("No se encontro usuario con el identificador proporsionado");
        }
    }

    public void resetPassword(String userId, String pass) throws Exception {
        User u = this.findOne(userId);
        List<String> lastPassword = u.getLastPasswords();
        //obtener el numero maximo de contraseñas a guardar para impedir repeticion
        int maxNumber = UtilsConfig.getMaxPasswordRecords();

        // lastPassword.size() < maxNumber -> agregar pass actual al registro
        // lastPassword.size() >= maxNumber -> resize de lastPassword con los ultimos maxNumber contraseñas
        if (lastPassword.size() < maxNumber) {
            lastPassword.add(u.getPass()); //añadimos le passActual            
        } else {
            String[] newLastPasswords = new String[maxNumber];
            for (int i = 1; i < maxNumber; i++) {
                newLastPasswords[i - 1] = lastPassword.get(i);
            }
            newLastPasswords[maxNumber - 1] = u.getPass(); //añadir la final            
            u.setLastPasswords(Arrays.asList(newLastPasswords));
        }

        u.setPass(UtilsSecurity.cifrarMD5(pass));
        this.update(u);
    }

    private userIdentifierType getUserIdentifierType(String userIdentifier) {
        if (userIdentifier.contains("@")) { //es un correo
            return userIdentifierType.MAIL;
        } else {
            if (userIdentifier.matches("^[0-9]{10}$")) {
                return userIdentifierType.PHONE;
            } else {
                return userIdentifierType.USER;
            }
        }
    }

    private enum userIdentifierType {
        PHONE, MAIL, USER
    }

}
