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

import com.machineAdmin.daos.cg.admin.postgres.DaoPerfil;
import com.machineAdmin.daos.cg.commons.DaoSQLFacade;
import com.machineAdmin.entities.cg.admin.postgres.Perfil;
import com.machineAdmin.entities.cg.admin.postgres.PerfilesPermisos;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.commons.ManagerSQLCatalog;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.ModelAsignarPermisos;
import com.machineAdmin.models.cg.ModelPermisoAsignado;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerPerfil extends ManagerSQLCatalog<Perfil, Integer> {

    public ManagerPerfil() {
        super(new DaoPerfil());
    }   

    public ManagerPerfil(DaoSQLFacade dao, String token, Profundidad profundidad) throws TokenInvalidoException, TokenExpiradoException {
        super(dao, token, profundidad);
    }
   
    @Override
    public String nombreColeccionParaRegistros() {
        return "perfiles";
    }

    public void asignarPermisos(ModelAsignarPermisos model) throws Exception {
        ManagerPerfilesPermisos managerPerfilesPermisos = new ManagerPerfilesPermisos();
        //borrar los actuales
        Integer perfilId = model.getId();
        managerPerfilesPermisos.deleteAll(managerPerfilesPermisos.stream()
                .where( pp -> pp.getPerfilesPermisosPK().getPerfil().equals(model.getId()))
                .select( pp -> pp.getPerfilesPermisosPK())
                .collect(toList()));                
        //ingresar los nuevos
        List<PerfilesPermisos> permisosNuevos = new ArrayList<>();        
        for (ModelPermisoAsignado permiso : model.getPermisos()) {
            PerfilesPermisos pp = new PerfilesPermisos(perfilId, permiso.getId());
            pp.setProfundidad(permiso.getProfundidad());
            permisosNuevos.add(pp);                       
        }
        managerPerfilesPermisos.persistAll(permisosNuevos);
        
    }
}
