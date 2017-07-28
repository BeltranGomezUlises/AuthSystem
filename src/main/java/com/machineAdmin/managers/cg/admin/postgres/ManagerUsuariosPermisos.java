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

import com.machineAdmin.daos.cg.admin.postgres.DaoUsuariosPermisos;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPermisos;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPermisosPK;
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
public class ManagerUsuariosPermisos extends ManagerSQLFacade<UsuariosPermisos, UsuariosPermisosPK>{
    
    public ManagerUsuariosPermisos() {
        super(new DaoUsuariosPermisos());
    }
    
    public void asignarPermisos(ModelAsignarPermisos model) throws Exception{
        //aliminar los actuales de ese usuario
        List<UsuariosPermisosPK> usuariosPermisosPk = this.stream()
                .filter( up -> up.getUsuariosPermisosPK().getUsuario().equals(UUID.fromString(model.getId())))
                .map( up -> up.getUsuariosPermisosPK())
                .collect(toList());
        
        this.deleteAll(usuariosPermisosPk);
        //insertar los nuevos
        List<UsuariosPermisos> usuariosPermisos = new ArrayList<>();
        for (ModelPermisoAsignado permiso : model.getPermisos()) {
            UsuariosPermisos usuariosPermisosRelacion = new UsuariosPermisos(UUID.fromString(model.getId()), permiso.getId());
            usuariosPermisosRelacion.setProfundidad(permiso.getProfundidad());            
            usuariosPermisos.add(usuariosPermisosRelacion);
        }
        this.persistAll(usuariosPermisos);                
    }
    
    
}
