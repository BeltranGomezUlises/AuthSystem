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
package com.auth.managers.admin;

import com.auth.daos.admin.DaoPerfil;
import com.auth.daos.admin.DaoPerfilesPermisos;
import com.auth.entities.admin.Perfil;
import com.auth.entities.admin.PerfilesPermisos;
import com.auth.managers.commons.ManagerSQL;
import com.auth.models.ModelAsignarPermisos;
import java.util.List;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class ManagerPerfil extends ManagerSQL<Perfil, Integer> {

    public ManagerPerfil() {
        super(new DaoPerfil());
    }

    /**
     * asigna los permisos colocados en el modelo ModelAsignarPermisos al usuario correspondiente a la propiedad id del modelo
     *
     * @param model modelo para asignar permisos
     * @return asignaciones de permisos con el perfil
     * @throws Exception si existe un error de I/O
     */
    public List<PerfilesPermisos> reemplazarPermisosDelPerfil(ModelAsignarPermisos model) throws Exception {
        DaoPerfilesPermisos daoPerfilesPermisos = new DaoPerfilesPermisos();
        return daoPerfilesPermisos.reemplazarPermisosDelPerfil(model.getId(), model.getPermisos());
    }
}
