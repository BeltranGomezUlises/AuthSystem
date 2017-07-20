/*
 * Copyright (C) 2017 Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.machineAdmin.managers.cg.admin.postgres;

import com.machineAdmin.daos.cg.admin.postgres.DaoUsuario;
import com.machineAdmin.daos.cg.admin.postgres.jpaControllers.BitacoraContrasJpaController;
import com.machineAdmin.daos.cg.admin.postgres.jpaControllers.UsuarioJpaController;
import com.machineAdmin.daos.cg.exceptions.ConstraintException;
import com.machineAdmin.daos.cg.exceptions.SQLPersistenceException;
import com.machineAdmin.entities.cg.admin.mongo.BinnacleAccess;
import com.machineAdmin.entities.cg.admin.postgres.BitacoraContras;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.managers.cg.commons.ManagerSQLFacade;
import com.machineAdmin.managers.cg.exceptions.ContraseñaIncorrectaException;
import com.machineAdmin.managers.cg.exceptions.ParametroInvalidoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioBlockeadoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.models.cg.ModelRecoverCodeUser;
import com.machineAdmin.utils.UtilsBinnacle;
import com.machineAdmin.utils.UtilsConfig;
import com.machineAdmin.utils.UtilsDB;
import com.machineAdmin.utils.UtilsDate;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsMail;
import com.machineAdmin.utils.UtilsSMS;
import com.machineAdmin.utils.UtilsSecurity;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;
import static java.util.stream.Collectors.toList;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerUsuario extends ManagerSQLFacade<Usuario> {

    public ManagerUsuario() {
        super(new DaoUsuario());
    }

    @Override
    public Usuario persist(Usuario entity) throws SQLPersistenceException, ConstraintException {
        try {
            entity.setContra(UtilsSecurity.cifrarMD5(entity.getContra()));
            Usuario persisted = super.persist(entity);

            //bitacorizar la contraseña
            BitacoraContras bc = new BitacoraContras(persisted.getId(), persisted.getContra());
            bc.setUsuario1(persisted);

            ManagerBitacoraContra managerBitacoraContra = new ManagerBitacoraContra();
            managerBitacoraContra.persist(bc);

            return persisted; //To change body of generated methods, choose Tools | Templates. 
        } catch (ConstraintException ex) {
            throw new ConstraintException(getMessageOfUniqueContraint(entity));
        } catch (SQLPersistenceException ex) {
            throw ex;
        }
    }

    @Override
    public void update(Usuario entity) throws ConstraintException, SQLPersistenceException {
        try {
            super.update(entity);
        } catch (ConstraintException ex) {
            throw new ConstraintException(getMessageOfUniqueContraint(entity));
        } catch (SQLPersistenceException ex) {
            throw ex;
        }
    }

    @Override
    public void delete(Object id) throws Exception {
        Usuario usuario = this.findOne(id);
        usuario.setInhabilitado(Boolean.TRUE);
        this.update(usuario);
    }

    @Override
    public Usuario findOne(Object id) {
        return super.findOne(UUID.fromString(id.toString()));
    }

    /**
     * Metodo de login para autentificar usuarios
     *
     * @param usuarioAutenticando -> el usuario puede contener en su atributo
     * user el nombre de usuario, el correo o el telefono como identificador
     * @return loged, usuario logeado
     * @throws UsuarioInexistenteException
     * @throws
     * com.machineAdmin.managers.cg.exceptions.ContraseñaIncorrectaException
     * @throws com.machineAdmin.managers.cg.exceptions.UsuarioBlockeadoException
     */
    public Usuario login(Usuario usuarioAutenticando) throws UsuarioInexistenteException, ContraseñaIncorrectaException, UsuarioBlockeadoException, Exception {
        try {
            Usuario loged = this.stream().filter(u -> {
                switch (getUserIdentifierType(usuarioAutenticando.getNombre())) {
                    case MAIL:
                        return u.getCorreo().equals(usuarioAutenticando.getNombre()) && u.getContra().equals(usuarioAutenticando.getContra());
                    case PHONE:
                        return u.getTelefono().equals(usuarioAutenticando.getNombre()) && u.getContra().equals(usuarioAutenticando.getContra());
                    default:
                        return u.getNombre().equals(usuarioAutenticando.getNombre()) && u.getContra().equals(usuarioAutenticando.getContra());
                }
            }).findFirst().get();
            if (loged.getBloqueado() && loged.getBloqueadoHastaFecha().after(new Date())) {
                throw new UsuarioBlockeadoException("Usuario bloqueado hasta " + UtilsDate.format_D_MM_YYYY_HH_MM(loged.getBloqueadoHastaFecha()));
            }
            if (loged.getInhabilitado()) {
                throw new ContraseñaIncorrectaException("No se encontro un usuario con esa contraseña");
            }

            loged.setNumeroIntentosLogin(0);
            this.update(loged);

            //login exitoso, generar bitácora                                     
            new Thread(() -> {
                BinnacleAccess access = new BinnacleAccess(loged.getId().toString());
                UtilsBinnacle.bitacorizar("cg.bitacora.accesos", access);
            }).start();

            return loged;

        } catch (NoSuchElementException e) {
            //verificar si existe el usuario
            this.numberAttemptVerification(usuarioAutenticando);
            throw new ContraseñaIncorrectaException("No se encontro un usuario con esa contraseña");
        }
    }

    public void logout(String token) throws IOException {
        BinnacleAccess exit = new BinnacleAccess(UtilsJWT.getBodyToken(token));
        UtilsBinnacle.bitacorizar("cg.bitacora.salidas", exit);
    }

    private void numberAttemptVerification(Usuario usuario) throws UsuarioInexistenteException, UsuarioBlockeadoException, Exception {
        try {
            Usuario intentoLogin = this.stream().filter(u
                    -> u.getCorreo().equals(usuario.getCorreo())
                    || u.getNombre().equals(usuario.getNombre())
                    || u.getTelefono().equals(usuario.getNombre())).findFirst().get();

            if (intentoLogin.getBloqueado()) {
                throw new UsuarioBlockeadoException("El usuario fue blockeado por el número de intentos fallidos hasta " +  UtilsDate.format_D_MM_YYYY_HH_MM(intentoLogin.getBloqueadoHastaFecha()));
            }
            //<editor-fold defaultstate="collapsed" desc="CRITERIOS DE VERIFICACION DE INTENTOS DE LOGIN"> 
//             aumentar numero de intentos para bloqueo temporal si el lapso de tiempo es mayor al configurado 
//             si el numero de intentos realizados es nulo, inicializar y actualizar 
//             si realiza un intento en un ranto menor a los segundos permitidos entre intentos aumentar intentos 
//             si el numero de intentos es mayor al permitido dejar el usuario bloqueado por un timepo configurado 
//             formula = (now() - lastUserAttemptLoginDate) > timeBetweenAttempts             
            //</editor-fold>                
            if (intentoLogin.getNumeroIntentosLogin() == 0) {
                intentoLogin.setNumeroIntentosLogin(1);
                intentoLogin.setFechaUltimoIntentoLogin(new Date());
            } else {
                long intervaloDeIntento = (new Date().getTime() - intentoLogin.getFechaUltimoIntentoLogin().getTime());
                if (intervaloDeIntento < UtilsConfig.getSecondsBetweenLoginAttempt() * 1000) { //es un intento fuera del rango permitido de tiempo 
                    intentoLogin.aumentarNumeroDeIntentosLogin();
                } else { //es un intento dentro del rango permitido de tiempo 
                    intentoLogin.setNumeroIntentosLogin(1);
                    intentoLogin.setFechaUltimoIntentoLogin(new Date());
                }
            }
            /**
             * si el numero de intentos excede el permitido, bloquear usuario
             */
            try {
                if (intentoLogin.getNumeroIntentosLogin() > UtilsConfig.getMaxNumberLoginAttempt()) {
                    intentoLogin.setBloqueado(true);
                    intentoLogin.setBloqueadoHastaFecha(UtilsConfig.getDateUtilUserStillBlocked());
                    this.update(intentoLogin);
                    throw new UsuarioBlockeadoException("El usuario fue blockeado por el número de intentos fallidos hasta " +  UtilsDate.format_D_MM_YYYY_HH_MM(intentoLogin.getBloqueadoHastaFecha()));
                } else {
                    this.update(intentoLogin);
                }
            } catch (UsuarioBlockeadoException e) {
                throw e;
            } catch (Exception e) {                
                throw e;
            }
        } catch (NoSuchElementException e) {
            throw new UsuarioInexistenteException("La contraseña es incorrecta");
        }
    }

    private String getMessageOfUniqueContraint(Usuario entity) {
        //buscar que atributo ya ocupado  
        String mensaje = "ya existen un usuario con el atributo";
        if (this.stream().anyMatch(u -> u.getNombre().equals(entity.getNombre()))) {
            mensaje += " nombre,";
        }
        if (this.stream().anyMatch(u -> u.getCorreo().equals(entity.getCorreo()))) {
            mensaje += " correo,";
        }
        if (this.stream().anyMatch(u -> u.getTelefono().equals(entity.getTelefono()))) {
            mensaje += " telefono,";
        }
        mensaje = mensaje.substring(0, mensaje.length() - 1);
        return mensaje;
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

    public ModelRecoverCodeUser enviarCodigo(String identifier) throws UsuarioInexistenteException,
            ParametroInvalidoException, EmailException, MalformedURLException {

        Usuario usuarioARecuperar = null;

        try {
            switch (getUserIdentifierType(identifier)) {
                case MAIL:
                    usuarioARecuperar = this.stream().filter(u -> u.getCorreo().equals(identifier)).findFirst().get();
                    break;
                case PHONE:
                    usuarioARecuperar = this.stream().filter(u -> u.getTelefono().equals(identifier)).findFirst().get();
                    break;
                default:
                    throw new ParametroInvalidoException("el identificador proporsionado no es váliodo. Debe de utilizar un correo electronico ó número de teléfono de 10 dígitos");
            }

            Random r = new Random();
            //generar codigo de 8 digitos aleatorios
            String code = String.valueOf(r.nextInt(99));
            code += String.valueOf(r.nextInt(99));
            code += String.valueOf(r.nextInt(99));
            code += String.valueOf(r.nextInt(99));
            //enviar correo con codigo de recuperacion
            switch (getUserIdentifierType(identifier)) {
                case MAIL:
                    UtilsMail.sendRecuperarContraseñaHTMLMail(identifier, usuarioARecuperar.getNombre(), code);
                    break;
                case PHONE:
                    UtilsSMS.sendSMS(identifier, "Hola " + usuarioARecuperar.getNombre() + " su código de recuperacion de contraseña es: " + code);
                    break;
            }
            ModelRecoverCodeUser model = new ModelRecoverCodeUser();
            model.setCode(code);
            model.setIdUser(usuarioARecuperar.getId().toString());

            return model;

        } catch (NoSuchElementException e) {
            throw new UsuarioInexistenteException("No se encontro usuario con el identificador proporsionado");
        }
    }

    public void resetPassword(String userId, String pass) throws Exception {
        pass = UtilsSecurity.cifrarMD5(pass);

        ManagerBitacoraContra managerBitacoraContra = new ManagerBitacoraContra();
        BitacoraContras bitacoraContra = new BitacoraContras(UUID.fromString(userId), pass);

        if (managerBitacoraContra.stream().anyMatch(e -> e.equals(bitacoraContra))) {
            throw new ParametroInvalidoException("La contraseña que esta ingresando ya fué utilizada, intente con otra");
        }

        ManagerUsuario managerUsuario = new ManagerUsuario();
        Usuario u = this.findOne(UUID.fromString(userId));
        u.setContra(pass);
        managerUsuario.update(u);

        List<BitacoraContras> bitacoraContras = managerBitacoraContra.stream()
                .filter(b -> b.getBitacoraContrasPK().getUsuario().equals(u.getId()))
                .sorted((b1, b2) -> b1.getFechaAsiganada().compareTo(b2.getFechaAsiganada()))
                .collect(toList());

        //obtener el numero maximo de contraseñas a guardar para impedir repeticion
        int maxNumber = UtilsConfig.getMaxPasswordRecords();
        // lastPassword.size() < maxNumber -> agregar pass actual al registro
        // lastPassword.size() >= maxNumber -> resize de lastPassword con los ultimos maxNumber contraseñas                        

        bitacoraContra.setUsuario1(u);

        if (bitacoraContras.size() < maxNumber) {
            managerBitacoraContra.persist(bitacoraContra); //añadir la bitacora de la contra usada            
        } else {
            managerBitacoraContra.delete(bitacoraContras.get(0).getBitacoraContrasPK()); //remover la ultima contra asignada
            managerBitacoraContra.persist(bitacoraContra);//agregar nueva
        }
    }

    private enum userIdentifierType {
        PHONE, MAIL, USER
    }
}
