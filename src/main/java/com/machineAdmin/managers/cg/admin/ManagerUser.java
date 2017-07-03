/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.machineAdmin.managers.cg.ManagerMongoFacade;
import com.machineAdmin.daos.cg.admin.DaoUser;
import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.managers.cg.exceptions.ParametroInvalidoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.models.cg.ModelRecoverCodeUser;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsJson;
import com.machineAdmin.utils.UtilsMail;
import com.machineAdmin.utils.UtilsSMS;
import com.machineAdmin.utils.UtilsSecurity;
import java.io.IOException;
import java.net.MalformedURLException;
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

    public User Login(User usuario) throws UsuarioInexistenteException {
        Query q = DBQuery
                .is("user", usuario.getUser())
                .is("pass", usuario.getPass());
        User loged = this.findOne(q);
        if (loged != null) {
            return loged;
        } else {
            throw new UsuarioInexistenteException("No se encontro un usuario con esa contraseña");
        }
    }   

    public ModelRecoverCodeUser enviarCodigo(String identifier) throws UsuarioInexistenteException, ParametroInvalidoException, EmailException, MalformedURLException {
        //obtener el usuario registrado con ese numero de telefono
        ManagerUser managerUser = new ManagerUser();
        String tipoEnvio;
        Query q;
        if (identifier.contains("@")) { //es un correo
            tipoEnvio = "mail";
            q = DBQuery.is("mail", identifier);
        } else {
            if (identifier.matches("^[0-9]{10}$")) { //es numero de 10 digitos
                tipoEnvio = "phone";
                q = DBQuery.is("phone", identifier);
            } else {
                throw new ParametroInvalidoException("el identificador proporsionado no es váliodo. Debe de utilizar un correo electronico ó número de teléfono de 10 dígitos");
            }
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
            switch (tipoEnvio) {
                case "mail":
                    UtilsMail.sendRecuperarContraseñaHTMLMail(identifier, u.getUser(), code);
                    break;
                case "phone":
                    UtilsSMS.sendSMS(identifier, "Hola " + u.getUser() +" su código de recuperacion de contraseña es: " + code);
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

    public boolean resetPassword(String userId, String pass) {
        User u = this.findOne(userId);
        u.setPass(UtilsSecurity.cifrarMD5(pass));
        return this.update(u);
    }
}
