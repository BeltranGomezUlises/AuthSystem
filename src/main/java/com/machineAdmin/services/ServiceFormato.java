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
            response.setBody(myManager.findAll(f));
            response.setMessage("Formatos encontrados con filtro");
        } catch (Exception e) {
            response.setStatus(Status.ERROR);
            response.setMessage(e.getMessage());
        }
        return response;
    }  

}
