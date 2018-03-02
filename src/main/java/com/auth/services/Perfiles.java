/*
 * Copyright (C) 2017 Alonso --- alonso@kriblet.com
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
package com.auth.services;

import com.auth.entities.admin.Perfil;
import com.auth.managers.admin.ManagerPerfil;
import com.auth.managers.exceptions.TokenExpiradoException;
import com.auth.managers.exceptions.TokenInvalidoException;
import com.auth.models.ModelAsignarPermisos;
import com.auth.models.ModelPermisosAsignados;
import com.auth.utils.UtilsJWT;
import com.auth.utils.UtilsPermissions;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * servicios de administracion de perfiles
 *
 * @author Alonso --- alonso@kriblet.com
 */
@Path("/perfiles")
public class Perfiles extends ServiceFacade<Perfil, Integer> {

    public Perfiles() {
        super(new ManagerPerfil());
    }

    /**
     * asigna los permisos al perfil reemplazando los que tenia por los nuevos proporsionados
     *
     * @param token token de sesion
     * @param modelo modelo contenedor del perfil y la lista de permisos a asignar
     * @return sin data
     */
    @POST
    @Path("/asignarPermisos")
    public Perfil asignarPermisos(@HeaderParam("Authorization") String token, ModelAsignarPermisos modelo) throws TokenInvalidoException, TokenExpiradoException, Exception {
        ManagerPerfil managerPerfil = new ManagerPerfil();
        managerPerfil.setToken(token);
        managerPerfil.asignarPermisos(modelo);
        return this.detalle(token, modelo.getId());
    }

    /**
     * servicio para obtener la lista de los permisos que tiene asignado un perfil
     *
     * @param token token de sesion
     * @param perfilId id del perfil del cual buscar sus permisos
     * @return en data, la lista de permisos a obtener
     */
    @GET
    @Path("/permisos/{perfilId}")
    public ModelPermisosAsignados obtenerPermisos(@HeaderParam("Authorization") String token, @PathParam("perfilId") Integer perfilId) throws TokenExpiradoException, TokenInvalidoException, Exception {
        UtilsJWT.validateSessionToken(token);
        return UtilsPermissions.permisosAsignadosAlPerfil(perfilId);
    }

}
