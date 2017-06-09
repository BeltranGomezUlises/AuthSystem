package com.machineAdmin.services;

import com.machineAdmin.entities.Entity;
import com.machineAdmin.managers.ManagerFacade;
import com.machineAdmin.models.Response;
import com.machineAdmin.models.enums.Status;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> is an entity
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceFacade<T extends Entity> {

    ManagerFacade manager;

    public ServiceFacade(ManagerFacade manager) {
        this.manager = manager;
    }

    @GET
    public Response get() {
        Response response = new Response();
        try {
            response.setBody(manager.findAll());
            response.setMessage("Entidades encontradas");
        } catch (Exception e) {            
            response.setStatus(Status.ERROR);
            response.setMessage(e.getMessage());
            if (e.getCause() != null) {
                response.setMessage(e.getMessage() + e.getCause().getMessage());
            }
        }
        return response;
    }
    
    @GET
    @Path("/{id}")
    public Response get(@PathParam("id")String id) {
        Response response = new Response();
        try {
            response.setBody(manager.find(id));
            response.setMessage("Entidad encontrada");
        } catch (Exception e) {
            response.setStatus(Status.ERROR);
            response.setMessage(e.getMessage());
            if (e.getCause() != null) {
                response.setMessage(e.getMessage() + e.getCause().getMessage());
            }
        }
        return response;
    }

    @POST
    public Response post(T t) {
        Response response = new Response();
        try {
            response.setBody(manager.persist(t));
            response.setMessage("Entidad persistida");
        } catch (Exception e) {
            response.setStatus(Status.ERROR);
            response.setMessage(e.getMessage());
            if (e.getCause() != null) {
                response.setMessage(e.getMessage() + e.getCause().getMessage());
            }
        }
        return response;
    }

    @PUT
    public Response put(T t) {
        Response response = new Response();
        try {
            response.setBody(manager.update(t));
            response.setMessage("Entidad actualizada");
        } catch (Exception e) {
            response.setStatus(Status.ERROR);
            response.setMessage(e.getMessage());
            if (e.getCause() != null) {
                response.setMessage(e.getMessage() + e.getCause().getMessage());
            }
        }
        return response;
    }

    @DELETE
    public Response delete(T t) {
         Response response = new Response();
        try {
            response.setBody(manager.delete(t));
            response.setMessage("Entidad eliminada");
        } catch (Exception e) {
            response.setStatus(Status.ERROR);
            response.setMessage(e.getMessage());
            if (e.getCause() != null) {
                response.setMessage(e.getMessage() + e.getCause().getMessage());
            }
        }
        return response;
    }

}
