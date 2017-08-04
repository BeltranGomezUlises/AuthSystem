/*
 * Copyright (C) 2017 Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.machineAdmin.services.cg.commons;

import com.machineAdmin.entities.cg.commons.IEntity;
import com.machineAdmin.managers.cg.commons.ManagerFacade;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.enums.Status;
import com.machineAdmin.models.cg.responsesCG.Response;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T>
 * @param <K>
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceFacadeBase<T extends IEntity, K> {

    private ManagerFacade<T, K> manager;

    public ServiceFacadeBase(ManagerFacade<T, K> manager) {
        this.manager = manager;
    }

    public final ManagerFacade<T, K> getManager() {
        return manager;
    }

    public final void setManager(ManagerFacade<T, K> manager) {
        this.manager = manager;
    }

    /**
     * proporciona el listado de las entidades de esta clase servicio
     *
     * @param request
     * @param token token de sesion
     * @return reponse, con su campo data asignado con una lista de las
     * entidades de esta clase servicio
     */
    @GET
    public Response listar(@HeaderParam("Authorization") String token) {                        
        Response response = new Response();
        try {
            this.manager.setToken(token);      
            setOkResponse(response, manager.findAll(), "Entidades encontradas");           
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(response);
        } catch (Exception ex) {
            setErrorResponse(response, ex);
        }
        return response;
    }

    /**
     * obtiene una entidad en particular por su identificador de esta clase
     * servicio
     *
     * @param token token de sesion
     * @param id identificador de la entidad buscada
     * @return response, con su campo data asignado con la entidad buscada
     */
    @GET
    @Path("/{id}")
    public Response obtener(@HeaderParam("Authorization") String token, @PathParam("id") String id) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            response.setData(manager.findOne(manager.stringToKey(id)));
            response.setMessage("Entidad encontrada");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
        return response;
    }

    /**
     * persiste la entidad de esta clase servicio en base de datos
     *
     * @param token token de sesion
     * @param t entidad a persistir en base de datos
     * @return response con el estatus y el mensaje
     */
    @POST
    public Response alta(@HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            response.setData(manager.persist(t));
            response.setMessage("Entidad persistida");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
        return response;
    }

    /**
     * actualiza la entidad proporsionada a su equivalente en base de datos,
     * tomando como referencia su identificador
     *
     * @param token token de sesion
     * @param t entidad con los datos actualizados
     * @return Response, en data asignado con la entidad que se actualizó
     */
    @PUT
    public Response modificar(@HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
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

    /**
     * eliminar la entidad proporsionada
     *
     * @param token token de sesion
     * @param t entidad proporsionada
     * @return
     */
    @DELETE
    public Response eliminar(@HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            manager.delete((K) t.getId());
            response.setMessage("Entidad eliminada");
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
        return response;
    }

    /**
     * asigna al modelo response, la pila de causas del error de la exception e
     *
     * @param response response a asignar la pila de causas
     * @param e la exception lanzada
     */
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

    /**
     * asigna a response el estatus y el mensaje de un token invalido, se
     * utiliza cuando se lanzá una exception de tipo TokenInvalidoException
     *
     * @param response res
     */
    public static final void setInvalidTokenResponse(Response response) {
        response.setStatus(Status.WARNING);
        response.setDevMessage("Token inválido");
    }

    /**
     * asignar a response el estatus WARNING y los mensajes proporcionados
     *
     * @param res modelo response generico a asignar valores
     * @param message mensaje para el usuario
     * @param devMessage mensaje para el desarrollador
     */
    public static final void setWarningResponse(Response res, String message, String devMessage) {
        res.setStatus(Status.WARNING);
        res.setMessage(message);
        res.setDevMessage(devMessage);
    }

    /**
     * asignar a response el estatus WARNING y el mensaje proporsionado
     *
     * @param res modelo response generico a asignar valores
     * @param devMessage mensaje para el desarrollador
     */
    public static final void setWarningResponse(Response res, String devMessage) {
        res.setStatus(Status.WARNING);
        res.setDevMessage(devMessage);
    }

    /**
     * asignar a response el estatus ERROR y el mensaje proporsionado para el
     * usuario
     *
     * @param res modelo response generico a asignar valores
     * @param err exception lanzada
     * @param message mensaje para el usuario
     */
    public static final void setErrorResponse(Response res, Throwable err, String message) {
        res.setStatus(Status.ERROR);
        setCauseMessage(res, err);
        res.setMessage(message);
    }

    /**
     * asignar a response el estatus ERROR asi como un mensaje generico
     *
     * @param res modelo response generico a asignar valores
     * @param err exception lanzada
     */
    public static final void setErrorResponse(Response res, Throwable err) {
        res.setStatus(Status.ERROR);
        res.setMessage("Existió un error de programación, consultar con el administrador del sistema");
        setCauseMessage(res, err);

    }

    /**
     * asigna a response el estatus OK y los mensajes proporcionados
     *
     * @param res modelo response generico
     * @param message
     * @param devMessage
     */
    public static final void setOkResponse(Response res, String message, String devMessage) {
        res.setStatus(Status.OK);
        res.setMessage(message);
        res.setDevMessage(devMessage);
    }

    /**
     * asigna a response el estatus OK mas los mensajes proporcionados, ademas
     * de poner en metadata el objeto data proporsionado
     *
     * @param res
     * @param data
     * @param message
     * @param devMessage
     */
    public static final void setOkResponse(Response res, Object data, String message, String devMessage) {
        res.setStatus(Status.OK);
        res.setData(data);
        res.setMessage(message);
        res.setDevMessage(devMessage);
    }

    /**
     * asignar a response el estatus OK, el metadata y un mensaje para el
     * desarrollador
     *
     * @param res
     * @param data
     * @param devMessage
     */
    public static final void setOkResponse(Response res, Object data, String devMessage) {
        res.setStatus(Status.OK);
        res.setData(data);
        res.setDevMessage(devMessage);
    }

    /**
     * asigna solo le estatus OK a response y le añade el mensaje para el
     * desarrollador
     *
     * @param res
     * @param devMessage
     */
    public static final void setOkResponse(Response res, String devMessage) {
        res.setStatus(Status.OK);
        res.setDevMessage(devMessage);
    }
    
}

