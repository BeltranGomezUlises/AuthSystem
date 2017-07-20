/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.admin.mongo;

import com.machineAdmin.managers.cg.commons.ManagerMongoFacade;
import com.machineAdmin.daos.cg.admin.mongo.DaoUser;
import com.machineAdmin.entities.cg.admin.mongo.BinnacleAccess;
import com.machineAdmin.entities.cg.admin.mongo.Profile;
import com.machineAdmin.entities.cg.admin.mongo.User;
import com.machineAdmin.entities.cg.admin.mongo.User.LoginAttempt;
import com.machineAdmin.managers.cg.exceptions.ContraseñaIncorrectaException;
import com.machineAdmin.managers.cg.exceptions.ParametroInvalidoException;
import com.machineAdmin.managers.cg.exceptions.UserException;
import com.machineAdmin.managers.cg.exceptions.UsuarioBlockeadoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.models.cg.ModelAsignedUser;
import com.machineAdmin.models.cg.ModelRecoverCodeUser;
import com.machineAdmin.models.cg.ModelSetPermission;
import com.machineAdmin.models.cg.ModelSetProfilesToUsuario;
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
import static java.util.stream.Collectors.toList;
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

    @Override
    public List<User> findAll() {
        return this.findAll("user", "mail", "phone", "_id", "inhabilitado", "permission");
    }

    @Override
    public User persist(User entity) throws UserException, Exception {
        this.verificarUnicidadCreate(entity);
        entity.setPass(UtilsSecurity.cifrarMD5(entity.getPass()));
        return super.persist(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(User entity) throws UserException, Exception {
        this.verificarUnicidadUpdate(entity);
        super.update(entity);
    }

    /**
     *
     * @param userId -> id de usuario a habilitar/inhabilitar
     * @return -> true si fue habilitado, false si fue inhabilitado
     * @throws Exception
     */
    public boolean inhabilitar(String userId) throws Exception {
        User u = this.findOne(userId);
        if (u == null) {
            throw new NullPointerException("Usuario inexistente");
        }
        u.setInhabilitado(!u.isInhabilitado());
        this.update(u);
        return u.isInhabilitado();
    }
    
    public void setPermissionToUser(ModelSetPermission modelSetPermissionUser) throws Exception {
        User u = this.findOne(modelSetPermissionUser.getId());
        u.setAsignedPermissions(modelSetPermissionUser.getPermissionsAsigned());
        this.update(u);
    }

    public void setProfiles(ModelSetProfilesToUsuario modelSetProfilesToUsuario) {
        ManagerProfile managerProfile = new ManagerProfile();

        List<Profile> perfilesAsignarUsuario = managerProfile.findAll(
                DBQuery.in("_id", modelSetProfilesToUsuario.getPerfiles().stream()
                        .map(e -> e.getPerfilId())
                        .collect(toList())
                )
        );

        perfilesAsignarUsuario.stream().forEach(p -> {
            //buscar el perfil y ver si hereda o no
            ModelAsignedUser modelAsignedUser = new ModelAsignedUser();

            modelAsignedUser.setHeritage(modelSetProfilesToUsuario.getPerfiles().stream()
                    .filter(m -> m.getPerfilId().equals(p.getId()))
                    .findFirst()
                    .get()
                    .isHereda()
            );

            modelAsignedUser.setUserId(modelSetProfilesToUsuario.getUserId());

            //si p ya tiene el usuario, actualizar la herencia
            if (p.getUsers().stream().anyMatch(u -> u.getUserId().equals(modelSetProfilesToUsuario.getUserId()))) {
                p.getUsers().stream().filter(u -> u.getUserId().equals(modelSetProfilesToUsuario.getUserId())).findFirst().get().setHeritage(modelAsignedUser.isHeritage());
            } else {
                p.getUsers().add(modelAsignedUser);
            }

            try {
                managerProfile.update(p);
            } catch (Exception ex) {
                Logger.getLogger(ManagerUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }
    
    private void verificarUnicidadCreate(User entity) throws UserException {
        if (entity.getMail() == null && entity.getPhone() == null) {
            throw new UserException("Se necesita tener un correo o un numero de telefono para poder registrar el usuario");
        }
        if (this.findOne(DBQuery.is("user", entity.getUser())) != null) {
            throw new UserException.UsuarioYaExistente("Nombre de Usuario ya ha sido utilizado");
        }
        if (entity.getMail() != null && this.findOne(DBQuery.is("mail", entity.getMail())) != null) {
            throw new UserException.CorreoYaExistente("Correo de usuario ya ha sido utilizado");
        }
        if (entity.getPhone() != null && this.findOne(DBQuery.is("phone", entity.getPhone())) != null) {
            throw new UserException.CorreoYaExistente("Telefono de usuario ya ha sido utilizado");
        }

    }

    private void verificarUnicidadUpdate(User entity) throws UserException {
        if (this.findOne(DBQuery.is("user", entity.getUser()).notEquals("_id", entity.getId())) != null) {
            throw new UserException.UsuarioYaExistente("Nombre de Usuario ya ha sido utilizado");
        }
        if (entity.getMail() != null && this.findOne(DBQuery.is("mail", entity.getMail()).notEquals("_id", entity.getId())) != null) {
            throw new UserException.CorreoYaExistente("Correo de usuario ya ha sido utilizado");
        }
        if (entity.getPhone() != null && this.findOne(DBQuery.is("phone", entity.getPhone()).notEquals("_id", entity.getId())) != null) {
            throw new UserException.CorreoYaExistente("Telefono de usuario ya ha sido utilizado");
        }
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
