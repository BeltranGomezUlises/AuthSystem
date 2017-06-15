/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services;

import com.machineAdmin.entities.Usuario;
import com.machineAdmin.models.Response;
import com.machineAdmin.utils.JWTUtil;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceLogin{
        
    @GET
    @Path("/{usuario}/{contra}")
    public Response login(@PathParam("usuario") String usuario, @PathParam("contra") String contra){
        Response r = new Response();
        r.setBody(JWTUtil.generateToken());
        return r;
    }
    
    @GET
    @Path("/token")
    public Response login(@HeaderParam("Authorization") String token){
        Response r = new Response();
        if (JWTUtil.isTokenValid(token)) {
            r.setBody(JWTUtil.getBodyToken(token));
        }        
        return r;
    }     
    
}
