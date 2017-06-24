/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services;

import com.machineAdmin.services.cg.ServiceFacade;
import com.machineAdmin.entities.business.Formato;
import com.machineAdmin.managers.ManagerFormato;
import com.machineAdmin.models.filters.FilterFormato;
import com.machineAdmin.models.cg.enums.responses.Response;
import com.machineAdmin.models.cg.enums.Status;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/formatos")
public class ServiceFormato extends ServiceFacade<Formato> {

    public ServiceFormato() {
        super(new ManagerFormato());
    }

    @POST
    @Path("/filtrado")
    public Response find(@HeaderParam("Authorization") String auth, FilterFormato f) {
        Response response = new Response();
        try {
            ManagerFormato myManager = new ManagerFormato();
            response.setData(myManager.findAll(f));
            response.setMessage("Formatos encontrados con filtro");
        } catch (Exception e) {
            response.setStatus(Status.ERROR);
            response.setMessage(e.getMessage());
        }
        return response;
    }  
 
    @Override
    public Response delete(String token, Formato t) {
        return super.delete(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response put(String token, Formato t) {
        return super.put(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response post(String token, Formato t) {
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
