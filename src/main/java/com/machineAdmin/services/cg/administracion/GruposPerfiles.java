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

import com.machineAdmin.entities.cg.admin.postgres.GrupoPerfiles;
import com.machineAdmin.managers.cg.admin.postgres.ManagerGrupoPerfil;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.ModelAsignarPerfilesAlGrupoPerfil;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.commons.ServiceFacadeCatalogSQL;
import static com.machineAdmin.utils.UtilsService.*;
import com.machineAdmin.utils.UtilsJWT;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/gruposPerfiles")
public class GruposPerfiles extends ServiceFacadeCatalogSQL<GrupoPerfiles, Integer> {

    public GruposPerfiles() {
        super(new ManagerGrupoPerfil());
    }

    @Override
    public Response eliminar(HttpServletRequest request, String token, GrupoPerfiles t) {
        return super.eliminar(request, token, t);
    }

    @Override
    public Response modificar(HttpServletRequest request, String token, GrupoPerfiles t) {
        return super.modificar(request, token, t);
    }

    @Override
    public Response alta(HttpServletRequest request, String token, GrupoPerfiles t) {
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
     * sirve para asignar a un grupo de perfiles, una lista de perfiles para
     * agruparlos
     *
     * @param token token de sesion
     * @param modelo modelos contenedor para asignar perfiles, debe contener el
     * id del grupo perfil y la lista de ids de los perfiles a agrupar
     * @return retorna mensaje de éxito
     */
    @Path("/asignarPerfiles")
    @POST
    public Response asignarPerfiles(@HeaderParam("Authorization") String token, ModelAsignarPerfilesAlGrupoPerfil modelo) {
        Response res = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            ManagerGrupoPerfil managerGrupoPerfil = new ManagerGrupoPerfil();            
            managerGrupoPerfil.asignarPerfiles(modelo);
            res.setMessage("Los Permisos fuéron asignados al perfil con éxito");
            res.setDevMessage("Permisos asignado al perfil");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(res);
        } catch (Exception e) {
            setErrorResponse(res, e);
        }
        return res;

    }

}