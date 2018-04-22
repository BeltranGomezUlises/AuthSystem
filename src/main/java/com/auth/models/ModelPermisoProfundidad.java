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

/**
 *
 * @author Alonso - Alonso@kriblet.com
 */
public class ModelPermisoProfundidad {

    private Profundidad profundidad;

    public ModelPermisoProfundidad(Profundidad profundidad) {
        this.profundidad = profundidad;
    }

    public ModelPermisoProfundidad() {
    }

    public Profundidad getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(Profundidad profundidad) {
        this.profundidad = profundidad;
    }

}
