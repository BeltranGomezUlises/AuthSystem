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
import com.machineAdmin.daos.cg.exceptions.ConstraintException;
import com.machineAdmin.daos.cg.exceptions.SQLPersistenceException;
import com.machineAdmin.entities.cg.admin.mongo.BinnacleAccess;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.managers.cg.commons.ManagerSQLFacade;
import com.machineAdmin.managers.cg.exceptions.ContraseñaIncorrectaException;
import com.machineAdmin.managers.cg.exceptions.UsuarioBlockeadoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.utils.UtilsBinnacle;
import com.machineAdmin.utils.UtilsConfig;
import com.machineAdmin.utils.UtilsDate;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsJson;
import com.machineAdmin.utils.UtilsSecurity;
import java.io.IOException;
import java.util.Date;

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
            return super.persist(entity); //To change body of generated methods, choose Tools | Templates. 
        } catch (ConstraintException ex) {           
            throw new ConstraintException(getMessageOfUniqueContraint(entity));
        } catch (SQLPersistenceException ex) {
            throw ex;
        }
    }

    @Override
    public void update(Usuario entity) throws ConstraintException, SQLPersistenceException {
        try {
            entity.setContra(UtilsSecurity.cifrarMD5(entity.getContra()));
            super.update(entity); 
        } catch (ConstraintException ex) {           
            throw new ConstraintException(getMessageOfUniqueContraint(entity));
        } catch (SQLPersistenceException ex) {
            throw ex;
        }
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

        if (loged != null) {

            if (loged.getBloqueadoHastaFecha() != null) {
                if (loged.getBloqueado() && loged.getBloqueadoHastaFecha().after(new Date())) {
                    throw new UsuarioBlockeadoException("Usuario bloqueado hasta " + UtilsDate.format_D_MM_YYYY_HH_MM(loged.getBloqueadoHastaFecha()));
                }
            }
            if (loged.getInhabilitado() != null) {
                if (loged.getInhabilitado()) {
                    throw new ContraseñaIncorrectaException("No se encontro un usuario con esa contraseña");
                }
            }
            loged.setNumeroIntentosLogin(0);
            this.update(loged);

            //login exitoso, generar bitácora                                     
            new Thread(() -> {
                BinnacleAccess access = new BinnacleAccess(loged.getId().toString());
                UtilsBinnacle.bitacorizar("cg.bitacora.accesos", access);
            }).start();

            return loged;
        } else { //ver si el usuario existe y verificar número de intentos 
            this.numberAttemptVerification(usuarioAutenticando);
            throw new ContraseñaIncorrectaException("No se encontro un usuario con esa contraseña");
        }

    }

    public void logout(String token) throws IOException {
        Usuario u = UtilsJson.jsonDeserialize(UtilsJWT.getBodyToken(token), Usuario.class);
        BinnacleAccess exit = new BinnacleAccess(u.getId().toString());
        UtilsBinnacle.bitacorizar("cg.bitacora.salidas", exit);
    }

    private void numberAttemptVerification(Usuario usuario) throws UsuarioInexistenteException, UsuarioBlockeadoException, Exception {

        Usuario intentoLogin = this.stream().filter(u
                -> u.getCorreo().equals(usuario.getCorreo())
                || u.getNombre().equals(usuario.getNombre())
                || u.getTelefono().equals(usuario.getNombre())).findFirst().get();

        if (intentoLogin != null) { //si es un usuario existente                                                                         
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
                    this.update(usuario);
                    throw new UsuarioBlockeadoException("El usuario fue blockeado por el número de intentos fallidos hasta " + intentoLogin.getBloqueadoHastaFecha());
                } else {
                    this.update(usuario);
                }
            } catch (UsuarioBlockeadoException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } else {
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

    private enum userIdentifierType {
        PHONE, MAIL, USER
    }
}
