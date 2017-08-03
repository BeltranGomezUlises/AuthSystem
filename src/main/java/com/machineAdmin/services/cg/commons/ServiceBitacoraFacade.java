package com.machineAdmin.services.cg.commons;

import com.machineAdmin.entities.cg.commons.IEntity;
import com.machineAdmin.managers.cg.commons.ManagerFacade;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.responsesCG.Response;
import static com.machineAdmin.services.cg.commons.ServiceFacadeBase.*;
import java.util.Date;
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
 * @param <K> tipo de dato del identificador de la entidad manejada
 */
public class ServiceBitacoraFacade<T extends IEntity, K> extends ServiceFacadeBase<T, K>{

    private ManagerFacade<T, K> manager;

    public ServiceBitacoraFacade(ManagerFacade<T, K> manager) {
        super(manager);
    }    

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
            this.manager.setToken(token);
            res.setData(manager.bitacoras());
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
            this.manager.setToken(token);
            res.setData(manager.bitacorasEntre(new Date(fechaInicial), new Date(fechaFinal)));
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
            this.manager.setToken(token);
            res.setData(manager.bitacorasDesde(new Date(fechaInicial)));
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
            this.manager.setToken(token);
            res.setData(manager.bitacorasHasta(new Date(fechaFinal)));
            res.setDevMessage("Bitácoras de esta entidad");
        } catch (TokenExpiradoException | TokenInvalidoException e) {
            setInvalidTokenResponse(res);
        } catch (Exception e) {
            setErrorResponse(res, e);
        }
        return res;
    }

    
}
