/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.admin;

import com.machineAdmin.managers.cg.ManagerMongoFacade;
import com.machineAdmin.daos.cg.admin.DaoUser;
import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.managers.cg.exceptions.ContraseñaIncorrectaException;
import com.machineAdmin.managers.cg.exceptions.ParametroInvalidoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.models.cg.ModelRecoverCodeUser;
import com.machineAdmin.utils.UtilsMail;
import com.machineAdmin.utils.UtilsSMS;
import com.machineAdmin.utils.UtilsSecurity;
import java.net.MalformedURLException;
import java.util.Random;
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
     * @param usuario -> el usuario puede contener en su atributo user el nombre de usuario, el correo o el telefono como identificador
     * @return loged, usuario logeado
     * @throws UsuarioInexistenteException 
     */
    public User Login(User usuario) throws UsuarioInexistenteException, ContraseñaIncorrectaException {
        Query q = DBQuery.is("pass", usuario.getPass());
        switch (getUserIdentifierType(usuario.getUser())) {
            case MAIL:
                q.is("mail", usuario.getUser());
                break;
            case PHONE:
                q.is("phone", usuario.getUser());
                break;
            default:
                q.is("user", usuario.getUser());
        }        
        User loged = this.findOne(q);
        if (loged != null) {
            return loged;
        } else { //ver si el usuario existe y verificar número de intentos
            Query queryVerificacion = DBQuery.or(
                        DBQuery.is("user", usuario.getUser()),
                        DBQuery.is("mail", usuario.getUser()),
                        DBQuery.is("phone", usuario.getUser())
                    );
            User attemptedLogin = this.findOne(q);
            if (attemptedLogin != null) {
                //aumentar numero de intentos para bloqueo temporal
                
                throw new ContraseñaIncorrectaException("La contraseña es incorrecta");
            }            
        }
        throw new UsuarioInexistenteException("No se encontro un usuario con esa contraseña");
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
