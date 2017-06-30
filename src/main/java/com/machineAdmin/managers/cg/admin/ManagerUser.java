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
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsMail;
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
    
    public User Login(User usuario) throws UsuarioInexistenteException{
        Query q = DBQuery
                .is("user", usuario.getUser())
                .is("pass", usuario.getPass());        
        User loged = this.findOne(q);
        if (loged != null) {
            return loged;
        }else{
            throw new UsuarioInexistenteException("No se encontro un usuario con esa contraseña");
        }
    }
    
    /**    
     * @param mail correo del usuario a enviar codigo de reseteo de contraseña
     * @throws org.apache.commons.mail.EmailException
     * @throws java.net.MalformedURLException
     * @throws com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException
     */
    public void enviarCodigoMail(String mail) throws EmailException, MalformedURLException, UsuarioInexistenteException{                
        //obtener el usuario registrado con ese mail
        ManagerUser managerUser = new ManagerUser();
        
        Query q = DBQuery.is("mail", mail);
        User u = managerUser.findOne(q);
        if (u != null) {
            Random r = new Random();
            
            //generar codigo de 8 digitos aleatorios
            String code = String.valueOf(r.nextInt(99));
            code += String.valueOf(r.nextInt(99));
            code += String.valueOf(r.nextInt(99));
            code += String.valueOf(r.nextInt(99));
            
            //asignar codigo de recuperacion al usuario
            u.setResetPasswordCode(code);
            if (managerUser.update(u)) {
                //enviar correo con codigo de recuperacion
                UtilsMail.sendRecuperarContraseñaHTMLMail(mail, code);
            }                                    
        }else{
             throw new UsuarioInexistenteException("No se encontro usuario con el correo especificado");
        }                        
    }
    
    public void enviarCodigoSMS(String phone){
        //obtener el usuario registrado con ese numero de telefono
        ManagerUser managerUser = new ManagerUser();
        
        Query q = DBQuery.is("phone", phone);
        User u = managerUser.findOne(q);
        if (u != null) {
            Random r = new Random();
            
            //generar codigo de 8 digitos aleatorios
            String code = String.valueOf(r.nextInt(99));
            code += String.valueOf(r.nextInt(99));
            code += String.valueOf(r.nextInt(99));
            code += String.valueOf(r.nextInt(99));
            
            //asignar codigo de recuperacion al usuario
            u.setResetPasswordCode(code);
            if (managerUser.update(u)) {
                //enviar correo con codigo de recuperacion
                UtilsMail.sendRecuperarContraseñaHTMLMail(mail, code);
            }                                    
        }else{
             throw new UsuarioInexistenteException("No se encontro usuario con el correo especificado");
        }
    }
    
    public String generateTokenResetPassword(String mail, String code) throws UsuarioInexistenteException, JsonProcessingException{
        //obtener el usuario registrado con ese mail y el codigo 
        ManagerUser managerUser = new ManagerUser();        
        Query q = DBQuery.is("mail", mail).is("resetPasswordCode", code);
        User u = managerUser.findOne(q);
        if (u != null) {            
            //asignar codigo de recuperacion al usuario a vacío
            u.setResetPasswordCode(null);
            if (managerUser.update(u)) {
              return UtilsJWT.generateToken(u);
            }else{
               return "No se logró generar el token de reseteo de contraseña";
            }                
        }else{
             throw new UsuarioInexistenteException("No se encontro usuario con el correo especificado y el código de recuperación ingresado");
        }                                
    }
    
    public boolean resetPassword(User u, String pass){        
        u.setPass(UtilsSecurity.cifrarMD5(pass));
        return this.update(u);        
    }
}
