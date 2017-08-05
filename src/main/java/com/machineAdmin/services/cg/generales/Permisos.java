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
package com.machineAdmin.services.cg.generales;

import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.admin.postgres.ManagerSeccion;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.responsesCG.Response;
import static com.machineAdmin.services.cg.commons.ServiceFacadeBase.*;
import com.machineAdmin.utils.UtilsJWT;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * servicios relacionados con los permisos del sistema en general
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/permisos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Permisos {

    /**
     * sirve para obtener la lista de todos los permisos que pueden ser
     * asignados a un usuario o perfil
     *
     * @param token token de sesion
     * @return retorna en data modelo "Seccion"
     */
    @GET
    @Path("/disponibles")
    public Response getPermisosDisponibles(@HeaderParam("Authorization") String token) {
        Response res = new Response();
        try {
            ManagerSeccion managerSeccion = new ManagerSeccion();
            managerSeccion.setToken(token);
            res.setData(managerSeccion.findAll());            
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch (Exception ex) {
            setErrorResponse(res, ex);
        }
        return res;
    }

    /**
     * obtiene las profundides permitidas para asignar
     * @param token token de sesion
     * @return  en data, la lista de profundidaes disponibles
     */
    @GET
    @Path("/profundidades")
    public Response getProfundidades(@HeaderParam("Authorization") String token){
        Response res = new Response();
        try {
            UtilsJWT.validateSessionToken(token);            
            res.setData(Profundidad.values());
            res.setDevMessage("profundidades disponibles para los permisos");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        }
        return res;
    }
    
}
