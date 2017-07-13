/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services.cg.admin;

import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.managers.cg.admin.ManagerUser;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.ServiceFacade;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/users")
public class ServiceUsers extends ServiceFacade<User>{
    
    public ServiceUsers() {
        super(new ManagerUser());
    }

    @Override
    public Response delete(String token, User t) {
    //delete solo deshabilita los usuarios
        return super.delete(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response put(String token, User t) {
        return super.put(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response post(String token, User t) {
        
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
             
}
