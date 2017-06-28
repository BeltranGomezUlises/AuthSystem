/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.admin;

import com.machineAdmin.managers.cg.ManagerMongoFacade;
import com.machineAdmin.daos.cg.admin.DaoUser;
import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import org.mongojack.DBQuery;
import org.mongojack.DBQuery.Query;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerUser extends ManagerMongoFacade<User> {
    
    public ManagerUser() {
        super(new DaoUser());
    }
    
    public User Login(User usuario) throws UsuarioInexistenteException{
        Query q = DBQuery
                .is("user", usuario.getUser())
                .is("pass", usuario.getPass());
        
        User loged = this.findOne(q);
        if (loged != null) {
            return loged;
        }else{
            throw new UsuarioInexistenteException("No se encontro un usuario con esa contraseña");
        }
    }
    
}
