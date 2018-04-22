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

import java.util.List;

/**
 * modelo para asignar permisos a un usuario
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class ModelAsignarPerfilesAlUsuario {

    private Integer userId;
    private List<ModelPerfilYHereda> perfiles;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<ModelPerfilYHereda> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<ModelPerfilYHereda> perfiles) {
        this.perfiles = perfiles;
    }

}
