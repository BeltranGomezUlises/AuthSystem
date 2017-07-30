/*
 * Copyright (C) 2017 Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.machineAdmin.entities.cg.admin.mongo;

import com.machineAdmin.entities.cg.commons.EntityMongo;
import java.util.Date;

/**
 * modelo de registro de bitacoras
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 *
 */
public class BitacoraAcceso extends EntityMongo {

    private Date fecha;
    private String usuario;

    public BitacoraAcceso(Date fecha, String usuario) {
        this.fecha = fecha;
        this.usuario = usuario;
    }

    public BitacoraAcceso(String usuario) {
        this.fecha = new Date();
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
