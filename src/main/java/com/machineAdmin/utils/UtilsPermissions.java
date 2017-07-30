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
package com.machineAdmin.utils;

import com.machineAdmin.daos.cg.admin.postgres.DaoPermiso;
import com.machineAdmin.entities.cg.admin.postgres.Permiso;
import com.machineAdmin.managers.cg.admin.postgres.ManagerPerfilesPermisos;
import com.machineAdmin.managers.cg.admin.postgres.ManagerPermiso;
import com.machineAdmin.managers.cg.admin.postgres.ManagerUsuariosPermisos;
import com.machineAdmin.models.cg.ModelPermisoAsignado;
import java.util.List;
import java.util.UUID;
import static java.util.stream.Collectors.toList;

/**
 * clase utileria de metodos relacionados con permisos
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsPermissions {

    /**
     * sirve para obtener los permisos existentes disponibles en el sistem
     * @return  lista de permisos existentes
     */
    public static List<Permiso> getExistingPermissions() {
        DaoPermiso dao = new DaoPermiso();        
        return dao.stream().collect(toList());
    }

    /**
     * genera una lista de los permisos que un usuario tiene asignados con su profundidad de acceso
     * @param userId id del usuario a obtener sus permisos
     * @return lista modelos con id de permiso y profundidad
     */
    public static List<ModelPermisoAsignado> permisosAsignadosAUsuario(String userId) {
        ManagerUsuariosPermisos managerUsuariosPermisos = new ManagerUsuariosPermisos();
        return managerUsuariosPermisos.stream()
                .filter(up -> up.getUsuariosPermisosPK().getUsuario().equals(UUID.fromString(userId)))
                .map(up -> {
                    ModelPermisoAsignado asignado = new ModelPermisoAsignado();
                    asignado.setId(up.getUsuariosPermisosPK().getPermiso());
                    asignado.setProfundidad(up.getProfundidad());
                    return asignado;
                }).collect(toList());
    }

    /**
     * genera una lista de los permisos que un perfil tiene asignados con su profundidad de acceso
     * @param perfilId id del perfil a obtener sus permisos
     * @return lista modelos con id de permiso y profundidad
     */
    public static List<ModelPermisoAsignado> permisosAsignadosAPerfil(String perfilId) {
        ManagerPerfilesPermisos managerPerfilesPermisos = new ManagerPerfilesPermisos();
        return managerPerfilesPermisos.stream()
                .filter(pp -> pp.getPerfilesPermisosPK().getPerfil().equals(UUID.fromString(perfilId)))
                .map(pp -> {
                    ModelPermisoAsignado asignado = new ModelPermisoAsignado();
                    asignado.setId(pp.getPerfilesPermisosPK().getPermiso());
                    asignado.setProfundidad(pp.getProfundidad());
                    return asignado;
                }).collect(toList());
    }

}
