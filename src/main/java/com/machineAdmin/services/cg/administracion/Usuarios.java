/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services.cg.administracion;

import com.machineAdmin.entities.cg.admin.mongo.User;
import com.machineAdmin.managers.cg.admin.mongo.ManagerUser;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.ModelSetPermission;
import com.machineAdmin.models.cg.ModelSetProfilesToUsuario;
import com.machineAdmin.models.cg.ModelUserId;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.commons.ServiceFacade;
import com.machineAdmin.utils.UtilsJWT;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * servicios de administracion de usuarios del sitema
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/usuarios")
public class Usuarios extends ServiceFacade<User> {
    
    public Usuarios() {
        super(new ManagerUser());
    }
    
    @Override
    public Response modificar(String token, User t) {
        return super.modificar(token, t);
    }
    
    @Override
    public Response alta(String token, User t) {
        return super.alta(token, t);
    }
    
    @Override
    public Response obtener(String token, String id) {
        return super.obtener(token, id); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Response listar(String token) {
        return super.listar(token); //To change body of generated methods, choose Tools | Templates.
    }
    
    @POST
    @Path("/inhabilitar")
    public Response inhabilitar(@HeaderParam("Authorization") String token, ModelUserId modelUserId) {
        Response res = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            ManagerUser managerUser = new ManagerUser();
            boolean fueHabilitado = managerUser.inhabilitar(modelUserId.getId());
            if (fueHabilitado) {
                res.setMessage("Usuario habilitado");
                res.setDevMessage("El usuario fue habilitado");
            } else {
                res.setMessage("Usuario Inhabilitado");
                res.setDevMessage("El usuario fue inhabilitado");
            }
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch (Exception ex) {
            setErrorResponse(res, ex);
        }
        return res;
    }
    
    @POST
    @Path("/asignarPermisos")    
    public Response asignarPermisos(@HeaderParam("Authorization") String token, ModelSetPermission modelSetPermissionUser) {
        Response res = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            ManagerUser managerUser = new ManagerUser();
            managerUser.setPermissionToUser(modelSetPermissionUser);
            res.setDevMessage("Permisos del usuario actualizados");
            res.setDevMessage("Los permisos del usuario fueron actualizados con éxito");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(res);
        } catch (Exception ex) {
            setErrorResponse(res, ex);
        }
        return res;
    }
                
    @POST
    @Path("/asignarPerfiles")        
    public Response asignarPerfiles(@HeaderParam("Authorization") String token, ModelSetProfilesToUsuario modelSetProfilesToUsuario){
        Response res = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            ManagerUser managerUser = new ManagerUser();
            managerUser.setProfiles(modelSetProfilesToUsuario);
            res.setDevMessage("Roles del usuario actualizados");
            res.setDevMessage("Los roles del usuario fueron actualizados con éxito");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(res);
        } catch (Exception ex) {
            setErrorResponse(res, ex);
        }
        return res;
    }
    
    //falta poder agregar los perfiles de un grupo de perfiles
    
    //al crear el usuario debes de crearlo con un perfil?
    
    //al crear el perfil debe de tener permisos?
    
    //al crear un grupo de perfiles, debe de tener perfiles?
    
    
    
}
