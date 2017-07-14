/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.services.cg.admin;

import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.managers.cg.admin.ManagerUser;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.enums.Status;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.ServiceFacade;
import static com.machineAdmin.services.cg.ServiceFacade.setCauseMessage;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsSecurity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Path;
import org.mongojack.DBQuery;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/users")
public class ServiceUsers extends ServiceFacade<User>{
    
    public ServiceUsers() {
        super(new ManagerUser());
    }

    @Override
    public Response delete(String token, User t) {
    //delete solo deshabilita los usuarios
        return super.delete(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response put(String token, User t) {
        return super.put(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response post(String token, User t) {
        t.setPass(UtilsSecurity.cifrarMD5(t.getPass()));
        return super.post(token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response get(String token, String id) {
        return super.get(token, id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override    
    public Response get(String token) {                                        
        Response response = new Response();
        try {                        
            UtilsJWT.validateSessionToken(token);
            ManagerUser manager = new ManagerUser();
            response.setData(manager.findAll("user", "mail", "phone", "_id"));
            response.setMessage("Entidad encontrada");            
        } catch (TokenExpiradoException | TokenInvalidoException ex) {
            response.setMessage("Token de session expirado");
            setCauseMessage(response, ex);
            response.setStatus(Status.WARNING);
        }
        return response;        
    }
             
}
