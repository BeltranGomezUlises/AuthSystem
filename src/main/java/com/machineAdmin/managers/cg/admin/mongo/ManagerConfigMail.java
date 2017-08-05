/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.admin.mongo;

import com.machineAdmin.daos.cg.admin.mongo.DaoConfigMail;
import com.machineAdmin.entities.cg.admin.mongo.ConfigMail;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.commons.ManagerMongoFacade;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerConfigMail extends ManagerMongoFacade<ConfigMail> {

    public ManagerConfigMail(){
        super();
    }
    
    public ManagerConfigMail(String usuario) {
        super(usuario, new DaoConfigMail());
    }
    
    public ManagerConfigMail(Profundidad profundidad, String token) {
        super(new DaoConfigMail(), profundidad, token);
    }

    @Override
    public String nombreColeccionParaRegistros() {
        return "correos";
    }
        
}
