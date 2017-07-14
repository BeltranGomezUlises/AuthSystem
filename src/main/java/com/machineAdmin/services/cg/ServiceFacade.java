package com.machineAdmin.services.cg;

import com.machineAdmin.managers.cg.ManagerFacade;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.models.cg.enums.Status;
import com.machineAdmin.utils.UtilsJWT;
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
 * @param <T> is a Entity that workw with de manager<T>
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceFacade<T> {

    ManagerFacade<T> manager;

    public ServiceFacade(ManagerFacade<T> manager) {
        this.manager = manager;
    }

    @GET
    public Response get(@HeaderParam("Authorization") String token) {
        Response response = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            response.setData(manager.findAll());
            response.setDevMessage("Entidades encontradas");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            response.setDevMessage("Token inválido");
            response.setStatus(Status.WARNING);
        } catch (Exception ex) {
            response.setStatus(Status.ERROR);
            setCauseMessage(response, ex);
        }
        return response;
    }

    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") String id) {
        Response response = new Response();

        try {
            UtilsJWT.validateSessionToken(token);
            response.setData(manager.findOne(id));
            response.setMessage("Entidad encontrada");

        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            response.setDevMessage("Token inválido");
            response.setStatus(Status.WARNING);
        } catch (Exception e) {
            response.setStatus(Status.ERROR);
            setCauseMessage(response, e);
        }
        return response;
    }

    @POST
    public Response post(@HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            response.setData(manager.persist(t));
            response.setMessage("Entidad persistida");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            response.setMessage("Token inválido");
            response.setDevMessage("Token inválido");
            response.setStatus(Status.WARNING);
        } catch (Exception e) {
            response.setStatus(Status.ERROR);
            setCauseMessage(response, e);
        }
        return response;
    }

    @PUT
    public Response put(@HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            manager.update(t);
            response.setData(t);
            response.setMessage("Entidad actualizada");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            response.setDevMessage("Token inválido");
            response.setStatus(Status.WARNING);
        } catch (Exception e) {
            response.setStatus(Status.ERROR);
            setCauseMessage(response, e);
        }
        return response;
    }

    @DELETE
    public Response delete(@HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            manager.delete(t);
            response.setData(t);
            response.setMessage("Entidad eliminada");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            response.setDevMessage("Token inválido");
            response.setStatus(Status.WARNING);
        } catch (Exception e) {
            response.setStatus(Status.ERROR);
            setCauseMessage(response, e);
        }
        return response;
    }

    public static final void setCauseMessage(Response response, Throwable e) {
        response.setDevMessage(response.getMeta().getDevMessage() + " CAUSE:" + e.getMessage());
        if (e.getCause() != null) {
            setCauseMessage(response, e.getCause());
        }
    }

}
