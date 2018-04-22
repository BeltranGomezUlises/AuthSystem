/*
 * Copyright (C) 2018 Alonso - Alonso@kriblet.com
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
package com.auth.services.commons;

import com.auth.entities.commons.IEntity;
import com.auth.managers.commons.ManagerFacade;
import com.auth.managers.commons.ManagerSQL;
import com.auth.managers.exceptions.TokenExpiradoException;
import com.auth.managers.exceptions.TokenInvalidoException;
import com.auth.models.ModelCantidad;
import com.auth.utils.UtilsJWT;
import java.util.List;
import javax.ws.rs.Consumes;
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
 * @author Alonso - Alonso@kriblet.com
 * @param <T> entidad a manejar por esta clase servicio
 * @param <K> tipo de dato de llave primaria de la entidad a menejar por esta clase servicio
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceFaceLCRU<T extends IEntity<K>, K> {

    protected ManagerSQL<T, K> manager;

    public ServiceFaceLCRU(ManagerSQL<T, K> manager) {
        this.manager = manager;
    }

    public final ManagerFacade<T, K> getManager() {
        return manager;
    }

    /**
     * proporciona el listado de las entidades de esta clase servicio
     *
     * @param token token de sesion
     * @return reponse, con su campo data asignado con una lista de las entidades de esta clase servicio
     * @throws com.auth.managers.exceptions.TokenInvalidoException si el token proporsionado no es valido
     * @throws com.auth.managers.exceptions.TokenExpiradoException si el token proporsionado ya caducó
     */
    @GET
    public List<T> listar(@HeaderParam("Authorization") String token) throws TokenInvalidoException, TokenExpiradoException, Exception {
        UtilsJWT.validateSessionToken(token);
        return manager.findAll();
    }

    /**
     * obtiene una entidad en particular por su identificador de esta clase servicio
     *
     * @param token token de sesion
     * @param id identificador de la entidad buscada
     * @throws com.auth.managers.exceptions.TokenInvalidoException si el token proporsionado no es valido
     * @throws com.auth.managers.exceptions.TokenExpiradoException si el token proporsionado ya caducó
     * @return response, con su campo data asignado con la entidad buscada
     */
    @GET
    @Path("/{id}")
    public T detalle(@HeaderParam("Authorization") String token, @PathParam("id") K id) throws TokenInvalidoException, TokenExpiradoException, Exception {
        UtilsJWT.validateSessionToken(token);
        return manager.findOne(id);
    }

    /**
     * persiste la entidad de esta clase servicio en base de datos
     *
     * @param token token de sesion
     * @param t entidad a persistir en base de datos
     * @throws com.auth.managers.exceptions.TokenInvalidoException si el token proporsionado no es valido
     * @throws com.auth.managers.exceptions.TokenExpiradoException si el token proporsionado ya caducó
     * @return response con el estatus y el mensaje
     */
    @POST
    public T alta(@HeaderParam("Authorization") String token, T t) throws TokenInvalidoException, TokenExpiradoException, Exception {
        UtilsJWT.validateSessionToken(token);
        manager.persist(t);
        return t;
    }

    /**
     * actualiza la entidad proporsionada a su equivalente en base de datos, tomando como referencia su identificador
     *
     * @param token token de sesion
     * @param t entidad con los datos actualizados
     * @throws com.auth.managers.exceptions.TokenInvalidoException si el token proporsionado no es valido
     * @throws com.auth.managers.exceptions.TokenExpiradoException si el token proporsionado ya caducó
     * @return Response, en data asignado con la entidad que se actualizó
     */
    @PUT
    public T modificar(@HeaderParam("Authorization") String token, T t) throws TokenInvalidoException, TokenExpiradoException, Exception {
        UtilsJWT.validateSessionToken(token);
        manager.update(t);
        return t;
    }

    /**
     * cuenta la cantidad de entidades existentes
     *
     * @return numero con la cantidad existente de entidades actualmente
     * @throws Exception si existe un error de I/O con la base de datos
     */
    @GET
    @Path("/count")
    public ModelCantidad contar() throws Exception {
        return new ModelCantidad(manager.count());
    }

    /**
     * proporciona el listado de entidades comprendidas desde la posicion {from} hasta la posicion {to}
     *
     * @param from posicion inicial del rango a consultar
     * @param to posicion final del rango a consulta
     * @return lista de entidades dentro del rango solicitado
     */
    @GET
    @Path("/{from}/{to}")
    public List<T> listarRango(@PathParam("from") final Integer from, @PathParam("to") final Integer to) {
        return manager.findRange(from, to);
    }

    /**
     * consulta en la entidad de este modulo, los atributos solicitados con un rango de posiciones
     *
     * @param from índice inferior del rango
     * @param to índice superior del rango
     * @param select cadena con los nombres de los atributos concatenados por un '+', ejemplo: nombre+correo+direccion
     * @return lista de arreglos de objetos con los atributos solicitados
     */
    @GET
    @Path("/select/{from}/{to}/{select}")
    public List select(@PathParam("from") Integer from, @PathParam("t") Integer to, @PathParam("select") String select) {
        return manager.select(from, to, select.split("\\+"));
    }

    /**
     * consulta en la entidad de este modulo, los atributos solicitados
     *
     * @param select cadena con los nombres de los atributos concatenados por un '+', ejemplo: nombre+correo+direccion
     * @return lista de arreglos con los objetos de los atributos solicitados
     */
    @GET
    @Path("/select/{select}")
    public List select(@PathParam("select") String select) {
        return manager.select(select.split("\\+"));
    }

}
