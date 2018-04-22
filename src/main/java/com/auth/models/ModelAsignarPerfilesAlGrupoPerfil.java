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
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class ModelAsignarPerfilesAlGrupoPerfil {

    private Integer grupoPerfilId;
    private List<Integer> perfilesIds;

    public Integer getGrupoPerfilId() {
        return grupoPerfilId;
    }

    public void setGrupoPerfilId(Integer grupoPerfilId) {
        this.grupoPerfilId = grupoPerfilId;
    }

    public List<Integer> getPerfilesIds() {
        return perfilesIds;
    }

    public void setPerfilesIds(List<Integer> perfilesIds) {
        this.perfilesIds = perfilesIds;
    }

}
