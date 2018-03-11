/*
 * Copyright (C) 2017 Alonso --- alonso@kriblet.com
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
package com.auth.models;

import com.auth.entities.commons.Profundidad;

/**
 * modelo contenedor de la accion y la profundidad del permiso
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class ModelPermisoAsignado {

    private String permisoId;
    private Profundidad profundidad;
    private Integer sucursalId;    

    public ModelPermisoAsignado() {
    }

    public ModelPermisoAsignado(String id, Profundidad profundidad, Integer sucursalId) {
        this.permisoId = id;
        this.profundidad = profundidad;
        this.sucursalId = sucursalId;
    }

    public Integer getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }

    public String getId() {
        return permisoId;
    }

    public void setId(String id) {
        this.permisoId = id;
    }

    public Profundidad getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(Profundidad profundidad) {
        this.profundidad = profundidad;
    }

}
