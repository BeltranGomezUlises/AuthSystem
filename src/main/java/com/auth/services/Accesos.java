/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auth.services;

import com.auth.entities.admin.Usuario;
import com.auth.managers.admin.ManagerUsuario;
import com.auth.managers.exceptions.ParametroInvalidoException;
import com.auth.managers.exceptions.TokenExpiradoException;
import com.auth.managers.exceptions.TokenInvalidoException;
import com.auth.managers.exceptions.UsuarioInexistenteException;
import com.auth.models.ModelCodigoRecuperacionUsuario;
import com.auth.models.ModelContenidoCifrado;
import com.auth.models.ModelLogin;
import com.auth.models.ModelUsuarioLogeado;
import com.auth.models.Respuesta;
import com.auth.models.enums.Status;
import com.auth.utils.UtilsJWT;
import com.auth.utils.UtilsJson;
import com.auth.utils.UtilsPermissions;
import com.auth.utils.UtilsSecurity;
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

/**
 * servicios de accesos al sistema
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/accesos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Accesos {

    /**
     * servicio de login del sistema
     *
     * @param modelLogin Modelo de inicio de sesion
     * @return en data: el usuario logeado, en metadata: el token de sesion
     * @throws java.lang.Exception
     */
    @POST
    @Path("/iniciarSesion")
    public ModelUsuarioLogeado login(ModelLogin modelLogin) throws Exception {
        ManagerUsuario managerUsuario = new ManagerUsuario();

        modelLogin.setPass(UtilsSecurity.cifrarMD5(modelLogin.getPass()));
        Usuario usuarioLogeado = managerUsuario.login(modelLogin);

        ModelUsuarioLogeado modelUsuarioLogeado = new ModelUsuarioLogeado();
        modelUsuarioLogeado.setPermisos(UtilsPermissions.permisosConmutadosDelUsuario(usuarioLogeado.getId()));

        modelUsuarioLogeado.setNombre(usuarioLogeado.getNombre());
        modelUsuarioLogeado.setTelefono(usuarioLogeado.getTelefono());
        modelUsuarioLogeado.setId(usuarioLogeado.getId());
        modelUsuarioLogeado.setCorreo(usuarioLogeado.getCorreo());
        modelUsuarioLogeado.setToken(UtilsJWT.generateSessionToken(usuarioLogeado.getId().toString()));

        return modelUsuarioLogeado;

    }

    /**
     * servicio para registrar las salidas del usuario del sistema
     *
     * @param token token de sesion
     * @return solo retorna los mensajes de despedida del sistema
     */
    @GET
    @Path("/cerrarSesion")
    public Respuesta logout(@HeaderParam("Authorization") String token) {
        Respuesta r;
        ManagerUsuario managerUsuario = new ManagerUsuario();
        try {
            managerUsuario.logout(token);
            r = new Respuesta(Status.OK, "Logout exitosó");
        } catch (Exception e) {
            r = new Respuesta(Status.ERROR, "Error de programación: " + e.getMessage());
        }
        return r;
    }

    /**
     * sirve para enviar código de recuperación a el destino en correo
     *
     * @param identifier correo electrónico a enviar el código de recuperación
     * @return retorna el token de verificacion, necesario para poder obtener el token de reseteo de contraseña
     */
    @GET
    @Path("/enviarCodigoRecuperacion/{identifier}")
    public Respuesta<String> recoverCode(@PathParam("identifier") String identifier) {
        Respuesta<String> r;
        try {
            ManagerUsuario managerUsuario = new ManagerUsuario();
            ModelCodigoRecuperacionUsuario recoverCode = managerUsuario.enviarCodigo(identifier);
            r = new Respuesta(Status.OK,
                    "El código para recuperar contraseña fué enviado a : " + identifier
                    + "token de verificacion de usuario, necesario para el siguiente servicio en la cabecera Authorization",
                    UtilsJWT.generateValidateUserToken(recoverCode));
        } catch (UsuarioInexistenteException | ParametroInvalidoException ex) {
            r = new Respuesta(Status.WARNING, "No se reconoce como un correo válido");
        } catch (Exception ex) {
            r = new Respuesta(Status.ERROR, "Error de programación: " + ex.getMessage());
        }
        return r;
    }

    /**
     * sirve para autenticar el usuario que quiere recuperar su contraseña, es necesario proporcionar el token de verificacion y el codigo obtenido desde el medio correo
     *
     * @param token token de verificación
     * @param code código obtenido por correo
     * @return retorna el token para restablecer la contraseña
     */
    @GET
    @Path("/tokenRestablecer/{code}")
    public Respuesta<String> getTokenReset(@HeaderParam("Authorization") String token, @PathParam("code") String code) {
        Respuesta<String> r;
        try {
            UtilsJWT.validateSessionToken(token);
            r = new Respuesta(Status.OK, "token para reseatear la contraseña", UtilsJWT.generateTokenResetPassword(token, code));
        } catch (IOException ex) {
            r = new Respuesta(Status.ERROR, "Error de programación: " + ex.getMessage());
        } catch (ParametroInvalidoException ex) {
            r = new Respuesta(Status.WARNING, "No se reconoce como parametro válido");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            r = new Respuesta(Status.WARNING, "El token expiró o es es invalido, intente el proceso completo de nuevo");
        }
        return r;
    }

    /**
     * sirve para restablecer la contraseña de un usuario
     *
     * @param tokenRestablecer token para restaurar de contraseña
     * @param content contenido cifrado con la clave publica con el texto de la nueva contraseña a asignar
     * @return retorna mensaje de éxito
     */
    @POST
    @Path("/restablecerContra")
    public Respuesta resetPassword(@HeaderParam("Authorization") String tokenRestablecer, ModelContenidoCifrado content) {
        Respuesta res;
        try {
            UtilsJWT.validateSessionToken(tokenRestablecer);
            Integer userId = UtilsJWT.getUserIdFrom(tokenRestablecer);
            String pass = UtilsSecurity.decryptBase64ByPrivateKey(content.getContent());

            ManagerUsuario managerUsuario = new ManagerUsuario();
            managerUsuario.setToken(tokenRestablecer);
            managerUsuario.resetPassword(userId, pass);
            res = new Respuesta(Status.OK, "Contraseña reestablecida con éxito");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            res = new Respuesta(Status.WARNING, "El token expiró o es es invalido, intente el proceso completo de nuevo");
        } catch (ParametroInvalidoException ex) {
            res = new Respuesta(Status.WARNING, "No puede ingresar una contraseña que ya fué utilizada, intente con otro por favor");
        } catch (Exception e) {
            res = new Respuesta(Status.ERROR, "Error de programación: " + e.getMessage());
        }
        return res;
    }
}
