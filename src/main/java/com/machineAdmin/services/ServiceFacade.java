package com.machineAdmin.services;

import com.machineAdmin.entities.Entity;
import com.machineAdmin.entities.admin.Usuario;
import com.machineAdmin.managers.ManagerFacade;
import com.machineAdmin.models.responses.Response;
import com.machineAdmin.models.enums.Status;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsJson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
    public Response get(@HeaderParam("Authorization") String token) {
        Response response = new Response();
        if (UtilsJWT.isTokenValid(token)) {
            try {
                response.setData(manager.findAll());
                response.setDevMessage("Entidades encontradas");
            } catch (Exception e) {
                response.setStatus(Status.ERROR);
                setCauseMessage(response, e);
            }
        } else {
            response.setDevMessage("Token inválido");
            response.setStatus(Status.WARNING);
        }
        return response;
    }

    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") String id) {
        Response response = new Response();
        if (UtilsJWT.isTokenValid(token)) {
            try {
                response.setData(manager.find(id));
                response.setMessage("Entidad encontrada");
            } catch (Exception e) {
                response.setStatus(Status.ERROR);
                setCauseMessage(response, e);
            }
        } else {
            response.setMessage("Token inválido");
            response.setStatus(Status.WARNING);
        }
        return response;
    }

    @POST
    public Response post(@HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        if (UtilsJWT.isTokenValid(token)) {
            try {
                response.setData(manager.persist(t));
                response.setMessage("Entidad persistida");
            } catch (Exception e) {
                response.setStatus(Status.ERROR);
                setCauseMessage(response, e);
            }
        } else {
            response.setMessage("Token inválido");
            response.setStatus(Status.WARNING);
        }
        return response;
    }

    @PUT
    public Response put(@HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        if (UtilsJWT.isTokenValid(token)) {
            try {
                response.setData(manager.update(t));
                response.setMessage("Entidad actualizada");
            } catch (Exception e) {
                response.setStatus(Status.ERROR);
                setCauseMessage(response, e);
            }
        } else {
            response.setMessage("Token inválido");
            response.setStatus(Status.WARNING);
        }
        return response;
    }

    @DELETE
    public Response delete(@HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        if (UtilsJWT.isTokenValid(token)) {
            try {
                response.setData(manager.delete(t));
                response.setMessage("Entidad eliminada");
            } catch (Exception e) {
                response.setStatus(Status.ERROR);
                setCauseMessage(response, e);
            }
        } else {
            response.setMessage("Token inválido");
            response.setStatus(Status.WARNING);
        }
        return response;
    }

    protected void setCauseMessage(Response response, Throwable e) {
        response.setDevMessage(e.getMessage());
        if (e.getCause() != null) {
            setCauseMessage(response, e.getCause());
        }
    }
    
}
