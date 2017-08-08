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

import com.machineAdmin.daos.cg.admin.postgres.DaoPerfil;
import com.machineAdmin.daos.cg.admin.postgres.DaoPermiso;
import com.machineAdmin.daos.cg.admin.postgres.DaoUsuario;
import com.machineAdmin.entities.cg.admin.postgres.Perfil;
import com.machineAdmin.entities.cg.admin.postgres.Permiso;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import java.util.List;
import java.util.UUID;
import static java.util.stream.Collectors.toList;

/**
 * clase utileria de metodos relacionados con permisos
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsPermissions {

    /**
     * obtiene por stacktrace el id de la accion actual en ejecucion
     * @return id de accion actual en ejecucion
     */
    public static String obtenerAccionActual() {
        return Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
    }
    
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
    public static List<Permiso> permisosAsignadosAUsuario(String userId) {
        DaoUsuario dao = new DaoUsuario();        
        Usuario usuario = dao.findOne(UUID.fromString(userId));
        return usuario.getPermisoList();        
    }

    /**
     * genera una lista de los permisos que un perfil tiene asignados con su profundidad de acceso
     * @param perfilId id del perfil a obtener sus permisos
     * @return lista modelos con id de permiso y profundidad
     */
    public static List<Permiso> permisosAsignadosAPerfil(String perfilId) {
       DaoPerfil dao = new DaoPerfil();
       Perfil perfil = dao.findOne(UUID.fromString(perfilId));
       return perfil.getPermisoList();
    }

}
