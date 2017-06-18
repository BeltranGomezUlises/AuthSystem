/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers;

import com.machineAdmin.daos.DaoFormato;
import com.machineAdmin.entities.business.Formato;
import com.machineAdmin.models.enums.Pagado;
import com.machineAdmin.models.filters.FilterFormato;
import java.util.List;
import org.mongojack.DBQuery;
import org.mongojack.DBQuery.Query;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerFormato extends ManagerFacade<Formato> {

    public ManagerFormato() {
        super(new DaoFormato());
    }

    public List<Formato> findAll(FilterFormato f) {
        Query query = DBQuery.exists("_id");
        if (f.getEncargado() != null) {
            query.all("encargado", f.getEncargado());
        }
        if (f.getSucursal() != null) {
            query.all("sucursal", f.getSucursal());
        }
        if (f.getTurno() != null) {
            query.all("turno", f.getTurno());
        }
        if (f.getPagado() != Pagado.TODOS) {
            if (f.getPagado() == Pagado.PAGADO) {
                query.all("pagado", true);
            } else {
                query.all("pagado", false);
            }
        }        
        return dao.findAll(query);
    }
}
