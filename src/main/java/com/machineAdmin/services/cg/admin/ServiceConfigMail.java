/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services.cg.admin;

import com.machineAdmin.entities.cg.admin.ConfigMail;
import com.machineAdmin.managers.cg.admin.ManagerConfigMail;
import com.machineAdmin.models.cg.responses.Response;
import com.machineAdmin.services.cg.ServiceFacade;
import javax.ws.rs.Path;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/configMail")
public class ServiceConfigMail extends ServiceFacade<ConfigMail>{
    
    public ServiceConfigMail() {
        super(new ManagerConfigMail());
    }

    @Override
    public Response delete(String token, ConfigMail t) {
        return super.delete(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response put(String token, ConfigMail t) {
        return super.put(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response post(String token, ConfigMail t) {
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
