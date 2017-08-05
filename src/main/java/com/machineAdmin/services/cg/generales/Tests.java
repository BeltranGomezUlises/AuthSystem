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

import com.machineAdmin.models.cg.ModelTest;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.commons.ServiceFacadeBase;
import com.machineAdmin.utils.UtilsBitacora;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/tests")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Tests {

    @GET
    @Path("/info")
    public Response explotarServidor(@Context HttpServletRequest request) {
        Response r = new Response();
                
        String ip = request.getRemoteAddr();
        String agent = request.getHeader("User-Agent");
        String authorization = request.getHeader("authorization");
        
        
        r.setData(ip);
        r.setMessage(agent);
        r.setDevMessage(authorization);
        System.out.println("----");        
        System.out.println(ip);
        System.out.println(agent);
        System.out.println(authorization);
        System.out.println("----");
        
        UtilsBitacora.ModeloBitacora bitacora = new UtilsBitacora.ModeloBitacora("ulises", new Date(), "test", ip, agent, ServiceFacadeBase.obtenerSistemaOperativo(agent).toString());
        
        UtilsBitacora.bitacorizar("test", bitacora);                        
        return r;
    }
    @GET
    @Path("/explotar")
    public Response explotarServidor() {
        Response r = new Response();

//        ManagerUsuario managerUsuario = new ManagerUsuario();        
//        Usuario u;
//        for (int i = 0; i < 1000000; i++) {
//            try {
//                u = new Usuario();
//                u.setNombre(UUID.randomUUID().toString());
//                u.setCorreo(UUID.randomUUID().toString());
//                u.setContra(UUID.randomUUID().toString());
//                
//                managerUsuario.persist(u);
//                
//                //persistir una relacion de usuarios con permisos
//                
//                ManagerUsuariosPermisos managerUsuariosPermisos = new ManagerUsuariosPermisos();
//                List<UsuariosPermisos> usuariosPermisoses = new ArrayList<>(100);
//                UsuariosPermisos usuariosPermisos;
//                for (Permiso existingPermission : UtilsPermissions.getExistingPermissions()) {
//                    usuariosPermisos = new UsuariosPermisos(u.getId(), existingPermission.getId());
//                    usuariosPermisos.setProfundidad(Profundidad.PROPIOS);
//                    usuariosPermisoses.add(usuariosPermisos);
//                }
//                                
//                managerUsuariosPermisos.persistAll(usuariosPermisoses);
//            } catch (Exception ex) {
//                ServiceFacade.setErrorResponse(r, ex);
//                Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        return r;
    }
    
    @POST
    @Path("/uuid")
    public Response uuid(ModelTest test){        
        Response res = new Response();
        try {
            
            res.setData(test);
            
            
            
        } catch (Exception e) {
            
        }        
        return res;       
        
    }
}
