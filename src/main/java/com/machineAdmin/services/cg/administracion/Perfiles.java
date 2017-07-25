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
import com.machineAdmin.managers.cg.admin.postgres.ManagerPerfilesPermisos;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.ModelAsignarPerfilesAlGrupoPerfil;
import com.machineAdmin.models.cg.ModelAsignarPermisosAlPerfil;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.commons.ServiceFacade;
import static com.machineAdmin.services.cg.commons.ServiceFacade.setInvalidTokenResponse;
import com.machineAdmin.utils.UtilsJWT;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/perfiles")
public class Perfiles extends ServiceFacade<Perfil>{
    
    public Perfiles() {
        super(new ManagerPerfil());
    }

    @Override
    public Response eliminar(String token, Perfil t) {
        return super.eliminar(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response modificar(String token, Perfil t) {
        return super.modificar(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response alta(String token, Perfil t) {
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
       
    @POST
    @Path("/asignarPermisos")
    public Response asignarPermisos(@HeaderParam("Authorization") String token, ModelAsignarPermisosAlPerfil modelo){
        Response res = new Response();       
        try {
            UtilsJWT.validateSessionToken(token);
            ManagerPerfilesPermisos managerPerfilesPermisos = new ManagerPerfilesPermisos();
            managerPerfilesPermisos.asignarPermisosAlPerfil(modelo);
            res.setMessage("Los Permisos fuéron asignados al perfil con éxito");
            res.setDevMessage("Permisos asignado al perfil");
        }catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(res);
        }  catch(Exception e){
            setErrorResponse(res, e);
        }
        return res;
    }
       
}
