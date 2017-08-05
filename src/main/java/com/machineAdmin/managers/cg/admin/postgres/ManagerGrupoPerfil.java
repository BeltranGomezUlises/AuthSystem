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
package com.machineAdmin.managers.cg.admin.postgres;

import com.machineAdmin.daos.cg.admin.postgres.DaoGrupoPerfiles;
import com.machineAdmin.daos.cg.exceptions.ConstraintException;
import com.machineAdmin.daos.cg.exceptions.SQLPersistenceException;
import com.machineAdmin.entities.cg.admin.postgres.GrupoPerfiles;
import com.machineAdmin.entities.cg.admin.postgres.Perfil;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.commons.ManagerSQLCatalogFacade;
import com.machineAdmin.models.cg.ModelAsignarPerfilesAlGrupoPerfil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerGrupoPerfil extends ManagerSQLCatalogFacade<GrupoPerfiles, UUID> {

    public ManagerGrupoPerfil(){
        super(new DaoGrupoPerfiles());
    }
    
    public ManagerGrupoPerfil(String usuario) {
        super(usuario, new DaoGrupoPerfiles());
    }

    public ManagerGrupoPerfil(Profundidad profundidad, String token) {
        super(new DaoGrupoPerfiles(), profundidad, token);
    }

    public void asignarPerfiles(ModelAsignarPerfilesAlGrupoPerfil model) throws SQLPersistenceException, ConstraintException {
        GrupoPerfiles gp = this.findOne(UUID.fromString(model.getGrupoPerfilId()));
        ManagerPerfil managerPerfil = new ManagerPerfil(this.getUsuario());
        List<Perfil> perfiles = new ArrayList<>();
        model.getPerfilesIds().forEach(pId -> perfiles.add(managerPerfil.findOne(UUID.fromString(pId))));
        gp.setPerfilList(perfiles);
        this.update(gp);
    }

    @Override
    public String nombreColeccionParaRegistros() {
        return "gruposPerfiles";
    }

}
