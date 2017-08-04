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
import com.machineAdmin.daos.cg.admin.postgres.DaoPermiso;
import com.machineAdmin.daos.cg.exceptions.ConstraintException;
import com.machineAdmin.daos.cg.exceptions.SQLPersistenceException;
import com.machineAdmin.entities.cg.admin.postgres.Perfil;
import com.machineAdmin.entities.cg.admin.postgres.Permiso;
import com.machineAdmin.managers.cg.commons.ManagerSQLCatalogFacade;
import com.machineAdmin.models.cg.ModelAsignarPermisos;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerPerfil extends ManagerSQLCatalogFacade<Perfil, UUID>{
    
    public ManagerPerfil(String usuario) {
        super(usuario, new DaoPerfil());
    }
    
    public ManagerPerfil() {
        super(new DaoPerfil());
    }

    @Override
    protected String nombreColeccionParaRegistros() {
        return "perfiles";
    }

    public void asignarPermisos(ModelAsignarPermisos model) throws SQLPersistenceException, ConstraintException{
        Perfil perfil = dao.findOne(model.getId());
        List<Permiso> permisosNuevos = new ArrayList<>();
        
        DaoPermiso daoPermiso = new DaoPermiso();        
        model.getPermisosIds().forEach((permisoId) -> {
            permisosNuevos.add(daoPermiso.findOne(permisoId));
        });        
        
        perfil.setPermisoList(permisosNuevos);                                
        dao.update(perfil);
    }
}
