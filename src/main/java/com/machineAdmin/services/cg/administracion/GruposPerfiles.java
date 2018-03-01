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

import com.machineAdmin.entities.admin.GrupoPerfiles;
import com.machineAdmin.managers.admin.ManagerGrupoPerfil;
import com.machineAdmin.models.cg.ModelAsignarPerfilesAlGrupoPerfil;
import com.machineAdmin.services.cg.commons.ServiceFacade;
import com.machineAdmin.utils.UtilsJWT;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/gruposPerfiles")
public class GruposPerfiles extends ServiceFacade<GrupoPerfiles, Integer> {

    public GruposPerfiles() {
        super(new ManagerGrupoPerfil());
    }


    /**
     * sirve para asignar a un grupo de perfiles, una lista de perfiles para agruparlos
     *
     * @param token token de sesion
     * @param modelo modelos contenedor para asignar perfiles, debe contener el id del grupo perfil y la lista de ids de los perfiles a agrupar
     * @return retorna mensaje de éxito
     * @throws java.lang.Exception
     */
    @Path("/asignarPerfiles")
    @POST
    public GrupoPerfiles asignarPerfiles(@HeaderParam("Authorization") String token, ModelAsignarPerfilesAlGrupoPerfil modelo) throws Exception {
        UtilsJWT.validateSessionToken(token);
        ManagerGrupoPerfil managerGrupoPerfil = new ManagerGrupoPerfil();
        return managerGrupoPerfil.asignarPerfiles(modelo);
    }

}
