/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services.cg.administracion;

import com.machineAdmin.entities.cg.admin.mongo.ConfigMail;
import com.machineAdmin.managers.cg.admin.mongo.ManagerConfigMail;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.commons.ServiceFacade;
import javax.ws.rs.Path;

/**
 * servicios de administracion de correos del sistema para soporte, contacto y recuperacion de usuarios
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/correos")
public class Correos extends ServiceFacade<ConfigMail>{
    
    public Correos() {
        super(new ManagerConfigMail());
    }

    @Override
    public Response eliminar(String token, ConfigMail t) {
        return super.eliminar(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response modificar(String token, ConfigMail t) {
        return super.modificar(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response alta(String token, ConfigMail t) {
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
            
}
