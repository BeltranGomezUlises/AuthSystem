/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.daos.cg.admin.mongo;

import com.machineAdmin.daos.cg.commons.DaoMongoFacade;
import com.machineAdmin.entities.cg.admin.mongo.ConfigMail;
import com.machineAdmin.utils.UtilsDB;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class DaoConfigMail extends DaoMongoFacade<ConfigMail>{

    public DaoConfigMail() {
        super(UtilsDB.getCGCollection("config.correos"), ConfigMail.class);
    }
            
}
