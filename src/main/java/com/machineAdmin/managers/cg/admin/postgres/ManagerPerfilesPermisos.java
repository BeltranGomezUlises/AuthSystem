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

import com.machineAdmin.daos.cg.admin.postgres.DaoPerfilesPermisos;
import com.machineAdmin.daos.cg.exceptions.ConstraintException;
import com.machineAdmin.daos.cg.exceptions.SQLPersistenceException;
import com.machineAdmin.entities.cg.admin.postgres.PerfilesPermisos;
import com.machineAdmin.managers.cg.commons.ManagerSQLFacade;
import com.machineAdmin.models.cg.ModelAsignarPermisos;
import com.machineAdmin.models.cg.ModelPermisoAsignado;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerPerfilesPermisos extends ManagerSQLFacade<PerfilesPermisos>{
    
    public ManagerPerfilesPermisos() {
        super(new DaoPerfilesPermisos());
    }
    
    public void asignarPermisosAlPerfil(ModelAsignarPermisos model) throws SQLPersistenceException, ConstraintException, Exception{               
        ManagerPerfil managerPerfil = new ManagerPerfil();
        ManagerPermiso managerPermiso = new ManagerPermiso();
              
        //borrar las relaciones actuales                
        List<Object> permisosDelPerfilPk = this.stream()
                .filter( p -> p.getPerfilesPermisosPK().getPerfil().equals(UUID.fromString(model.getId())))
                .map( p -> p.getPerfilesPermisosPK())
                .collect(toList());
        
        this.deleteAll(permisosDelPerfilPk);
        
        List<PerfilesPermisos> perfilesPermisos = new ArrayList<>();
        PerfilesPermisos perfilesPermisosRelacion;
        //asignar las nuevas relaciones        
        for (ModelPermisoAsignado permiso : model.getPermisos()) {
            perfilesPermisosRelacion = new PerfilesPermisos(UUID.fromString(model.getId()),permiso.getId());                                                            
            perfilesPermisosRelacion.setProfundidad(permiso.getProfundidad());                       
            perfilesPermisos.add(perfilesPermisosRelacion);
        }                                                   
        this.persistAll(perfilesPermisos);
        
    }
        
    
    
}
