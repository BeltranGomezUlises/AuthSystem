/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.daos;

import com.machineAdmin.entities.Formato;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class DaoFormato extends DaoFacade<Formato> {

    public DaoFormato() {
        super("formatos", Formato.class);
    }
    
}
