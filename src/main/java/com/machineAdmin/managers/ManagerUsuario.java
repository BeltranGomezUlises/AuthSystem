/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers;

import com.machineAdmin.managers.cg.ManagerMongoFacade;
import com.machineAdmin.daos.DaoUsuario;
import com.machineAdmin.entities.business.Usuario;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import org.mongojack.DBQuery;
import org.mongojack.DBQuery.Query;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerUsuario extends ManagerMongoFacade<Usuario> {
    
    public ManagerUsuario() {
        super(new DaoUsuario());
    }
    
    public Usuario Login(Usuario usuario) throws UsuarioInexistenteException{
        Query q = DBQuery.exists("_id");
        q.is("usuario", usuario.getUsuario());
        q.is("contraseña", usuario.getContraseña());
        Usuario loged= this.findOne(q);
        if (loged != null) {
            return loged;
        }else{
            throw new UsuarioInexistenteException("No se encontro un usuario con esa contraseña");
        }
    }
    
}
