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
package com.machineAdmin.managers.admin;

import com.machineAdmin.daos.admin.DaoGrupoPerfiles;
import com.machineAdmin.entities.admin.GrupoPerfiles;
import com.machineAdmin.entities.admin.Perfil;
import com.machineAdmin.managers.commons.ManagerSQL;
import com.machineAdmin.models.cg.ModelAsignarPerfilesAlGrupoPerfil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerGrupoPerfil extends ManagerSQL<GrupoPerfiles, Integer> {

    public ManagerGrupoPerfil() {
        super(new DaoGrupoPerfiles());
    }

    public GrupoPerfiles asignarPerfiles(ModelAsignarPerfilesAlGrupoPerfil model) throws Exception {
        GrupoPerfiles gp = this.findOne(model.getGrupoPerfilId());
        ManagerPerfil managerPerfil = new ManagerPerfil();
        managerPerfil.setUsuario(this.getUsuario());

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
