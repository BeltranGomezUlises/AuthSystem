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
package com.machineAdmin.services.cg.administracion;

import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.managers.cg.admin.postgres.ManagerUsuario;
import com.machineAdmin.managers.cg.admin.postgres.ManagerUsuariosPerfil;
import com.machineAdmin.managers.cg.admin.postgres.ManagerUsuariosPermisos;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.ModelAsignarPerfilesAlUsuario;
import com.machineAdmin.models.cg.ModelAsignarPermisos;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.commons.ServiceFacade;
import static com.machineAdmin.services.cg.commons.ServiceFacade.setErrorResponse;
import static com.machineAdmin.services.cg.commons.ServiceFacade.setInvalidTokenResponse;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsPermissions;
import java.util.UUID;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * servicios de administracion de usuarios del sistema
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/usuarios")
public class Usuarios extends ServiceFacade<Usuario, UUID> {

    public Usuarios() {
        super(new ManagerUsuario());
    }   

    @Override
    public Response eliminar(String token, Usuario t) {
        return super.eliminar(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response modificar(String token, Usuario t) {
        return super.modificar(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response alta(String token, Usuario t) {
        return super.alta(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response obtener(String token, String id) {
        return super.obtener(token, id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response listar(String token) {
        return super.listar(token); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * asigna los permisos al usuario reemplazando los que tenia por los nuevos
     * proporsionados
     *
     * @param token token de sesion
     * @param modelo modelo generico para relacionar los permisos al usuario
     * @return modelo response generico
     */
    @POST
    @Path("/asignarPermisos")
    public Response asignarPermisos(@HeaderParam("Authorization") String token, ModelAsignarPermisos modelo) {
        Response res = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            ManagerUsuariosPermisos managerUsuariosPermisos = new ManagerUsuariosPermisos();
            managerUsuariosPermisos.asignarPermisos(modelo);
            setOkResponse(res, "Los permisos fuerons asignados al usuario con éxito", "se asignaron los permisos al usuario");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch (Exception ex) {
            setErrorResponse(res, ex);
        }
        return res;
    }

    /**
     * asigna los perfiles al usuario reemplazando los que tenia por los nuevos
     * proporsionados
     *
     * @param token token de sesion
     * @param modelo modelo generico para relacionar los permisos al usuario
     * @return modelo response generico
     */
    @POST
    @Path("/asignarPerfiles")
    public Response asignarPerfiles(@HeaderParam("Authorization") String token, ModelAsignarPerfilesAlUsuario modelo) {
        Response res = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            ManagerUsuariosPerfil managerUsuariosPerfil = new ManagerUsuariosPerfil();
            managerUsuariosPerfil.asignarPerfilesAlUsuario(modelo);
            setOkResponse(res, "Los permisos fuerons asignados al usuario con éxito", "se asignaron los permisos al usuario");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch (Exception ex) {
            setErrorResponse(res, ex);
        }
        return res;
    }
    
    /**
     * sirve para obtener la lista de permisos asignados al usuario junto a su profundidad de acceso
     * @param token token de sesion
     * @param usuario id de usuario a obtener permisos
     * @return retorna en data, la lista de permisos asignados al usuario
     */
    @GET
    @Path("/permisos/{userId}")
    public Response obtenerPermisosAsignados(@HeaderParam("Authorization") String token, @PathParam("userId") String usuario){
        Response res = new Response();
        try {            
            UtilsJWT.validateSessionToken(token);
            res.setData(UtilsPermissions.permisosAsignadosAUsuario(usuario));
            res.setDevMessage("permisos que el usuario tiene asignado");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch(Exception ex){
            setErrorResponse(res, ex);  
        }        
        return res;        
    }
    
    /**
     * sirve para obtener la lista de perfiles asignados al usuario
     * @param token token de sesion
     * @param usuario id de usuario a obtener sus perfiles
     * @return retorna en data, la lista de perfiles asignados al usuario
     */
    @GET
    @Path("/perfiles/{userId}")
    public Response obtenerPerfilesAsignados(@HeaderParam("Authorization") String token, @PathParam("userId") String usuario){
        Response res = new Response();
        try {            
            UtilsJWT.validateSessionToken(token);
            ManagerUsuariosPerfil managerUsuariosPerfil = new ManagerUsuariosPerfil();
            
            
            
            res.setDevMessage("permisos que el usuario tiene asignado");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch(Exception ex){
            setErrorResponse(res, ex);  
        }        
        return res;        
    }
}
