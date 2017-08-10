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

import com.machineAdmin.entities.cg.admin.postgres.Perfil;
import com.machineAdmin.managers.cg.admin.postgres.ManagerPerfil;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.ModelAsignarPermisos;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.commons.ServiceFacade;
import static com.machineAdmin.utils.UtilsService.*;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsPermissions;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * servicios de administracion de perfiles
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/perfiles")
public class Perfiles extends ServiceFacade<Perfil, Integer> {

    public Perfiles() {
        super(new ManagerPerfil());
    }

    @Override
    public Response eliminar(HttpServletRequest request, String token, Perfil t) {
        return super.eliminar(request, token, t);
    }

    @Override
    public Response modificar(HttpServletRequest request, String token, Perfil t) {
        return super.modificar(request, token, t);
    }

    @Override
    public Response alta(HttpServletRequest request, String token, Perfil t) {
        return super.alta(request, token, t);
    }

    @Override
    public Response detalle(HttpServletRequest request, String token, String id) {
        return super.detalle(request, token, id);
    }

    @Override
    public Response listar(HttpServletRequest request, String token) {        
        return super.listar(request, token);
    }

    /**
     * asigna los permisos al perfil reemplazando los que tenia por los nuevos
     * proporsionados
     *
     * @param token token de sesion
     * @param modelo modelo contenedor del perfil y la lista de permisos a
     * asignar
     * @return sin data
     */
    @POST
    @Path("/asignarPermisos")
    public Response asignarPermisos(@HeaderParam("Authorization") String token, ModelAsignarPermisos modelo) {
        Response res = new Response();
        try {
            ManagerPerfil managerPerfil = new ManagerPerfil();
            managerPerfil.setToken(token);
            managerPerfil.asignarPermisos(modelo);
            res.setMessage("Los Permisos fuéron asignados al perfil con éxito");
            res.setDevMessage("Permisos asignado al perfil");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(res);
        } catch (Exception e) {
            setErrorResponse(res, e);
        }
        return res;
    }

    /**
     * servicio para obtener la lista de los permisos que tiene asignado un
     * perfil
     *
     * @param token token de sesion
     * @param perfilId id del perfil del cual buscar sus permisos
     * @return en data, la lista de permisos a obtener
     */
    @GET
    @Path("/permisos/{perfilId}")
    public Response obtenerPermisos(@HeaderParam("Authorization") String token, @PathParam("perfilId") Integer perfilId) {
        Response res = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            res.setData(UtilsPermissions.permisosAsignadosAlPerfil(perfilId));
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(res);
        } catch (Exception e) {
            setErrorResponse(res, e);
        }
        return res;
    }

}
