/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services;

import com.machineAdmin.entities.business.Usuario;
import com.machineAdmin.managers.ManagerUsuario;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.models.cg.enums.Status;
import com.machineAdmin.models.cg.enums.responses.Response;
import com.machineAdmin.utils.UtilsSecurity;
import com.machineAdmin.utils.UtilsJWT;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
public class ServiceLogin {
   
    @GET
    @Path("/{usuario}/{contra}")
    public Response login(@PathParam("usuario") String usuario, @PathParam("contra") String contra) {
        Response r = new Response();        
        ManagerUsuario managerUsuario = new ManagerUsuario();                
        try {
            r.setData(managerUsuario.Login(new Usuario(usuario, contra)));           
            r.setMetaData(UtilsJWT.generateToken());
        } catch (UsuarioInexistenteException e) {
            r.setStatus(Status.WARNING);
            r.setMessage("Usuario y/o contraseña incorrecto");
            r.setDevMessage("imposible inicio de sesión, por: " + e.getMessage());
        } catch (Exception ex){
            r.setStatus(Status.ERROR);            
            r.setDevMessage("imposible inicio de sesión, por: " + ex.getMessage());
        }       
        return r;
    }
    
    @GET
    @Path("/pass/{test}")
    public Response ecnript(@PathParam("test") String text){
        Response r = new Response();
        r.setMessage(UtilsSecurity.Encriptar(text));
        try {
            r.setDevMessage(UtilsSecurity.Desencriptar(r.getMeta().getMessage()));
        } catch (Exception ex) {
            Logger.getLogger(ServiceLogin.class.getName()).log(Level.SEVERE, null, ex);
            r.setDevMessage("imposible desencriptar");
        }
        
        return r;
    }
       
}
