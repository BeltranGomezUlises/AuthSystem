/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services.cg.administracion;

import com.machineAdmin.entities.cg.admin.mongo.ConfigMail;
import com.machineAdmin.managers.cg.admin.mongo.ManagerConfigMail;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.commons.ServiceBitacoraFacade;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

/**
 * servicios de administracion de correos del sistema para soporte, contacto y recuperacion de usuarios
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/correos")
public class Correos extends ServiceBitacoraFacade<ConfigMail, Object>{
    
    public Correos() {
        super(new ManagerConfigMail());
    }

    @Override
    public Response eliminar(HttpServletRequest request, String token, ConfigMail t) {
        return super.eliminar(request, token, t);
    }

    @Override
    public Response modificar(HttpServletRequest request, String token, ConfigMail t) {
        return super.modificar(request, token, t);
    }

    @Override
    public Response alta(HttpServletRequest request, String token, ConfigMail t) {
        return super.alta(request, token, t);
    }

    @Override
    public Response detalle(HttpServletRequest request, String token, String id) {
        return super.detalle(request, token, id);
    }

    @Override
    public Response listar(HttpServletRequest request, String token) {
        return super.listar(request, token);
    }
           
}
