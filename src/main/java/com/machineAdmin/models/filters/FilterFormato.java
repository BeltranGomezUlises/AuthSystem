/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.models.filters;

import com.machineAdmin.models.enums.Pagado;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class FilterFormato {

    private String encargado;
    private String sucursal;
    private String turno;
    private Pagado pagado;

    public FilterFormato() {
    }  

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Pagado getPagado() {
        return pagado;
    }

    public void setPagado(Pagado pagado) {
        this.pagado = pagado;
    }     

    @Override
    public String toString() {
        return "FilterFormato{" + "encargado=" + encargado + ", sucursal=" + sucursal + ", turno=" + turno + ", pagado=" + pagado + '}';
    }        

}
