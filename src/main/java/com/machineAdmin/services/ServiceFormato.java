/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services;

import com.machineAdmin.entities.Formato;
import com.machineAdmin.managers.ManagerFormato;
import com.machineAdmin.models.filters.FilterFormato;
import com.machineAdmin.models.Response;
import com.machineAdmin.models.enums.Status;
import java.util.List;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/formatos2")
public class ServiceFormato extends ServiceFacade<Formato> {

    public ServiceFormato() {
        super(new ManagerFormato());
    }

    @POST
    @Path("/filtrado")
    public Response find(FilterFormato f) {        
        Response response = new Response();        
        try {            
            ManagerFormato myManager = new ManagerFormato();            
            response.setBody(myManager.findAll(f));
            response.setMessage("Formatos encontrados con filtro");
        } catch (Exception e) {
            response.setStatus(Status.ERROR);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response delete(Formato t) {
        return super.delete(t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response put(Formato t) {
        return super.put(t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response post(Formato t) {
        return super.post(t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response get() {
        return super.get(); //To change body of generated methods, choose Tools | Templates.
    }       

    @Override
    public Response get(String id) {
        return super.get(id);
    }
    
}
