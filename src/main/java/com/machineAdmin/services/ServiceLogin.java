/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services;

import com.machineAdmin.entities.business.Usuario;
import com.machineAdmin.managers.ManagerUsuario;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.models.cg.LoginContent;
import com.machineAdmin.models.cg.enums.Status;
import com.machineAdmin.models.cg.responses.Response;
import com.machineAdmin.utils.UtilsSecurity;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsJson;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
   
    @POST
    @Path("/login")
    public Response login(LoginContent content) {
        Response r = new Response();        
        ManagerUsuario managerUsuario = new ManagerUsuario();                        
        try {            
            Usuario usuarioAutenticando = UtilsJson.jsonDeserialize(UtilsSecurity.decryptBase64ByPrivateKey(content.getContent()), Usuario.class);            
            Usuario usuarioLogeado = managerUsuario.Login(usuarioAutenticando);
            r.setData(usuarioLogeado);           
            r.setMetaData(UtilsJWT.generateToken(usuarioLogeado));
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
    @Path("/publicKey")
    public Response publicKey(){
        Response r = new Response();        
        r.setData(UtilsSecurity.getPublicKey());        
        r.setDevMessage("llave public de cifrado RSA Base64");        
        return r;
    }
    
    //<editor-fold defaultstate="collapsed" desc="TEST -> cifrado y decifrado por UtilsSecurity">
    @GET
    @Path("/test")
    public void test(){
        UtilsSecurity.testDeCrifradoYDecifrado();
    }
    //</editor-fold>     
       
}
