/*
 * Copyright (C) 2017 Alonso --- alonso@kriblet.com
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

import com.auth.entities.commons.IEntity;
import com.auth.managers.commons.ManagerFacade;
import com.auth.managers.commons.ManagerSQL;
import com.auth.managers.exceptions.TokenExpiradoException;
import com.auth.managers.exceptions.TokenInvalidoException;
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
 * clase de servicios generales LCRUD para entidades que no requiere profundidad de acceso
 *
 * @author Alonso --- alonso@kriblet.com
 * @param <T> entidad a manejar por esta clase servicio
 * @param <K> tipo de dato de llave primaria de la entidad a menejar por esta clase servicio
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceFacade<T extends IEntity<K>, K> {

    protected ManagerSQL<T, K> manager;

    public ServiceFacade(ManagerSQL<T, K> manager) {
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
     */
    @GET
    public List<T> listar(@HeaderParam("Authorization") String token) throws TokenInvalidoException, TokenExpiradoException, Exception {
        //this.manager.setToken(token);
        return manager.findAll();
    }

    /**
     * obtiene una entidad en particular por su identificador de esta clase servicio
     *
     * @param token token de sesion
     * @param id identificador de la entidad buscada
     * @return response, con su campo data asignado con la entidad buscada
     */
    @GET
    @Path("/{id}")
    public T detalle(@HeaderParam("Authorization") String token, @PathParam("id") K id) throws TokenInvalidoException, TokenExpiradoException, Exception {
        //this.manager.setToken(token);
        return manager.findOne(id);
    }

    /**
     * persiste la entidad de esta clase servicio en base de datos
     *
     * @param token token de sesion
     * @param t entidad a persistir en base de datos
     * @return response con el estatus y el mensaje
     */
    @POST
    public T alta(@HeaderParam("Authorization") String token, T t) throws Exception {
        //this.manager.setToken(token);
        manager.persist(t);
        return t;
    }

    /**
     * actualiza la entidad proporsionada a su equivalente en base de datos, tomando como referencia su identificador
     *
     * @param token token de sesion
     * @param t entidad con los datos actualizados
     * @return Response, en data asignado con la entidad que se actualizó
     */
    @PUT
    public T modificar(@HeaderParam("Authorization") String token, T t) throws TokenInvalidoException, TokenExpiradoException, Exception {
        //this.manager.setToken(token);
        manager.update(t);
        return t;
    }

    /**
     * eliminar la entidad proporsionada
     *
     * @param token token de sesion
     * @param t entidad proporsionada
     * @return
     */
    @DELETE
    public T eliminar(@HeaderParam("Authorization") String token, T t) throws TokenInvalidoException, TokenExpiradoException, Exception {
        //this.manager.setToken(token);
        manager.delete((K) t.getId());
        return t;
    }

}
