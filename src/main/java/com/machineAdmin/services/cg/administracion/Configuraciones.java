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

import com.machineAdmin.entities.cg.admin.mongo.CGConfig;
import com.machineAdmin.managers.cg.admin.mongo.ManagerCGConfig;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.commons.ServiceFacade;
import com.machineAdmin.utils.UtilsConfig;
import com.machineAdmin.utils.UtilsJWT;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;

/**
 * servicios de administracion de configuraciones generales
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/configs")
public class Configuraciones extends ServiceFacade<CGConfig, Object>{
    
    public Configuraciones() {
        super(new ManagerCGConfig());
    }

    @Override
    public Response modificar(String token, CGConfig t) {
        return super.modificar(token, t);
    }
    
    /**
     * sirve para obtener las configuraciones generales del sistema
     * @param token
     * @return contenedor de configuraciones generales
     */
    @Path("/generales")
    @GET    
    public Response getConfigGenerales(@HeaderParam("Authorization") String token){
        Response res = new Response();
        try {            
            UtilsJWT.validateSessionToken(token);
            res.setData(UtilsConfig.getCGConfig());
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch (Exception e) {
            setErrorResponse(res, e);
        }        
        return res;
    }
}
