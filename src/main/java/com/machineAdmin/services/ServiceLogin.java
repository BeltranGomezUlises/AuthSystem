/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services;

import com.machineAdmin.entities.admin.Usuario;
import com.machineAdmin.managers.ManagerUsuario;
import com.machineAdmin.managers.exceptions.UsuarioInexistenteException;
import com.machineAdmin.models.enums.Status;
import com.machineAdmin.models.responses.Response;
import com.machineAdmin.utils.UtilsDate;
import com.machineAdmin.utils.UtilsJWT;
import java.util.Date;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceLogin extends ServiceFacade<Usuario> {

    public ServiceLogin() {
        super(new ManagerUsuario());
    }

    @GET
    @Path("/{usuario}/{contra}")
    public Response login(@PathParam("usuario") String usuario, @PathParam("contra") String contra) {
        Response r = new Response();        
        ManagerUsuario managerUsuario = new ManagerUsuario();                
        try {
            r.setData(managerUsuario.Login(new Usuario(usuario, contra)));           
            r.setMetaData(UtilsJWT.generateToken());
        } catch (UsuarioInexistenteException e) {
            r.setStatus(Status.WARNING);
            r.setMessage("Usuario y/o contraseña incorrecto");
            setCauseMessage(r, e);
        } catch (Exception ex){
            r.setStatus(Status.ERROR);            
            setCauseMessage(r, ex);
        }       
        return r;
    }

    @Override
    public Response delete(String token, Usuario t) {
        return super.delete(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response put(String token, Usuario t) {
        return super.put(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response post(String token, Usuario t) {
        return super.post(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response get(String token, String id) {
        return super.get(token, id); //To change body of generated methods, choose Tools | Templates.
    }
    
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public Object test(){
        String fecha = UtilsDate.sdfUTC(new Date());
        String fecha2 = UtilsDate.sdf(new Date());
        String fecha3 = UtilsDate.sdfHM(new Date());
        String fecha4 = UtilsDate.sdfFull(new Date());
        return fecha;
    }
}
