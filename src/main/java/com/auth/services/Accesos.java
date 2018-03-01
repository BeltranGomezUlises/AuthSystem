/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auth.services;

import com.auth.entities.admin.Usuario;
import com.auth.managers.admin.ManagerUsuario;
import com.auth.managers.exceptions.TokenExpiradoException;
import com.auth.managers.exceptions.TokenInvalidoException;
import com.auth.models.ModelContenidoCifrado;
import com.auth.models.ModelLogin;
import com.auth.models.ModelUsuarioLogeado;
import com.auth.models.Respuesta;
import com.auth.models.enums.Status;
import com.auth.utils.UtilsJWT;
import com.auth.utils.UtilsJson;
import com.auth.utils.UtilsPermissions;
import com.auth.utils.UtilsSecurity;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
     * servicio para obtener la clave publica de cifrado RSA
     *
     * @return retorna en metada la clave publica
     */
//    @GET
//    @Path("/llavePublica")
//    public Response getPublicKey() {
//        Response r = new Response();
//        r.setMetaData(UtilsSecurity.getPublicKey());
//        setOkResponse(r, "llave publica de cifrado RSA Base64");
//        return r;
//    }
    /**
     * sirve para enviar codigo de recuperacion a el destino (correo o telefono celular)
     *
     * @param identifier correo electrónico ó número de teléfono celular a enviar el código de recuperación
     * @return retorna el token de verificacion, necesario para poder obtener el token de reseteo de contraseña
     */
//    @GET
//    @Path("/enviarCodigoRecuperacion/{identifier}")
//    public Response recoverCode(@PathParam("identifier") String identifier) {
//        Response res = new Response();
//        try {
//            ManagerUsuario managerUsuario = new ManagerUsuario();
//            ModelCodigoRecuperacionUsuario recoverCode = managerUsuario.enviarCodigo(identifier);
//            setOkResponse(res,
//                    "El código para recuperar contraseña fué enviado a : " + identifier,
//                    "token de verificacion de usuario, necesario para el siguiente servicio en la cabecera Authorization");
//            res.setMetaData(UtilsJWT.generateValidateUserToken(recoverCode));
//        } catch (UsuarioInexistenteException ex) {
//            setWarningResponse(res, ex.getMessage(), ex.getMessage());
//        } catch (ParametroInvalidoException ex) {
//            setWarningResponse(res, ex.getMessage(), "El parametro enviado no es correo ni numero de telefono");
//        } catch (EmailException | MalformedURLException ex) {
//            setErrorResponse(res, ex, "No fue posible enviar el código de recuperación, intente mas tarde");
//        } catch (JsonProcessingException ex) {
//            setErrorResponse(res, ex);
//        }
//        return res;
//    }
//    @OPTIONS
//    @Path("/enviarCodigoRecuperacion/{identifier}")
//    public Response optionsRecoverCode(@PathParam("identifier") String identifier) {
//        return new Response();
//    }
    /**
     * sirve para autenticar el usuario que quiere recuperar su contraseña, es necesario proporcionar el token de verificacion y el codigo obtenido desde el medio (correo o telefono)
     *
     * @param token token de verificación
     * @param code código obtenido por correo o telefono
     * @return retorna el token para restablecer la contraseña
     */
//    @GET
//    @Path("/tokenRestablecer/{code}")
//    public Response getTokenReset(@HeaderParam("Authorization") String token, @PathParam("code") String code) {
//        Response res = new Response();
//        try {
//            UtilsJWT.validateSessionToken(token);
//            res.setMetaData(UtilsJWT.generateTokenResetPassword(token, code));
//        } catch (IOException ex) {
//            setErrorResponse(res, ex, "No fué posible verificar el código proporsionado, intente mas tarde");
//        } catch (ParametroInvalidoException ex) {
//            setWarningResponse(res, ex.getMessage());
//        } catch (TokenExpiradoException | TokenInvalidoException ex) {
//            setInvalidTokenResponse(res);
//        }
//        return res;
//    }
    /**
     * sirve para restablecer la contraseña de un usuario
     *
     * @param tokenRestablecer token para restaurar de contraseña
     * @param content contenido cifrado con la clave publica con el texto de la nueva contraseña a asignar
     * @return retorna mensaje de éxito
     */
//    @POST
//    @Path("/restablecerContra")
//    public Response resetPassword(@HeaderParam("Authorization") String tokenRestablecer, ModelContenidoCifrado content) {
//        Response res = new Response();
//        try {
//            UtilsJWT.validateSessionToken(tokenRestablecer);
//            Long userId = UtilsJWT.getUserIdFrom(tokenRestablecer);
//            String pass = UtilsSecurity.decryptBase64ByPrivateKey(content.getContent());
//
//            ManagerUsuario managerUsuario = new ManagerUsuario(tokenRestablecer, Profundidad.TODOS);
//            managerUsuario.resetPassword(userId, pass);
//
//            res.setMessage("La contraseña fué restablecida con éxito");
//        } catch (TokenExpiradoException | TokenInvalidoException e) {
//            setInvalidTokenResponse(res);
//        } catch (ParametroInvalidoException ex) {
//            setWarningResponse(res, "No puede ingresar una contraseña que ya fué utilizada, intente con otro por favor", ex.getMessage());
//        } catch (Exception e) {
//            setErrorResponse(res, e, "No se logro actualizar la contraseña, consulte con su administrador del sistema");
//        }
//        return res;
//    }
}
