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

import com.machineAdmin.entities.cg.commons.EntityMongoCatalog;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.commons.ManagerFacade;
import com.machineAdmin.managers.cg.commons.ManagerMongoCatalog;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.utils.UtilsAuditoria;
import com.machineAdmin.utils.UtilsBitacora;
import com.machineAdmin.utils.UtilsPermissions;
import static com.machineAdmin.utils.UtilsService.setErrorResponse;
import static com.machineAdmin.utils.UtilsService.setInvalidTokenResponse;
import static com.machineAdmin.utils.UtilsService.setOkResponse;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

/**
 * servicios LCRUD para entidades mongo que tienen profundidad de acceso
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> entidad a manejar por esta clase de servicios
 * @param <Object>
 */
public class ServiceFacadeCatalogMongo<T extends EntityMongoCatalog, Object> extends ServiceBitacoraFacade<T, Object> {

    private final ManagerMongoCatalog<T> manager;

    public ServiceFacadeCatalogMongo(ManagerMongoCatalog<T> manager) {
        this.manager = manager;
    }

    /**
     * proporciona el listado de las entidades de esta clase servicio
     *
     * @param request contexto de peticion necesario para obtener datos como ip,
     * sistema operativo y navegador del cliente
     * @param token token de sesion
     * @return reponse, con su campo data asignado con una lista de las
     * entidades de esta clase servicio
     */
    @GET
    public Response listar(@Context HttpServletRequest request, @HeaderParam("Authorization") String token) {
        Response response = new Response();
        try {
            this.manager.setToken(token);           
            this.manager.setProfundidad(UtilsPermissions.obtenerProfundidad(token, accionActual()));                        
            setOkResponse(response, manager.findAll(), "Entidades encontradas");

            
            //<editor-fold defaultstate="collapsed" desc="BITACORIZAR">
            try {
                UtilsBitacora.ModeloBitacora bitacora = new UtilsBitacora.ModeloBitacora(manager.getUsuario(), new Date(), "Listar", request);
                UtilsBitacora.bitacorizar(manager.nombreColeccionParaRegistros(), bitacora);
            } catch (UnsupportedOperationException e) {
            }

            //</editor-fold>
            
            //<editor-fold defaultstate="collapsed" desc="Auditar">
            UtilsAuditoria.ModeloAuditoria auditoria = new UtilsAuditoria.ModeloAuditoria(manager.getUsuario(), "Listar", null);
            UtilsAuditoria.auditar(manager.nombreColeccionParaRegistros(), auditoria);
            //</editor-fold>

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
     * @param request contexto de peticion necesario para obtener datos como ip,
     * sistema operativo y navegador del cliente
     * @param token token de sesion
     * @param id identificador de la entidad buscada
     * @return response, con su campo data asignado con la entidad buscada
     */
    @GET
    @Path("/{id}")
    public Response detalle(@Context HttpServletRequest request, @HeaderParam("Authorization") String token, @PathParam("id") String id) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            response.setData(manager.findOne(manager.stringToKey(id)));
            response.setMessage("Entidad encontrada");

            //<editor-fold defaultstate="collapsed" desc="BITACORIZAR">
            try {
                UtilsBitacora.ModeloBitacora bitacora = new UtilsBitacora.ModeloBitacora(manager.getUsuario(), new Date(), "Detalle", request);
                UtilsBitacora.bitacorizar(manager.nombreColeccionParaRegistros(), bitacora);
            } catch (UnsupportedOperationException e) {
            }

            //</editor-fold>
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
     * @param request contexto de peticion necesario para obtener datos como ip,
     * sistema operativo y navegador del cliente
     * @param token token de sesion
     * @param t entidad a persistir en base de datos
     * @return response con el estatus y el mensaje
     */
    @POST
    public Response alta(@Context HttpServletRequest request, @HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            response.setData(manager.persist(t));
            response.setMessage("Entidad persistida");

            //<editor-fold defaultstate="collapsed" desc="BITACORIZAR">          
            try {
                UtilsBitacora.ModeloBitacora bitacora = new UtilsBitacora.ModeloBitacora(manager.getUsuario(), new Date(), "Alta", request);
                UtilsBitacora.bitacorizar(manager.nombreColeccionParaRegistros(), bitacora);
            } catch (UnsupportedOperationException unsupportedOperationException) {
            }
            //</editor-fold>

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
     * @param request contexto de peticion necesario para obtener datos como ip,
     * sistema operativo y navegador del cliente
     * @param token token de sesion
     * @param t entidad con los datos actualizados
     * @return Response, en data asignado con la entidad que se actualizó
     */
    @PUT
    public Response modificar(@Context HttpServletRequest request, @HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            manager.update(t);
            response.setData(t);
            response.setMessage("Entidad actualizada");

            //<editor-fold defaultstate="collapsed" desc="BITACORIZAR">
            try {
                UtilsBitacora.ModeloBitacora bitacora = new UtilsBitacora.ModeloBitacora(manager.getUsuario(), new Date(), "Modificar", request);
                UtilsBitacora.bitacorizar(manager.nombreColeccionParaRegistros(), bitacora);
            } catch (UnsupportedOperationException unsupportedOperationException) {
            }
            //</editor-fold>    

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
     * @param request contexto de peticion necesario para obtener datos como ip,
     * sistema operativo y navegador del cliente
     * @param token token de sesion
     * @param t entidad proporsionada
     * @return
     */
    @DELETE
    public Response eliminar(@Context HttpServletRequest request, @HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            manager.delete(t.getId());
            response.setMessage("Entidad eliminada");

            //<editor-fold defaultstate="collapsed" desc="BITACORIZAR">
            try {
                UtilsBitacora.ModeloBitacora bitacora = new UtilsBitacora.ModeloBitacora(manager.getUsuario(), new Date(), "Eliminar", request);
                UtilsBitacora.bitacorizar(manager.nombreColeccionParaRegistros(), bitacora);
            } catch (UnsupportedOperationException unsupportedOperationException) {
            }
            //</editor-fold>

        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
        return response;
    }

    @Override
    public final ManagerFacade<T, Object> getManager() {
        return (ManagerFacade<T, Object>) manager;
    }
    
}
