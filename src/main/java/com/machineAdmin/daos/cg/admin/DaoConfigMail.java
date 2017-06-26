/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.daos.cg.admin;

import com.machineAdmin.daos.cg.DaoMongoFacade;
import com.machineAdmin.entities.cg.admin.ConfigMail;

/**
 *
 * @author ulises
 */
public class DaoConfigMail extends DaoMongoFacade<ConfigMail>{

    public DaoConfigMail() {
        super("config.mails", ConfigMail.class);
    }
            
}
