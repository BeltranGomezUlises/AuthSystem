/*
 * Copyright (C) 2018 Ulises Beltrán Gómez - beltrangomezulises@gmail.com
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
package com.auth.services;

import com.auth.daos.admin.DaoSucursal;
import com.auth.entities.admin.Sucursal;
import com.auth.managers.commons.ManagerFacade;
import com.auth.managers.commons.ManagerSQL;
import com.auth.managers.exceptions.TokenExpiradoException;
import com.auth.managers.exceptions.TokenInvalidoException;
import com.auth.utils.UtilsJWT;
import java.util.List;
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
 * LCRUD de sucursales
 *
 * @author Ulises Beltrán Gómez - beltrangomezulises@gmail.com
 */
@Path("/sucursales")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Sucursales {

    /**
     * proporciona el listado de las entidades de esta clase servicio
     *
     * @param token token de sesion
     * @return reponse, con su campo data asignado con una lista de las entidades de esta clase servicio
     * @throws com.auth.managers.exceptions.TokenInvalidoException si el token proporsionado no es valido
     * @throws com.auth.managers.exceptions.TokenExpiradoException si el token proporsionado ya caducó
     */
    @GET
    public List<Sucursal> listar(@HeaderParam("Authorization") String token) throws TokenInvalidoException, TokenExpiradoException, Exception {
        return new DaoSucursal().findAll();
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
    public Sucursal detalle(@HeaderParam("Authorization") String token, @PathParam("id") int id) throws TokenInvalidoException, TokenExpiradoException, Exception {
        UtilsJWT.validateSessionToken(token);
        return new DaoSucursal().findOne(id);
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
    public Sucursal alta(@HeaderParam("Authorization") String token, Sucursal t) throws TokenInvalidoException, TokenExpiradoException, Exception {
        UtilsJWT.validateSessionToken(token);
        new DaoSucursal().persist(t);
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
    public Sucursal modificar(@HeaderParam("Authorization") String token, Sucursal t) throws TokenInvalidoException, TokenExpiradoException, Exception {
        UtilsJWT.validateSessionToken(token);
        new DaoSucursal().update(t);
        return t;
    }

    /**
     * eliminar la entidad proporsionada
     *
     * @param token token de sesion
     * @param t entidad proporsionada
     * @throws com.auth.managers.exceptions.TokenInvalidoException si el token proporsionado no es valido
     * @throws com.auth.managers.exceptions.TokenExpiradoException si el token proporsionado ya caduc
     * @return
     */
    @DELETE
    public Sucursal eliminar(@HeaderParam("Authorization") String token, Sucursal t) throws TokenInvalidoException, TokenExpiradoException, Exception {
        UtilsJWT.validateSessionToken(token);
        new DaoSucursal().delete(t.getId());
        return t;
    }

}
