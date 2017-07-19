package com.machineAdmin.services.cg.commons;

import com.machineAdmin.managers.cg.commons.ManagerFacade;
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
 * @param <T> is a Entity that workw with de manager
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceFacade<T> {
    
    ManagerFacade<T> manager;
    
    public ServiceFacade(ManagerFacade<T> manager) {
        this.manager = manager;
    }
    
    @GET
    public Response listar(@HeaderParam("Authorization") String token) {
        Response response = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            setOkResponse(response, manager.findAll(), "Entidades encontradas");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(response);
        } catch (Exception ex) {
            setErrorResponse(response, ex);
        }
        return response;
    }
    
    @GET
    @Path("/{id}")
    public Response obtener(@HeaderParam("Authorization") String token, @PathParam("id") String id) {
        Response response = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            response.setData(manager.findOne(id));
            response.setMessage("Entidad encontrada");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
        return response;
    }
    
    @POST
    public Response alta(@HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            response.setData(manager.persist(t));
            response.setMessage("Entidad persistida");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
        return response;
    }
    
    @PUT
    public Response modificar(@HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            manager.update(t);
            response.setData(t);
            response.setMessage("Entidad actualizada");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
        return response;
    }
    
    @DELETE
    public Response eliminar(@HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            UtilsJWT.validateSessionToken(token);
            manager.delete(t);
            response.setData(t);
            response.setMessage("Entidad eliminada");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
        return response;
    }
    
    public static final void setCauseMessage(Response response, Throwable e) {
        String anterior = response.getMeta().getDevMessage();
        if (anterior == null) {
            response.setDevMessage("CAUSE: " + e.getMessage());
        } else {
            response.setDevMessage(response.getMeta().getDevMessage() + " CAUSE: " + e.getMessage());
        }
        if (e.getCause() != null) {
            setCauseMessage(response, e.getCause());
        }
    }
    
    public static final void setInvalidTokenResponse(Response response) {
        response.setStatus(Status.WARNING);
        response.setDevMessage("Token inválido");
    }
    
    public static final void setWarningResponse(Response res, String message, String devMessage) {
        res.setStatus(Status.WARNING);
        res.setMessage(message);
        res.setDevMessage(devMessage);
    }
    
    public static final void setWarningResponse(Response res, String devMessage) {
        res.setStatus(Status.WARNING);
        res.setDevMessage(devMessage);
    }
    
    public static final void setErrorResponse(Response res, Throwable err, String message) {
        res.setStatus(Status.ERROR);
        setCauseMessage(res, err);
        res.setMessage(message);
    }
    
    public static final void setErrorResponse(Response res, Throwable err) {
        res.setStatus(Status.ERROR);
        res.setMessage("Existió un error de programación, consultar con el administrador del sistema");
        setCauseMessage(res, err);
        
    }
    
    public static final void setOkResponse(Response res, String message, String devMessage) {
        res.setStatus(Status.OK);
        res.setMessage(message);
        res.setDevMessage(devMessage);
    }
    
    public static final void setOkResponse(Response res, Object data, String message, String devMessage) {
        res.setStatus(Status.OK);
        res.setData(data);
        res.setMessage(message);
        res.setDevMessage(devMessage);
    }
    
    public static final void setOkResponse(Response res, Object data, String devMessage) {
        res.setStatus(Status.OK);
        res.setData(data);
        res.setDevMessage(devMessage);
    }
    
    public static final void setOkResponse(Response res, String devMessage) {
        res.setStatus(Status.OK);
        res.setDevMessage(devMessage);
    }
}
