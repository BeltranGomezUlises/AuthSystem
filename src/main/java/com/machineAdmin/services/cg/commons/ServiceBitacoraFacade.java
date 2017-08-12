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
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.utils.UtilsBitacora;
import static com.machineAdmin.utils.UtilsService.setErrorResponse;
import static com.machineAdmin.utils.UtilsService.setInvalidTokenResponse;
import java.util.Date;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * clase de servicios para bitacoras de acciones
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> entidad a manejar por esta clase servicio
 * @param <K> tipo de dato de llave primaria de la entidad a menejar por esta clase servicio
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public abstract class ServiceBitacoraFacade<T extends IEntity, K> {
    
    public abstract ManagerFacade<T, K> getManager();
           
    /**
     * obtener todo los registros de bitacoras de esta entidad
     *
     * @param token token de sesion
     * @return en data, la lista de modelos de bitacora
     */
    @GET
    @Path("/bitacoras")
    public Response obtenerBitacoras(@HeaderParam("Authorization") String token) {
        Response res = new Response();
        try {
            getManager().setToken(token);
            res.setData(UtilsBitacora.bitacoras(getManager().nombreColeccionParaRegistros()));
            res.setDevMessage("Bitácoras de esta entidad");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch (Exception e) {
            setErrorResponse(res, e);
        }
        return res;
    }

    /**
     * obtener los registros de bitcoras de esta entidad dentro del rango de
     * fechas proporsionado
     *
     * @param token token de sesion
     * @param fechaInicial los milisegundos que representan la fecha inicial de
     * filtrado
     * @param fechaFinal los milisegundos que representan la fecha final del
     * filtrado
     * @return en data, la lista de modelos de bitacora que estan dentro del
     * rango de fechas propuesto
     */
    @GET
    @Path("/bitacoras/{fechaInicial}/{fechaFinal}")
    public Response obtenerBitacorasRangoFechas(@HeaderParam("Authorization") String token, @PathParam("fechaInicial") long fechaInicial, @PathParam("fechaFinal") long fechaFinal) {
        Response res = new Response();
        try {
            this.getManager().setToken(token);
            res.setData(UtilsBitacora.bitacorasEntre(getManager().nombreColeccionParaRegistros(), new Date(fechaInicial), new Date(fechaFinal)));
            res.setDevMessage("Bitácoras de esta entidad");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch (Exception e) {
            setErrorResponse(res, e);
        }
        return res;

    }

    /**
     * obtener los registros de bitcoras de esta entidad desde de la fecha
     * proporsionada
     *
     * @param token token de sesion
     * @param fechaInicial los milisegundos que representan la fecha inicial
     * desde la cual obtener la bitacora
     * @return en data, la lista de modelos de bitacora que estan a partir de la
     * fecha proporsionada
     */
    @GET
    @Path("/bitacoras/desde/{fechaInicial}")
    public Response obtenerBitacorasDesdeFecha(@HeaderParam("Authorization") String token, @PathParam("fechaInicial") long fechaInicial) {
        Response res = new Response();
        try {
            this.getManager().setToken(token);
            res.setData(UtilsBitacora.bitacorasDesde(getManager().nombreColeccionParaRegistros(), new Date(fechaInicial)));
            res.setDevMessage("Bitácoras de esta entidad");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch (Exception e) {
            setErrorResponse(res, e);
        }
        return res;
    }

    /**
     * obtener los registros de bitcoras de esta entidad hasta la fecha
     * proporsionada
     *
     * @param token token de sesion
     * @param fechaFinal los milisegundos que representan la fecha final hasta
     * la cual obtener la bitacora
     * @return en data, la lista de modelos de bitacora que estan hasta de la
     * fecha proporsionada
     */
    @GET
    @Path("/bitacoras/hasta/{fechaFinal}")
    public Response obtenerBitacorasHastaFecha(@HeaderParam("Authorization") String token, @PathParam("fechaFinal") long fechaFinal) {
        Response res = new Response();
        try {
            this.getManager().setToken(token);
            res.setData(UtilsBitacora.bitacorasHasta(getManager().nombreColeccionParaRegistros(), new Date(fechaFinal)));
            res.setDevMessage("Bitácoras de esta entidad");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch (Exception e) {
            setErrorResponse(res, e);
        }
        return res;
    }

    /**
     * obtiene por stacktrace el id de la accion actual en ejecucion
     *
     * @return id de accion actual en ejecucion
     */
    protected static String accionActual() {
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        String raiz = "com.machineAdmin.services.";
        return className.substring(raiz.length()) + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
    }
    
}
