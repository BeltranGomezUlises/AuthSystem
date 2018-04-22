/*
 * Copyright (C) 2018 Alonso - Alonso@kriblet.com
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
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Alonso - Alonso@kriblet.com
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelPermisoValido {

    private boolean valido;
    private Profundidad profundidad;

    public ModelPermisoValido() {
    }

    public ModelPermisoValido(Profundidad profundidad) {
        this.valido = true;
        this.profundidad = profundidad;
    }

    public ModelPermisoValido(boolean valido, Profundidad profundidad) {
        this.valido = valido;
        this.profundidad = profundidad;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public Profundidad getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(Profundidad profundidad) {
        this.profundidad = profundidad;
    }

}
