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

import com.machineAdmin.entities.cg.commons.EntitySQLCatalog;
import com.machineAdmin.managers.cg.commons.ManagerFacade;
import com.machineAdmin.managers.cg.commons.ManagerSQLCatalog;
import com.machineAdmin.managers.cg.exceptions.*;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.utils.UtilsBitacora;
import com.machineAdmin.utils.UtilsPermissions;
import static com.machineAdmin.utils.UtilsService.*;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.toList;
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
 * servicios LCRUD para entidades SQL que tienen profundidad de acceso
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T>
 * @param <K>
 */
public class ServiceFacadeCatalogSQL<T extends EntitySQLCatalog, K> extends ServiceBitacoraFacade<T, K> {

    private final ManagerSQLCatalog<T, K> manager;

    public ServiceFacadeCatalogSQL(ManagerSQLCatalog<T, K> manager) {
        this.manager = manager;
    }

    @Override
    public final ManagerFacade<T, K> getManager() {
        return manager;
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
    //@com.webcohesion.enunciate.metadata.Ignore
    @GET
    public Response listar(@Context HttpServletRequest request, @HeaderParam("Authorization") String token) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            this.manager.setProfundidad(UtilsPermissions.obtenerProfundidad(token, UtilsPermissions.accionActual()));
            setOkResponse(response, manager.findAll(), "Entidades encontradas");

            //<editor-fold defaultstate="collapsed" desc="BITACORIZAR">
            try {
                UtilsBitacora.ModeloBitacora bitacora = new UtilsBitacora.ModeloBitacora(manager.getUsuario(), new Date(), "Listar", request);
                UtilsBitacora.bitacorizar(manager.nombreColeccionParaRegistros(), bitacora);
            } catch (UnsupportedOperationException unsupportedOperationException) {
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Auditar">
            //UtilsAuditoria.ModeloAuditoria auditoria = new UtilsAuditoria.ModeloAuditoria(manager.getUsuario(), "Listar", null);
            //UtilsAuditoria.auditar(manager.nombreColeccionParaRegistros(), auditoria);
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
//    @com.webcohesion.enunciate.metadata.Ignore
    @GET
    @Path("/{id}")
    public Response detalle(@Context HttpServletRequest request, @HeaderParam("Authorization") String token, @PathParam("id") String id) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            this.manager.setProfundidad(UtilsPermissions.obtenerProfundidad(token, UtilsPermissions.accionActual()));
            setOkResponse(response, manager.findOne(manager.stringToKey(id)), "Entidad encontrada");

            //<editor-fold defaultstate="collapsed" desc="BITACORIZAR">
            try {
                UtilsBitacora.ModeloBitacora bitacora = new UtilsBitacora.ModeloBitacora(manager.getUsuario(), new Date(), "Detalle", request);
                UtilsBitacora.bitacorizar(manager.nombreColeccionParaRegistros(), bitacora);
            } catch (UnsupportedOperationException unsupportedOperationException) {
            }
            //</editor-fold>

        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (AccesoDenegadoException e) {
            setAccesDeniedResponse(response, e);
        } catch (ParametroInvalidoException e) {
            setParametroInvalidoResponse(response, e);
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
    //@com.webcohesion.enunciate.metadata.Ignore
    @POST
    public Response alta(@Context HttpServletRequest request, @HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            this.manager.setProfundidad(UtilsPermissions.obtenerProfundidad(token, UtilsPermissions.accionActual()));
            setOkResponse(response, manager.persist(t), "Entidad persistida");
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
    //@com.webcohesion.enunciate.metadata.Ignore
    @PUT
    public Response modificar(@Context HttpServletRequest request, @HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            this.manager.setProfundidad(UtilsPermissions.obtenerProfundidad(token, UtilsPermissions.accionActual()));
            manager.update(t);
            setOkResponse(response, t, "Entidad actualizada");
            //<editor-fold defaultstate="collapsed" desc="BITACORIZAR">
            try {
                UtilsBitacora.ModeloBitacora bitacora = new UtilsBitacora.ModeloBitacora(manager.getUsuario(), new Date(), "Modificar", request);
                UtilsBitacora.bitacorizar(manager.nombreColeccionParaRegistros(), bitacora);
            } catch (UnsupportedOperationException unsupportedOperationException) {
            }
            //</editor-fold>    

        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (AccesoDenegadoException e) {
            setAccesDeniedResponse(response, e);
        } catch (ParametroInvalidoException e) {
            setParametroInvalidoResponse(response, e);
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
        return response;
    }

    /**
     * actualiza las entidades proporsionadas a su equivalente en base de datos,
     * tomando como referencia su identificador
     *
     * @param request contexto de peticion necesario para obtener datos como ip,
     * sistema operativo y navegador del cliente
     * @param token token de sesion
     * @param ts entidades con los datos actualizados
     * @return Response, en data asignado con la entidad que se actualizó
     */
    @Path("/varios")
    @PUT
    public Response modificarVarios(@Context HttpServletRequest request, @HeaderParam("Authorization") String token, List<T> ts) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            this.manager.setProfundidad(UtilsPermissions.obtenerProfundidad(token, UtilsPermissions.accionActual()));
            manager.updateAll(ts);
            setOkResponse(response, ts, "Entidad actualizada");
            //<editor-fold defaultstate="collapsed" desc="BITACORIZAR">
            try {
                UtilsBitacora.ModeloBitacora bitacora = new UtilsBitacora.ModeloBitacora(manager.getUsuario(), new Date(), "Modificar", request);
                UtilsBitacora.bitacorizar(manager.nombreColeccionParaRegistros(), bitacora);
            } catch (UnsupportedOperationException unsupportedOperationException) {
            }
            //</editor-fold>    

        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (AccesoDenegadoException e) {
            setAccesDeniedResponse(response, e);
        } catch(ElementosSinAccesoException e){
            setElementosSinAccesoResponse(response, e);
        }catch (Exception e) {
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
//    @com.webcohesion.enunciate.metadata.Ignore
    @DELETE
    public Response eliminar(@Context HttpServletRequest request, @HeaderParam("Authorization") String token, T t) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            this.manager.setProfundidad(UtilsPermissions.obtenerProfundidad(token, UtilsPermissions.accionActual()));
            manager.delete((K) t.getId());
            setOkResponse(response, "Entidad eliminada");

            //<editor-fold defaultstate="collapsed" desc="BITACORIZAR">
            try {
                UtilsBitacora.ModeloBitacora bitacora = new UtilsBitacora.ModeloBitacora(manager.getUsuario(), new Date(), "Eliminar", request);
                UtilsBitacora.bitacorizar(manager.nombreColeccionParaRegistros(), bitacora);
            } catch (UnsupportedOperationException unsupportedOperationException) {
            }
            //</editor-fold>

        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (AccesoDenegadoException e) {
            setAccesDeniedResponse(response, e);
        } catch (ParametroInvalidoException e) {
            setParametroInvalidoResponse(response, e);
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
        return response;
    }

    /**
     * eliminar las entidades proporsionadas
     *
     * @param request contexto de peticion necesario para obtener datos como ip,
     * sistema operativo y navegador del cliente
     * @param token token de sesion
     * @param ts entidades proporsionadas para eliminar
     * @return
     */
    @Path("/varios")
    @DELETE
    public Response eliminarVarios(@Context HttpServletRequest request, @HeaderParam("Authorization") String token, List<T> ts) {
        Response response = new Response();
        try {
            this.manager.setToken(token);
            this.manager.setProfundidad(UtilsPermissions.obtenerProfundidad(token, UtilsPermissions.accionActual()));
            manager.deleteAll((List<K>) ts.stream().map( t -> t.getId()).collect(toList()));
            setOkResponse(response, "Entidad eliminada");
            //<editor-fold defaultstate="collapsed" desc="BITACORIZAR">
            try {
                UtilsBitacora.ModeloBitacora bitacora = new UtilsBitacora.ModeloBitacora(manager.getUsuario(), new Date(), "Eliminar", request);
                UtilsBitacora.bitacorizar(manager.nombreColeccionParaRegistros(), bitacora);
            } catch (UnsupportedOperationException unsupportedOperationException) {
            }
            //</editor-fold>

        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            setInvalidTokenResponse(response);
        } catch (AccesoDenegadoException e) {
            setAccesDeniedResponse(response, e);
        } catch(ElementosSinAccesoException e){
            setElementosSinAccesoResponse(response, e);
        } catch (Exception e) {
            setErrorResponse(response, e);
        }
        return response;
    }

}
