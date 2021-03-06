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

import com.auth.daos.admin.DaoGrupoPerfiles;
import com.auth.entities.admin.GrupoPerfiles;
import com.auth.entities.admin.Perfil;
import com.auth.managers.commons.ManagerSQL;
import com.auth.models.ModelAsignarPerfilesAlGrupoPerfil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class ManagerGrupoPerfil extends ManagerSQL<GrupoPerfiles, Integer> {

    public ManagerGrupoPerfil() {
        super(new DaoGrupoPerfiles());
    }

    public GrupoPerfiles asignarPerfiles(ModelAsignarPerfilesAlGrupoPerfil model) throws Exception {
        GrupoPerfiles gp = this.findOne(model.getGrupoPerfilId());
        ManagerPerfil managerPerfil = new ManagerPerfil();

        List<Perfil> perfiles = new ArrayList<>();
        model.getPerfilesIds().forEach(pId -> {
            try {
                perfiles.add(managerPerfil.findOne(pId));
            } catch (Exception e) {
            }
        });
        gp.setPerfilList(perfiles);
        this.update(gp);
        return gp;
    }

}
