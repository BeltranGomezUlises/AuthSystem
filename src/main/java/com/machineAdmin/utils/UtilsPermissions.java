package com.machineAdmin.utils;

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
import com.machineAdmin.daos.cg.admin.postgres.DaoPerfil;
import com.machineAdmin.daos.cg.admin.postgres.DaoPerfilesPermisos;
import com.machineAdmin.daos.cg.admin.postgres.DaoPermiso;
import com.machineAdmin.daos.cg.admin.postgres.DaoSeccion;
import com.machineAdmin.daos.cg.admin.postgres.DaoUsuario;
import com.machineAdmin.daos.cg.admin.postgres.DaoUsuariosPermisos;
import com.machineAdmin.daos.cg.admin.postgres.DaoUsuariosPerfil;
import com.machineAdmin.entities.cg.admin.postgres.Menu;
import com.machineAdmin.entities.cg.admin.postgres.Modulo;
import com.machineAdmin.entities.cg.admin.postgres.PerfilesPermisos;
import com.machineAdmin.entities.cg.admin.postgres.Permiso;
import com.machineAdmin.entities.cg.admin.postgres.Seccion;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPermisos;
import com.machineAdmin.entities.cg.commons.Profundidad;
import static com.machineAdmin.entities.cg.commons.Profundidad.*;
import com.machineAdmin.managers.cg.exceptions.AccesoDenegadoException;
import com.machineAdmin.managers.cg.exceptions.ParametroInvalidoException;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.ModelPermisoAsignado;
import com.machineAdmin.models.cg.ModelPermisosAsignados;
import com.machineAdmin.models.cg.ModelPermisosAsignados.ModelSeccion;
import com.machineAdmin.models.cg.ModelPermisosAsignados.ModelSeccion.ModelModulo;
import com.machineAdmin.models.cg.ModelPermisosAsignados.ModelSeccion.ModelModulo.ModelMenu;
import com.machineAdmin.models.cg.ModelPermisosAsignados.ModelSeccion.ModelModulo.ModelMenu.ModelPermiso;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import org.jinq.orm.stream.JinqStream;
import org.jinq.tuples.Pair;

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
    public static String accionActual() {
        String className = Thread.currentThread().getStackTrace()[3].getClassName();
        String raiz = "com.machineAdmin.services.";
        return className.substring(raiz.length()) + "." + Thread.currentThread().getStackTrace()[3].getMethodName();
    }

    public static Profundidad obtenerProfundidad(String token, String accion) throws TokenInvalidoException, TokenExpiradoException, AccesoDenegadoException {
        Long userId = UtilsJWT.getUserIdFrom(token);
        DaoUsuariosPermisos daoUsuariosPermisos = new DaoUsuariosPermisos();
        // el usuario puede tener permiso por permisos del usuario o por permisos de los perfiles, juntarlos todos aqui
        List<Profundidad> profundidadesDelPermisoDelUsuario = new ArrayList<>();
        try {
            //buscar profundidad de la accion por usuario           
            try {
                profundidadesDelPermisoDelUsuario.add( daoUsuariosPermisos.stream()
                        .where(up -> up.getUsuariosPermisosPK().getUsuario().equals(userId) && up.getUsuariosPermisosPK().getPermiso().equals(accion))
                        .findFirst().get().getProfundidad() );                
            } catch (NoSuchElementException e) {
            }                      
            //buscar la profundidad de la accion por perfiles (cada perfil, puede tener el mismo permiso, pero con distintas profundidades                                         
            List<Profundidad> profundidadesPorPerfiles = profundidadesPorPerfilesDelUsuario(userId, accion);
            profundidadesDelPermisoDelUsuario.addAll(profundidadesPorPerfiles);

            return profundidadMayor(profundidadesDelPermisoDelUsuario);            
        } catch (ParametroInvalidoException e) {
        }
        throw new AccesoDenegadoException("No Tiene permiso para ejecutar esta acción");
    }

    private static List<Profundidad> profundidadesPorPerfilesDelUsuario(Long userId, String accion) {
        DaoUsuariosPerfil daoUsuariosPerfil = new DaoUsuariosPerfil();
        return daoUsuariosPerfil.stream()
                .where(up -> up.getUsuariosPerfilPK().getUsuario().equals(userId) && up.getHereda().equals(Boolean.TRUE))
                .joinList(up -> up.getPerfil1().getPerfilesPermisosList())
                .where(pp -> pp.getTwo().getPerfilesPermisosPK().getPermiso().equals(accion))
                .select(pp -> pp.getTwo().getProfundidad())
                .collect(toList());
    }

    public static Set<Long> idsDeUsuariosConLosPerfilesQueTieneElUsuario(Long usuarioId) throws Exception {
        //buscar los id de los usuarios con mis perfiles 
        DaoUsuario daoUsuario = new DaoUsuario();
        Usuario u = daoUsuario.findOne(usuarioId);
        //ids de perfiles del usuario
        List<Long> perfilesDelUsuario = u.getUsuariosPerfilList().stream()
                .map(up -> up.getUsuariosPerfilPK().getPerfil())
                .collect(toList());
        //ids de usuarios con esos perfiles
        DaoUsuariosPerfil daoUsuariosPerfil = new DaoUsuariosPerfil();

        Set<Long> usuariosDeLosPerfiles = daoUsuariosPerfil.stream()
                .where(up -> perfilesDelUsuario.contains(up.getUsuariosPerfilPK().getPerfil()))
                .select(up -> up.getUsuariosPerfilPK().getUsuario())
                .collect(toSet());        
        return usuariosDeLosPerfiles;
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
     * @param usuarioId id del usuario a obtener sus permisos
     * @return lista modelos con id de permiso y profundidad
     * @throws java.lang.Exception
     */
    public static List<Seccion> permisosAsignadosAlUsuario(Integer usuarioId) throws Exception {
        DaoUsuario daoUsuario = new DaoUsuario();
        DaoSeccion daoSeccion = new DaoSeccion();

        DaoUsuariosPermisos daoUsuarioPermisos = new DaoUsuariosPermisos();
        List<String> permisosUsuario = daoUsuarioPermisos.stream().where(e -> e.getUsuariosPermisosPK().getUsuario().equals(usuarioId)).map(e -> e.getUsuariosPermisosPK().getPermiso()).collect(toList());

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
     * genera los permisos del usuario con su profundidad de acceso
     *
     * @param userId id de usuario
     * @return modelo con la lista de permisos categorizados por seccion,
     * modulo, menu y accion
     * @throws Exception
     */
    public static ModelPermisosAsignados permisosAsignadosAlUsuarioConProfundidad(Integer userId) throws Exception {
        DaoUsuario daoUsuario = new DaoUsuario();
        DaoSeccion daoSeccion = new DaoSeccion();

        DaoUsuariosPermisos daoUsuarioPermisos = new DaoUsuariosPermisos();
        List<UsuariosPermisos> permisosUsuario = daoUsuarioPermisos.stream().where(e -> e.getUsuariosPermisosPK().getUsuario().equals(userId)).collect(toList());

        //secciones disponibles -> eliminar todos los permisos que el usuario no tenga               
        List<Seccion> secciones = daoSeccion.findAll();

        List<ModelSeccion> seccionesDeUsuario = new ArrayList<>();
        ModelPermisosAsignados asignados = new ModelPermisosAsignados(seccionesDeUsuario);
        for (Seccion seccion : secciones) {

            List<ModelModulo> modulos = new ArrayList<>();
            for (Modulo modulo : seccion.getModuloList()) {

                List<ModelMenu> menus = new ArrayList<>();
                for (Menu menu : modulo.getMenuList()) {

                    List<ModelPermiso> permisos = new ArrayList<>();
                    for (Permiso permiso : menu.getPermisoList()) {
                        try {
                            String permisoId = permiso.getId();
                            UsuariosPermisos usuarioPermiso = permisosUsuario.stream().filter(up -> up.getUsuariosPermisosPK().getPermiso().equals(permiso.getId())).findFirst().get();
                            permisos.add(new ModelPermiso(permiso.getNombre(), permiso.getId(), usuarioPermiso.getProfundidad()));
                        } catch (NoSuchElementException e) {//no tiene el permiso
                        }
                    }
                    //si tiene permisos del menu agregar el menu
                    if (!permisos.isEmpty()) {
                        ModelMenu m = new ModelMenu(menu.getNombre(), menu.getId(), permisos);
                        menus.add(m);
                    }
                }
                //si tiene menus en el modulo, agregar el modulo
                if (!menus.isEmpty()) {
                    ModelModulo m = new ModelModulo(modulo.getNombre(), modulo.getId(), menus);
                    modulos.add(m);
                }
            }
            if (!modulos.isEmpty()) {
                ModelSeccion s = new ModelSeccion(seccion.getNombre(), seccion.getId(), modulos);
                seccionesDeUsuario.add(s);
            }
        }

        return asignados;
    }

    /**
     * genera una lista de los permisos que un perfil tiene asignados con su
     * profundidad de acceso
     *
     * @param perfilId id del perfil a obtener sus permisos
     * @return lista modelos con id de permiso y profundidad
     * @throws java.lang.Exception
     */
    public static ModelPermisosAsignados permisosAsignadosAlPerfil(Integer perfilId) throws Exception {
        DaoPerfil daoPerfil = new DaoPerfil();
        DaoSeccion daoSeccion = new DaoSeccion();

        DaoPerfilesPermisos daoPerfilesPermisos = new DaoPerfilesPermisos();

        List<PerfilesPermisos> perfilesPermisos = daoPerfilesPermisos.stream()
                .where(e -> e.getPerfilesPermisosPK().getPerfil().equals(perfilId))
                .collect(toList());

        //secciones disponibles -> eliminar todos los permisos que el usuario no tenga               
        List<Seccion> secciones = daoSeccion.findAll();

        List<ModelSeccion> seccionesDePerfil = new ArrayList<>();
        ModelPermisosAsignados asignados = new ModelPermisosAsignados(seccionesDePerfil);
        for (Seccion seccion : secciones) {
            List<ModelModulo> modulos = new ArrayList<>();
            for (Modulo modulo : seccion.getModuloList()) {

                List<ModelMenu> menus = new ArrayList<>();
                for (Menu menu : modulo.getMenuList()) {

                    List<ModelPermiso> permisos = new ArrayList<>();
                    for (Permiso permiso : menu.getPermisoList()) {
                        try {
                            String permisoId = permiso.getId();
                            PerfilesPermisos perfilConPermiso = perfilesPermisos.stream().filter(pp -> pp.getPerfilesPermisosPK().getPermiso().equals(permisoId)).findFirst().get();
                            ModelPermiso modelPermiso = new ModelPermiso(permiso.getNombre(), permiso.getId(), perfilConPermiso.getProfundidad());
                            permisos.add(modelPermiso);
                        } catch (NoSuchElementException e) {//no encontro el permiso en ese perfil
                        }
                    }
                    //si tiene permisos del menu agregar el menu
                    if (!permisos.isEmpty()) {
                        ModelMenu m = new ModelMenu(menu.getNombre(), menu.getId(), permisos);
                        menus.add(m);
                    }
                }
                //si tiene menus en el modulo, agregar el modulo
                if (!menus.isEmpty()) {
                    ModelModulo m = new ModelModulo(modulo.getNombre(), modulo.getId(), menus);
                    modulos.add(m);
                }
            }
            if (!modulos.isEmpty()) {
                ModelSeccion s = new ModelSeccion(seccion.getNombre(), seccion.getId(), modulos);
                seccionesDePerfil.add(s);
            }
        }
        return asignados;
    }

    /**
     * genera los permisos conmutados del usuario con los permisos por perfiles
     * con su profundidad de acceso mayoritaria
     *
     * @param usuarioId id de usuario a generar permisos conmutados
     * @return modelo con los permisos asignados
     * @throws java.lang.Exception
     */
    public static ModelPermisosAsignados permisosConmutadosDelUsuario(Long usuarioId) throws Exception {
        DaoSeccion daoSeccion = new DaoSeccion();

        DaoUsuariosPerfil daoUsuariosPerfil = new DaoUsuariosPerfil();
        DaoPerfilesPermisos daoPerfilesPermisos = new DaoPerfilesPermisos();

        DaoUsuariosPermisos daoUsuarioPermisos = new DaoUsuariosPermisos();

        //permisos por usuario
        List<ModelPermisoAsignado> permisosPorUsuario = daoUsuarioPermisos.stream()
                .where(e -> e.getUsuariosPermisosPK().getUsuario().equals(usuarioId))
                .select(e -> new Pair<>(e.getUsuariosPermisosPK().getPermiso(), e.getProfundidad()))
                .map(pair -> {
                    ModelPermisoAsignado model = new ModelPermisoAsignado();
                    model.setId(pair.getOne());
                    model.setProfundidad(pair.getTwo());
                    return model;
                })
                .collect(toList());

        //permisos por perfiles
        List<ModelPermisoAsignado> permisosDeLosPerfiles = daoUsuariosPerfil.stream()
                .where(up -> up.getUsuariosPerfilPK().getUsuario().equals(usuarioId) && up.getHereda().equals(Boolean.TRUE))
                .join(up -> JinqStream.of(up.getPerfil1()))
                .joinList(p -> p.getTwo().getPerfilesPermisosList())
                .select(p -> p.getTwo())
                .map(p -> {
                    ModelPermisoAsignado model = new ModelPermisoAsignado();
                    model.setId(p.getPerfilesPermisosPK().getPermiso());
                    model.setProfundidad(p.getProfundidad());
                    return model;
                })
                .collect(toList());

        //secciones disponibles -> eliminar todos los permisos que el usuario no tenga               
        List<Seccion> secciones = daoSeccion.findAll();

        List<ModelSeccion> seccionesDeUsuario = new ArrayList<>();
        ModelPermisosAsignados asignados = new ModelPermisosAsignados(seccionesDeUsuario);
        for (Seccion seccion : secciones) {

            List<ModelModulo> modulos = new ArrayList<>();
            for (Modulo modulo : seccion.getModuloList()) {

                List<ModelMenu> menus = new ArrayList<>();
                for (Menu menu : modulo.getMenuList()) {

                    List<ModelPermiso> permisos = new ArrayList<>();
                    for (Permiso permiso : menu.getPermisoList()) {
                        try {
                            String permisoId = permiso.getId();
                            List<Profundidad> profundidades = new ArrayList<>();
                            //añadir la profundidad por usuario
                            try {
                                Profundidad profundidadPorUsuario = permisosPorUsuario.stream().filter(p -> p.getId().equals(permiso.getId())).findFirst().get().getProfundidad();
                                profundidades.add(profundidadPorUsuario);
                            } catch (NoSuchElementException e) { //ignorar si no tiene el permiso
                            }
                            //añadir las profundidades por perfil
                            try {
                                permisosDeLosPerfiles.stream()
                                        .filter(permi -> permi.getId().equals(permiso.getId()))
                                        .forEach(p -> profundidades.add(p.getProfundidad()));
                            } catch (NoSuchElementException e) { //omitir si no existe
                            }

                            permisos.add(new ModelPermiso(permiso.getNombre(), permiso.getId(), profundidadMayor(profundidades)));
                        } catch (ParametroInvalidoException e) {//no tiene el permiso                            
                        }
                    }
                    //si tiene permisos del menu agregar el menu
                    if (!permisos.isEmpty()) {
                        ModelMenu m = new ModelMenu(menu.getNombre(), menu.getId(), permisos);
                        menus.add(m);
                    }
                }
                //si tiene menus en el modulo, agregar el modulo
                if (!menus.isEmpty()) {
                    ModelModulo m = new ModelModulo(modulo.getNombre(), modulo.getId(), menus);
                    modulos.add(m);
                }
            }
            if (!modulos.isEmpty()) {
                ModelSeccion s = new ModelSeccion(seccion.getNombre(), seccion.getId(), modulos);
                seccionesDeUsuario.add(s);
            }
        }

        return asignados;
    }

    public static Profundidad profundidadMayor(List<Profundidad> profundidades) throws ParametroInvalidoException {
        if (profundidades.contains(TODOS)) {
            return TODOS;
        }
        if (profundidades.contains(PROPIOS_MAS_PERFILES)) {
            return PROPIOS_MAS_PERFILES;
        }
        if (profundidades.contains(PROPIOS)) {
            return PROPIOS;
        }
        throw new ParametroInvalidoException("No existe una profundidad valida en la lista proporsionada");
    }
}
