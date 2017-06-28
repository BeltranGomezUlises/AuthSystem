/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services;

import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.managers.cg.admin.ManagerUser;
import com.machineAdmin.models.cg.responses.Response;
import com.machineAdmin.services.cg.ServiceFacade;
import com.machineAdmin.utils.UtilsMail;
import com.machineAdmin.utils.UtilsSecurity;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/users")
public class ServicesUsers extends ServiceFacade<User>{
    
    public ServicesUsers() {
        super(new ManagerUser());
    }

    @Override
    public Response delete(String token, User t) {
        return super.delete(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response put(String token, User t) {
        return super.put(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response post(String token, User t) {
        t.setPass(UtilsSecurity.cifrarMD5(t.getPass()));
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
        UtilsMail.sendHTMLMail("smtp.googlemail.com", 465, "beltrangomezulises@gmail.com", "ELECTRO-nic1", true, "prueba2", "html", "no soportado html", "ubg700@gmail.com");
        return "sending";
    }
    
}
