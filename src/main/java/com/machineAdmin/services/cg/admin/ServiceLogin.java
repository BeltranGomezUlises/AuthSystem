/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services.cg.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.managers.cg.admin.ManagerUser;
import com.machineAdmin.managers.cg.exceptions.ContraseñaIncorrectaException;
import com.machineAdmin.managers.cg.exceptions.ParametroInvalidoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioBlockeadoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.models.cg.ModelEncryptContent;
import com.machineAdmin.models.cg.ModelRecoverCodeUser;
import com.machineAdmin.models.cg.enums.Status;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.ServiceFacade;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsJson;
import com.machineAdmin.utils.UtilsSecurity;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceLogin {

    @POST
    public Response login(ModelEncryptContent content) {
        Response res = new Response();
        ManagerUser managerUsuario = new ManagerUser();
        try {
            User usuarioAutenticando = UtilsJson.jsonDeserialize(UtilsSecurity.decryptBase64ByPrivateKey(content.getContent()), User.class);
            usuarioAutenticando.setPass(UtilsSecurity.cifrarMD5(usuarioAutenticando.getPass()));
            User usuarioLogeado = managerUsuario.login(usuarioAutenticando);
            
            //no regresar estos datos ó mapear a modelo
            usuarioLogeado.setPass(null);
            usuarioLogeado.setBlocked(null);
            usuarioLogeado.setLoginAttempt(null);
            
            res.setData(usuarioLogeado);
            res.setMetaData(UtilsJWT.generateToken(usuarioLogeado));                                          
            res.setMessage("Bienvenido " + usuarioLogeado.getUser());
            res.setDevMessage("Token de sesion de usuario, necesario para las cabeceras de los demas servicios");
        } catch (UsuarioInexistenteException | ContraseñaIncorrectaException e) {
            res.setStatus(Status.WARNING);
            res.setMessage("Usuario y/o contraseña incorrecto");
            res.setDevMessage("imposible inicio de sesión, por: " + e.getMessage());
        } catch (UsuarioBlockeadoException ex) {
            res.setStatus(Status.WARNING);
            res.setMessage(ex.getMessage());
            res.setDevMessage("El Usuario está bloqueado temporalmente. Cause: " + ex.getMessage());
        } catch (Exception ex) {            
            res.setStatus(Status.ERROR);
            ServiceFacade.setCauseMessage(res, ex);
        }
        return res;
    }

    @GET
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") String token){        
        Response res = new Response();
        ManagerUser managerUsuario = new ManagerUser();
        try {            
            managerUsuario.logout(token);                                    
            res.setMessage("Saliendo del sistema");
            res.setDevMessage("Registro de salida del sistema realizado");                                    
        } catch (Exception ex) {            
            res.setStatus(Status.ERROR);
            ServiceFacade.setCauseMessage(res, ex);
        }
        return res;
    }
    
    @GET
    @Path("/publicKey")
    public Response getPublicKey() {
        Response r = new Response();
        r.setData(UtilsSecurity.getPublicKey());
        r.setDevMessage("llave publica de cifrado RSA Base64");
        return r;
    }

    @GET
    @Path("/recoverCode/{identifier}")
    public Response recoverCode(@PathParam("identifier") String identifier) {
        Response res = new Response();
        try {
            ManagerUser managerUser = new ManagerUser();
            ModelRecoverCodeUser recoverCode = managerUser.enviarCodigo(identifier);
            res.setMetaData(UtilsJWT.generateValidateUserToken(recoverCode));
            res.setDevMessage("token de codigo para restaurar contraseña");
            res.setMessage("El código para recuperar contraseña fué enviado");
        } catch (UsuarioInexistenteException ex) {
            res.setStatus(Status.WARNING);
            res.setMessage("No se encontró el usuario con el identificador proporsionado");
            ServiceFacade.setCauseMessage(res, ex);
        } catch (ParametroInvalidoException ex) {
            res.setStatus(Status.WARNING);
            res.setMessage("El parametro de identificación de usuario no se reconoce como válido");
            ServiceFacade.setCauseMessage(res, ex);
        } catch (EmailException | MalformedURLException ex) {
            res.setStatus(Status.ERROR);
            res.setMessage("No fue posible enviar el código de recuperación, intente mas tarde");
            ServiceFacade.setCauseMessage(res, ex);
        } catch (JsonProcessingException ex) {
            res.setStatus(Status.ERROR);
            res.setMessage("No fue posible generar el token de recuperación, intente mas tarde");
            ServiceFacade.setCauseMessage(res, ex);
        }
        return res;
    }

    @GET
    @Path("/tokenResetPassword/{code}")
    public Response getTokenReset(@HeaderParam("Authorization") String token, @PathParam("code") String code) {
        Response res = new Response();
        if (UtilsJWT.isTokenValid(token)) {
            try {
                res.setMetaData(UtilsJWT.generateTokenResetPassword(token, code));
            } catch (IOException ex) {
                res.setStatus(Status.ERROR);
                res.setMessage("No fué posible verificar el código proporsionado, intente mas tarde");
                ServiceFacade.setCauseMessage(res, ex);
            } catch (ParametroInvalidoException ex) {
                res.setStatus(Status.WARNING);
                res.setMessage("No fué posible verificar el código proporsionado, intente repetir el proceso");
                ServiceFacade.setCauseMessage(res, ex);
            }
        } else {
            res.setMessage("No fué posible verificar el código proporsionado, intente repetir el proceso");
            res.setDevMessage("Token inválido");
            res.setStatus(Status.WARNING);
        }
        return res;
    }

    @POST
    @Path("/resetPassword")
    public Response resetPassword(@HeaderParam("Authorization") String tokenResetPassword, ModelEncryptContent content) {
        Response res = new Response();
        if (UtilsJWT.isTokenValid(tokenResetPassword)) {
            try {
                String userId = UtilsJWT.getBodyToken(tokenResetPassword);
                String pass = UtilsSecurity.decryptBase64ByPrivateKey(content.getContent());

                ManagerUser managerUser = new ManagerUser();
                managerUser.resetPassword(userId, pass);
                
                res.setMessage("La contraseña fué restablecida con éxito");               
            } catch (Exception ex) {
                res.setMessage("No se logró restablecer la contraseña, intente repetir el proceso completo");
                ServiceFacade.setCauseMessage(res, ex);
                res.setStatus(Status.ERROR);
            }
        } else {
            res.setStatus(Status.WARNING);
            res.setMessage("No se logró restablecer la contraseña, intente repetir el proceso completo");
            res.setDevMessage("token inválido");
        }
        return res;
    }

}
