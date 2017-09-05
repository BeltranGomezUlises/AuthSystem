/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.admin.mongo;

import com.machineAdmin.daos.cg.admin.mongo.DaoConfigMail;
import com.machineAdmin.entities.cg.admin.mongo.ConfigMail;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.commons.ManagerMongoCatalog;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerConfigMail extends ManagerMongoCatalog<ConfigMail> {

    public ManagerConfigMail(){
        super(new DaoConfigMail());
    }
        
    public ManagerConfigMail(String token, Profundidad profundidad) throws TokenInvalidoException, TokenExpiradoException {
        super(new DaoConfigMail(), token, profundidad);
    }

    @Override
    public String nombreColeccionParaRegistros() {
        return "correos";
    }
        
}
