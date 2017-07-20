/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services.cg.administracion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.managers.cg.admin.postgres.ManagerUsuario;
import com.machineAdmin.managers.cg.exceptions.ContraseñaIncorrectaException;
import com.machineAdmin.managers.cg.exceptions.ParametroInvalidoException;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioBlockeadoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.models.cg.ModelEncryptContent;
import com.machineAdmin.models.cg.ModelRecoverCodeUser;
import com.machineAdmin.models.cg.ModelUsuarioLogeado;
import com.machineAdmin.models.cg.responsesCG.Response;
import static com.machineAdmin.services.cg.commons.ServiceFacade.*;
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
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.mail.EmailException;

/**
 * servicios de accesos al sistema
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/accesos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Accesos {

    @POST
    @Path("/login")
    public Response login(ModelEncryptContent content) {
        Response res = new Response();
        ManagerUsuario managerUsuario = new ManagerUsuario();
        try {
            Usuario usuarioAutenticando = UtilsJson.jsonDeserialize(UtilsSecurity.decryptBase64ByPrivateKey(content.getContent()), Usuario.class);
            usuarioAutenticando.setContra(UtilsSecurity.cifrarMD5(usuarioAutenticando.getContra()));
            Usuario usuarioLogeado = managerUsuario.login(usuarioAutenticando);

            ModelUsuarioLogeado modelUsuarioLogeado = new ModelUsuarioLogeado();

            BeanUtils.copyProperties(modelUsuarioLogeado, usuarioLogeado);

            res.setData(modelUsuarioLogeado);
            res.setMetaData(UtilsJWT.generateSessionToken(usuarioLogeado.getId().toString()));
            res.setMessage("Bienvenido " + usuarioLogeado.getNombre());
            res.setDevMessage("Token de sesion de usuario, necesario para las cabeceras de los demas servicios");
        } catch (UsuarioInexistenteException | ContraseñaIncorrectaException e) {
            setWarningResponse(res, "Usuario y/o contraseña incorrecto", "imposible inicio de sesión, por: " + e.getMessage());
        } catch (UsuarioBlockeadoException ex) {
            setWarningResponse(res, ex.getMessage(), "El Usuario está bloqueado temporalmente. Cause: " + ex.getMessage());
        } catch (Exception ex) {
            setErrorResponse(res, ex);
        }
        return res;
    }

    @GET
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") String token) {
        Response res = new Response();
        ManagerUsuario managerUsuario = new ManagerUsuario();
        try {
            managerUsuario.logout(token);
            res.setMessage("Saliendo del sistema");
            res.setDevMessage("Registro de salida del sistema realizado");
        } catch (Exception ex) {
            setErrorResponse(res, ex);
        }
        return res;
    }

    @GET
    @Path("/publicKey")
    public Response getPublicKey() {
        Response r = new Response();
        setOkResponse(r, UtilsSecurity.getPublicKey(), "llave publica de cifrado RSA Base64");
        return r;
    }

    @GET
    @Path("/recoverCode/{identifier}")
    public Response recoverCode(@PathParam("identifier") String identifier) {
        Response res = new Response();
        try {
            ManagerUsuario managerUsuario = new ManagerUsuario();
            ModelRecoverCodeUser recoverCode = managerUsuario.enviarCodigo(identifier);
            setOkResponse(res,
                    "El código para recuperar contraseña fué enviado a : " + identifier,
                    "token de verificacion de usuario, necesario para el siguiente servicio en la cabecera Authorization");
            res.setMetaData(UtilsJWT.generateValidateUserToken(recoverCode));
        } catch (UsuarioInexistenteException ex) {
            setWarningResponse(res, ex.getMessage(), ex.getMessage());
        } catch (ParametroInvalidoException ex) {
            setWarningResponse(res, ex.getMessage(), "El parametro enviado no es correo ni numero de telefono");
        } catch (EmailException | MalformedURLException ex) {
            setErrorResponse(res, ex, "No fue posible enviar el código de recuperación, intente mas tarde");
        } catch (JsonProcessingException ex) {
            setErrorResponse(res, ex);
        }
        return res;
    }

    @GET
    @Path("/tokenResetPassword/{code}")
    public Response getTokenReset(@HeaderParam("Authorization") String token, @PathParam("code") String code) {
        Response res = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            res.setMetaData(UtilsJWT.generateTokenResetPassword(token, code));
        } catch (IOException ex) {
            setErrorResponse(res, ex, "No fué posible verificar el código proporsionado, intente mas tarde");
        } catch (ParametroInvalidoException ex) {
            setWarningResponse(res, ex.getMessage());
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(res);
        }
        return res;
    }

    @POST
    @Path("/resetPassword")
    public Response resetPassword(@HeaderParam("Authorization") String tokenResetPassword, ModelEncryptContent content) {
        Response res = new Response();
        try {
            UtilsJWT.validateSessionToken(tokenResetPassword);
            String userId = UtilsJWT.getBodyToken(tokenResetPassword);
            String pass = UtilsSecurity.decryptBase64ByPrivateKey(content.getContent());

            ManagerUsuario managerUsuario = new ManagerUsuario();
            managerUsuario.resetPassword(userId, pass);

            res.setMessage("La contraseña fué restablecida con éxito");

        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch (Exception ex) {
            setErrorResponse(res, ex);
        }
        return res;
    }

}
