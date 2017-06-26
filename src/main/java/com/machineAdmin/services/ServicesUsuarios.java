/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services;

import com.machineAdmin.entities.business.Usuario;
import com.machineAdmin.managers.ManagerUsuario;
import com.machineAdmin.models.cg.enums.responses.Response;
import com.machineAdmin.services.cg.ServiceFacade;
import com.machineAdmin.utils.UtilsMail;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author ulises
 */
@Path("/usuarios")
public class ServicesUsuarios extends ServiceFacade<Usuario>{
    
    public ServicesUsuarios() {
        super(new ManagerUsuario());
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

    @Override
    public Response get(String token) {
        return super.get(token); //To change body of generated methods, choose Tools | Templates.
    }
    
    @GET
    @Path("/mail")
    @Produces(MediaType.TEXT_PLAIN)
    public String sendMail(){
        UtilsMail.sendHTMLMail();
        return "sending";
    }
    
}
