/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services.cg.generales;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.admin.postgres.ManagerSeccion;
import com.machineAdmin.managers.cg.admin.postgres.ManagerUsuario;
import com.machineAdmin.managers.cg.exceptions.ContraseñaIncorrectaException;
import com.machineAdmin.managers.cg.exceptions.ParametroInvalidoException;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioBlockeadoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.models.cg.ModelContenidoCifrado;
import com.machineAdmin.models.cg.ModelCodigoRecuperacionUsuario;
import com.machineAdmin.models.cg.ModelUsuarioLogeado;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsJson;
import com.machineAdmin.utils.UtilsPermissions;
import com.machineAdmin.utils.UtilsSecurity;
import static com.machineAdmin.utils.UtilsService.*;
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

    /**
     * servicio de login del sistema
     *
     * @param content debera contener la cadena cifrada con el identificador del
     * usuario en el atributo nombre y contra a logear en formato json, EJ:
     * {"nombre":"Administrador", "contra":"1234"}, seria:
     * SEMZhVpciiV6VyKO4MwpMu92YiYlPoxMTz1ivHKhrKOtw5/2AXSoO/OZE3qmMDQ15C/Mxz9AsO1KbpH92Ov7+s58YnjmvNo+WhSAflsmXLBlYLaSZ9i3Wab9atsAC9+3Sh3it/qqFeyWDicyHVh3Au2d9j9sMGvzb2lM8ygiL+0=
     * @return en data: el usuario logeado, en metadata: el token de sesion
     */
    @POST
    @Path("/iniciarSesion")
    public Response login(ModelContenidoCifrado content) {
        Response res = new Response();
        ManagerUsuario managerUsuario = new ManagerUsuario();
        try {
            Usuario usuarioAutenticando = UtilsJson.jsonDeserialize(UtilsSecurity.decryptBase64ByPrivateKey(content.getContent()), Usuario.class);
            usuarioAutenticando.setContra(UtilsSecurity.cifrarMD5(usuarioAutenticando.getContra()));
            Usuario usuarioLogeado = managerUsuario.login(usuarioAutenticando);

            ModelUsuarioLogeado modelUsuarioLogeado = new ModelUsuarioLogeado();
            modelUsuarioLogeado.setPermisos(UtilsPermissions.permisosAsignadosAlUsuario(usuarioLogeado.getId()));

            BeanUtils.copyProperties(modelUsuarioLogeado, usuarioLogeado);

            setOkResponse(res, modelUsuarioLogeado, "BienVenido " + usuarioLogeado.getNombre(), "Token de sesion de usuario, necesario para las cabeceras de los demas servicios");
            res.setMetaData(UtilsJWT.generateSessionToken(usuarioLogeado.getId().toString()));
        } catch (UsuarioInexistenteException | ContraseñaIncorrectaException e) {
            setWarningResponse(res, "Usuario y/o contraseña incorrecto", "imposible inicio de sesión, por: " + e.getMessage());
        } catch (UsuarioBlockeadoException ex) {
            setWarningResponse(res, ex.getMessage(), "El Usuario está bloqueado temporalmente. Cause: " + ex.getMessage());
        } catch (Exception ex) {
            setErrorResponse(res, ex);
        }
        return res;
    }

    /**
     * servicio para registrar las salidas del usuario del sistema
     *
     * @param token token de sesion
     * @return solo retorna los mensajes de despedida del sistema
     */
    @GET
    @Path("/cerrarSesion")
    public Response logout(@HeaderParam("Authorization") String token) {
        Response res = new Response();
        ManagerUsuario managerUsuario = new ManagerUsuario();
        try {
            managerUsuario.logout(token);
            res.setMessage("Saliendo del sistema");
            res.setDevMessage("Registro de salida del sistema realizado");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(res);
        } catch (Exception e) {
            setErrorResponse(res, e);
        }
        return res;
    }

    /**
     * servicio para obtener la clave publica de cifrado RSA
     *
     * @return retorna en metada la clave publica
     */
    @GET
    @Path("/llavePublica")
    public Response getPublicKey() {
        Response r = new Response();
        r.setMetaData(UtilsSecurity.getPublicKey());
        setOkResponse(r, "llave publica de cifrado RSA Base64");
        return r;
    }

    /**
     * sirve para enviar codigo de recuperacion a el destino (correo o telefono
     * celular)
     *
     * @param identifier correo electrónico ó número de teléfono celular a
     * enviar el código de recuperación
     * @return retorna el token de verificacion, necesario para poder obtener el
     * token de reseteo de contraseña
     */
    @GET
    @Path("/enviarCodigoRecuperacion/{identifier}")
    public Response recoverCode(@PathParam("identifier") String identifier) {
        Response res = new Response();
        try {
            ManagerUsuario managerUsuario = new ManagerUsuario();
            ModelCodigoRecuperacionUsuario recoverCode = managerUsuario.enviarCodigo(identifier);
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

    /**
     * sirve para autenticar el usuario que quiere recuperar su contraseña, es
     * necesario proporcionar el token de verificacion y el codigo obtenido
     * desde el medio (correo o telefono)
     *
     * @param token token de verificación
     * @param code código obtenido por correo o telefono
     * @return retorna el token para restablecer la contraseña
     */
    @GET
    @Path("/tokenRestablecer/{code}")
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

    /**
     * sirve para restablecer la contraseña de un usuario
     *
     * @param tokenRestablecer token para restaurar de contraseña
     * @param content contenido cifrado con la clave publica con el texto de la
     * nueva contraseña a asignar
     * @return retorna mensaje de éxito
     */
    @POST
    @Path("/restablecerContraseña")
    public Response resetPassword(@HeaderParam("Authorization") String tokenRestablecer, ModelContenidoCifrado content) {
        Response res = new Response();
        try {
            UtilsJWT.validateSessionToken(tokenRestablecer);
            Integer userId = UtilsJWT.getUserIdFrom(tokenRestablecer);
            String pass = UtilsSecurity.decryptBase64ByPrivateKey(content.getContent());

            ManagerUsuario managerUsuario = new ManagerUsuario(tokenRestablecer, Profundidad.TODOS);
            managerUsuario.resetPassword(userId, pass);

            res.setMessage("La contraseña fué restablecida con éxito");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch (ParametroInvalidoException ex) {
            setWarningResponse(res, "No puede ingresar una contraseña que ya fué utilizada, intente con otro por favor", ex.getMessage());
        } catch (Exception e) {
            setErrorResponse(res, e, "No se logro actualizar la contraseña, consulte con su administrador del sistema");
        }
        return res;
    }
    
}
