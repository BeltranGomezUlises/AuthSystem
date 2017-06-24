/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.daos;

import com.machineAdmin.daos.cg.DaoFacade;
import com.machineAdmin.entities.business.Usuario;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class DaoUsuario extends DaoFacade<Usuario>{
    
    public DaoUsuario() {
        super("usuarios", Usuario.class);
    }
        
}
