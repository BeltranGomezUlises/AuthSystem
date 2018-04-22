/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auth.services;

import com.auth.utils.UtilsDate.DateClass;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * servicios comunes para toma de referencias
 *
 * @author Alonso --- alonso@kriblet.com
 */
@Path("/commons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Commons {

    /**
     * sirve para obtener la hora actual del servidor
     *
     * @return modelo dateclass que contiene la hora actual del servidor
     */
    @GET
    @Path("/serverDate")
    public DateClass serverDate() {
        return new DateClass();
    }

}
