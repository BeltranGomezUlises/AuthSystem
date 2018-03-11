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

import com.auth.managers.exceptions.TokenExpiradoException;
import com.auth.managers.exceptions.TokenInvalidoException;
import com.auth.models.Respuesta;
import com.auth.models.enums.Status;
import com.auth.utils.UtilsJWT;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * servicios para validacion de tokens
 *
 * @author Ulises Beltrán Gómez - beltrangomezulises@gmail.com
 */
@Path("/tokens")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Tokens {

    /**
     * retorna una respuesta positiva o negativa segun la valides del token proporcionado
     *
     * @param token token a validar
     * @return repuesta con mensaje de valido o no valido segun el token proporcionado
     */
    @GET
    @Path("/{token}")
    public Respuesta<Boolean> tokenValido(@PathParam("token") final String token) {
        Respuesta<Boolean> res;
        try {
            UtilsJWT.validateSessionToken(token);
            res = new Respuesta<>(Status.OK, "Token válido", Boolean.TRUE);

        } catch (TokenExpiradoException | TokenInvalidoException e) {
            res = new Respuesta<>(Status.WARNING, "Token no válido", false);
        }
        return res;
    }

}
