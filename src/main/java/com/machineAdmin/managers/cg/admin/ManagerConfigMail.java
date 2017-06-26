/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.admin;

import com.machineAdmin.daos.cg.admin.DaoConfigMail;
import com.machineAdmin.entities.cg.admin.ConfigMail;
import com.machineAdmin.managers.cg.ManagerMongoFacade;

/**
 *
 * @author ulises
 */
public class ManagerConfigMail extends ManagerMongoFacade<ConfigMail> {

    public ManagerConfigMail() {
        super(new DaoConfigMail());
    }
     
}
