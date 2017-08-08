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
import com.machineAdmin.daos.cg.admin.postgres.DaoSeccion;
import com.machineAdmin.daos.cg.admin.postgres.DaoUsuario;
import com.machineAdmin.daos.cg.admin.postgres.DaoUsuarioPermisos;
import com.machineAdmin.daos.cg.admin.postgres.DaoUsuariosPerfil;
import com.machineAdmin.daos.cg.commons.DaoSQLFacade;
import com.machineAdmin.entities.cg.admin.postgres.Menu;
import com.machineAdmin.entities.cg.admin.postgres.Modulo;
import com.machineAdmin.entities.cg.admin.postgres.Permiso;
import com.machineAdmin.entities.cg.admin.postgres.Seccion;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPermisos;
import com.machineAdmin.entities.cg.commons.IEntity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import static java.util.stream.Collectors.toList;

/**
 * clase utileria de metodos relacionados con permisos
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsPermissions {

    /**
     * obtiene por stacktrace el id de la accion actual en ejecucion
     *
     * @return id de accion actual en ejecucion
     */
    public static String obtenerAccionActual() {
        return Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    /**
     * sirve para obtener los permisos existentes disponibles en el sistem
     *
     * @return lista de permisos existentes
     */
    public static List<Permiso> getExistingPermissions() {
        DaoPermiso dao = new DaoPermiso();
        return dao.stream().collect(toList());
    }

    /**
     * genera una lista de los permisos que un usuario tiene asignados con su
     * profundidad de acceso
     *
     * @param userId id del usuario a obtener sus permisos
     * @return lista modelos con id de permiso y profundidad
     * @throws java.lang.Exception
     */
    public static List<Seccion> permisosAsignadosAlUsuario(String userId) throws Exception {
        DaoUsuario daoUsuario = new DaoUsuario();
        DaoSeccion daoSeccion = new DaoSeccion();
        
        DaoUsuarioPermisos daoUsuarioPermisos = new DaoUsuarioPermisos();
        UUID uuidUserId = UUID.fromString(userId);
        List<String> permisosUsuario = daoUsuarioPermisos.stream().where( e -> e.getUsuariosPermisosPK().getUsuario().equals(uuidUserId)).map( e -> e.getUsuariosPermisosPK().getPermiso()).collect(toList());
        
        //secciones disponibles -> eliminar todos los permisos que el usuario no tenga               
        List<Seccion> secciones = daoSeccion.findAll();

        List<Seccion> seccionesDeUsuario = new ArrayList<>();
        for (Seccion seccion : secciones) {

            List<Modulo> modulos = new ArrayList<>();
            for (Modulo modulo : seccion.getModuloList()) {

                List<Menu> menus = new ArrayList<>();
                for (Menu menu : modulo.getMenuList()) {

                    List<Permiso> permisos = new ArrayList<>();
                    for (Permiso permiso : menu.getPermisoList()) {                        
                        if (permisosUsuario.contains(permiso.getId())) {
                            permisos.add(permiso);
                        }
                    }
                    //si tiene permisos del menu agregar el menu
                    if (!permisos.isEmpty()) {
                        Menu m = new Menu(menu.getId());
                        m.setPermisoList(permisos);
                        menus.add(m);
                    }
                }
                //si tiene menus en el modulo, agregar el modulo
                if (!menus.isEmpty()) {
                    Modulo m = new Modulo(modulo.getId());
                    m.setMenuList(menus);
                    modulos.add(m);
                }
            }
            if (!modulos.isEmpty()) {
                Seccion s = new Seccion(seccion.getId());
                s.setModuloList(modulos);
                seccionesDeUsuario.add(s);
            }
        }
                       
        return seccionesDeUsuario;
    }

    /**
     * genera una lista de los permisos que un perfil tiene asignados con su
     * profundidad de acceso
     *
     * @param perfilId id del perfil a obtener sus permisos
     * @return lista modelos con id de permiso y profundidad
     */
    public static List<Permiso> permisosAsignadosAlPerfil(String perfilId) throws Exception {
//       DaoPerfil dao = new DaoPerfil();
//       Perfil perfil = dao.findOne(UUID.fromString(perfilId));
//       return perfil.getPermisoList();
        return null;
    }

}
