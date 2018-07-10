package com.auth.utils;

/*
 * Copyright (C) 2017 Alonso --- alonso@kriblet.com
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
import com.auth.daos.admin.DaoPerfilesPermisos;
import com.auth.daos.admin.DaoPermiso;
import com.auth.daos.admin.DaoSeccion;
import com.auth.daos.admin.DaoUsuario;
import com.auth.daos.admin.DaoUsuariosPerfil;
import com.auth.daos.admin.DaoUsuariosPermisos;
import com.auth.entities.admin.Menu;
import com.auth.entities.admin.Modulo;
import com.auth.entities.admin.PerfilesPermisos;
import com.auth.entities.admin.Permiso;
import com.auth.entities.admin.Seccion;
import com.auth.entities.admin.Usuario;
import com.auth.entities.admin.UsuariosPermisos;
import com.auth.entities.commons.Profundidad;
import static com.auth.entities.commons.Profundidad.*;
import com.auth.managers.exceptions.AccesoDenegadoException;
import com.auth.managers.exceptions.ParametroInvalidoException;
import com.auth.managers.exceptions.TokenExpiradoException;
import com.auth.managers.exceptions.TokenInvalidoException;
import com.auth.models.ModelPermisoProfundidad;
import com.auth.models.ModelPermisoValido;
import com.auth.models.ModelPermisosAsignados;
import com.auth.models.ModelPermisosAsignados.ModelSeccion;
import com.auth.models.ModelPermisosAsignados.ModelSeccion.ModelModulo;
import com.auth.models.ModelPermisosAsignados.ModelSeccion.ModelModulo.ModelMenu;
import com.auth.models.ModelPermisosAsignados.ModelSeccion.ModelModulo.ModelMenu.ModelPermiso;
import com.auth.models.ModelSesion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.jinq.orm.stream.JinqStream;
import org.jinq.tuples.Tuple3;

/**
 * clase utileria de metodos relacionados con permisos
 *
 * @author Alonso --- alonso@kriblet.com
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
        Integer userId = UtilsJWT.getUserIdFrom(token);
        DaoUsuariosPermisos daoUsuariosPermisos = new DaoUsuariosPermisos();
        // el usuario puede tener permiso por permisos del usuario o por permisos de los perfiles, juntarlos todos aqui
        List<Profundidad> profundidadesDelPermisoDelUsuario = new ArrayList<>();
        try {
            //buscar profundidad de la accion por usuario           
            try {
                profundidadesDelPermisoDelUsuario.add(daoUsuariosPermisos.stream()
                        .where(up -> up.getUsuariosPermisosPK().getUsuario() == (userId) && up.getUsuariosPermisosPK().getPermiso().equals(accion))
                        .findFirst().get().getProfundidad());
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

    private static List<Profundidad> profundidadesPorPerfilesDelUsuario(Integer userId, String accion) {
        DaoUsuariosPerfil daoUsuariosPerfil = new DaoUsuariosPerfil();
        return daoUsuariosPerfil.stream()
                .where(up -> up.getUsuariosPerfilPK().getUsuario() == (userId) && up.getHereda().equals(Boolean.TRUE))
                .joinList(up -> up.getPerfil1().getPerfilesPermisosList())
                .where(pp -> pp.getTwo().getPerfilesPermisosPK().getPermiso().equals(accion))
                .select(pp -> pp.getTwo().getProfundidad())
                .collect(toList());
    }

    public static Set<Integer> idsDeUsuariosConLosPerfilesQueTieneElUsuario(Integer usuarioId) throws Exception {
        //buscar los id de los usuarios con mis perfiles 
        DaoUsuario daoUsuario = new DaoUsuario();
        Usuario u = daoUsuario.findOne(usuarioId);
        //ids de perfiles del usuario
        List<Integer> perfilesDelUsuario = u.getUsuariosPerfilList().stream()
                .map(up -> up.getUsuariosPerfilPK().getPerfil())
                .collect(toList());
        //ids de usuarios con esos perfiles
        DaoUsuariosPerfil daoUsuariosPerfil = new DaoUsuariosPerfil();

        Set<Integer> usuariosDeLosPerfiles = daoUsuariosPerfil.stream()
                .where(up -> perfilesDelUsuario.contains(up.getUsuariosPerfilPK().getPerfil()))
                .select(up -> up.getUsuariosPerfilPK().getUsuario())
                .collect(toSet());
        return usuariosDeLosPerfiles;
    }

    /**
     * sirve para obtener los permisos existentes disponibles en el sistem
     *
     * @return lista de permisos existentes
     * @throws java.lang.Exception
     */
    public static List<Permiso> getExistingPermissions() throws Exception {
        return new DaoPermiso().findAll();
    }

    /**
     * genera una lista de los permisos que un usuario tiene asignados con su profundidad de acceso
     *
     * @param usuarioId id del usuario a obtener sus permisos
     * @return lista modelos con id de permiso y profundidad
     * @throws java.lang.Exception
     */
    public static List<Seccion> permisosAsignadosAlUsuario(Integer usuarioId) throws Exception {
        DaoUsuario daoUsuario = new DaoUsuario();
        DaoSeccion daoSeccion = new DaoSeccion();

        DaoUsuariosPermisos daoUsuarioPermisos = new DaoUsuariosPermisos();
        List<String> permisosUsuario = daoUsuarioPermisos.stream().where(e -> e.getUsuariosPermisosPK().getUsuario() == (usuarioId)).map(e -> e.getUsuariosPermisosPK().getPermiso()).collect(toList());

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
     * @return modelo con la lista de permisos categorizados por seccion, modulo, menu y accion
     * @throws Exception
     */
    public static ModelPermisosAsignados permisosAsignadosAlUsuarioConProfundidad(Integer userId) throws Exception {
        DaoSeccion daoSeccion = new DaoSeccion();

        DaoUsuariosPermisos daoUsuarioPermisos = new DaoUsuariosPermisos();
        List<UsuariosPermisos> permisosUsuario = daoUsuarioPermisos.stream().where(e -> e.getUsuariosPermisosPK().getUsuario() == (userId)).collect(toList());

        //secciones disponibles -> eliminar todos los permisos que el usuario no tenga               
        List<Seccion> secciones = daoSeccion.findAll();

        List<ModelSeccion> seccionesDeUsuario = new ArrayList<>();
        ModelPermisosAsignados asignados = new ModelPermisosAsignados(seccionesDeUsuario);
        secciones.forEach((seccion) -> {
            List<ModelModulo> modulos = new ArrayList<>();
            seccion.getModuloList().forEach((modulo) -> {
                List<ModelMenu> menus = new ArrayList<>();
                modulo.getMenuList().forEach((menu) -> {
                    List<ModelPermiso> permisos = new ArrayList<>();
                    menu.getPermisoList().forEach((permiso) -> {
                        try {
                            String permisoId = permiso.getId();
                            UsuariosPermisos usuarioPermiso = permisosUsuario.stream().filter(up -> up.getUsuariosPermisosPK().getPermiso().equals(permiso.getId())).findFirst().get();
                            permisos.add(new ModelPermiso(permiso.getNombre(), permiso.getId(), usuarioPermiso.getProfundidad(), usuarioPermiso.getSucursal1().getId()));
                        } catch (NoSuchElementException e) {//no tiene el permiso
                        }
                    });
                    //si tiene permisos del menu agregar el menu
                    if (!permisos.isEmpty()) {
                        ModelMenu m = new ModelMenu(menu.getNombre(), menu.getId(), permisos);
                        menus.add(m);
                    }
                });
                //si tiene menus en el modulo, agregar el modulo
                if (!menus.isEmpty()) {
                    ModelModulo m = new ModelModulo(modulo.getNombre(), modulo.getId(), menus);
                    modulos.add(m);
                }
            });
            if (!modulos.isEmpty()) {
                ModelSeccion s = new ModelSeccion(seccion.getNombre(), seccion.getId(), modulos);
                seccionesDeUsuario.add(s);
            }
        });

        return asignados;
    }

    /**
     * genera una lista de los permisos que un perfil tiene asignados con su profundidad de acceso
     *
     * @param perfilId id del perfil a obtener sus permisos
     * @return lista modelos con id de permiso y profundidad
     * @throws java.lang.Exception
     */
    public static ModelPermisosAsignados permisosAsignadosAlPerfil(Integer perfilId) throws Exception {
        DaoSeccion daoSeccion = new DaoSeccion();
        DaoPerfilesPermisos daoPerfilesPermisos = new DaoPerfilesPermisos();

        List<PerfilesPermisos> perfilesPermisos = daoPerfilesPermisos.stream()
                .where(e -> e.getPerfilesPermisosPK().getPerfil() == (perfilId))
                .collect(toList());

        //secciones disponibles -> eliminar todos los permisos que el usuario no tenga               
        List<Seccion> secciones = daoSeccion.findAll();

        List<ModelSeccion> seccionesDePerfil = new ArrayList<>();
        ModelPermisosAsignados asignados = new ModelPermisosAsignados(seccionesDePerfil);
        secciones.forEach(seccion -> {
            List<ModelModulo> modulos = new ArrayList<>();
            seccion.getModuloList().forEach((modulo) -> {
                List<ModelMenu> menus = new ArrayList<>();
                modulo.getMenuList().forEach((menu) -> {
                    List<ModelPermiso> permisos = new ArrayList<>();
                    menu.getPermisoList().forEach((permiso) -> {
                        try {
                            String permisoId = permiso.getId();
                            PerfilesPermisos perfilConPermiso = perfilesPermisos.stream().filter(pp -> pp.getPerfilesPermisosPK().getPermiso().equals(permisoId)).findFirst().get();
                            ModelPermiso modelPermiso = new ModelPermiso(permiso.getNombre(), permiso.getId(), perfilConPermiso.getProfundidad(), perfilConPermiso.getSucursal1().getId());
                            permisos.add(modelPermiso);
                        } catch (NoSuchElementException e) {//no encontro el permiso en ese perfil
                        }
                    });
                    //si tiene permisos del menu agregar el menu
                    if (!permisos.isEmpty()) {
                        ModelMenu m = new ModelMenu(menu.getNombre(), menu.getId(), permisos);
                        menus.add(m);
                    }
                });
                //si tiene menus en el modulo, agregar el modulo
                if (!menus.isEmpty()) {
                    ModelModulo m = new ModelModulo(modulo.getNombre(), modulo.getId(), menus);
                    modulos.add(m);
                }
            });
            if (!modulos.isEmpty()) {
                ModelSeccion s = new ModelSeccion(seccion.getNombre(), seccion.getId(), modulos);
                seccionesDePerfil.add(s);
            }
        });
        return asignados;
    }

    /**
     * genera los permisos conmutados del usuario con los permisos por perfiles con su profundidad de acceso mayoritaria
     *
     * @param usuarioId id de usuario a generar permisos conmutados
     * @return modelo con los permisos asignados
     * @throws java.lang.Exception
     */
    public static ModelPermisosAsignados permisosConmutadosDelUsuario(Integer usuarioId) throws Exception {
        DaoSeccion daoSeccion = new DaoSeccion();
        DaoUsuariosPerfil daoUsuariosPerfil = new DaoUsuariosPerfil();
        DaoUsuariosPermisos daoUsuarioPermisos = new DaoUsuariosPermisos();

        //permisos por usuario
        List<Tuple3<String, Profundidad, Integer>> permisosPorUsuario = daoUsuarioPermisos.stream()
                .where(e -> e.getUsuariosPermisosPK().getUsuario() == (usuarioId))
                .select(e -> new Tuple3<>(e.getUsuariosPermisosPK().getPermiso(), e.getProfundidad(), e.getSucursal1().getId()))
                .collect(toList());

        //permisos por perfiles
        List<Tuple3<String, Profundidad, Integer>> permisosDeLosPerfiles = daoUsuariosPerfil.stream()
                .where(up -> up.getUsuariosPerfilPK().getUsuario() == (usuarioId) && up.getHereda().equals(Boolean.TRUE))
                .join(up -> JinqStream.of(up.getPerfil1()))
                .joinList(p -> p.getTwo().getPerfilesPermisosList())
                .select(p -> new Tuple3<>(p.getTwo().getPerfilesPermisosPK().getPermiso(), p.getTwo().getProfundidad(), p.getTwo().getSucursal1().getId()))
                .collect(toList());

        //secciones disponibles -> eliminar todos los permisos que el usuario no tenga               
        List<Seccion> secciones = daoSeccion.findAll();

        List<ModelSeccion> seccionesDeUsuario = new ArrayList<>();
        ModelPermisosAsignados asignados = new ModelPermisosAsignados(seccionesDeUsuario);
        secciones.forEach((seccion) -> {
            List<ModelModulo> modulos = new ArrayList<>();
            seccion.getModuloList().forEach((modulo) -> {
                List<ModelMenu> menus = new ArrayList<>();
                modulo.getMenuList().forEach((menu) -> {
                    List<ModelPermiso> permisos = new ArrayList<>();
                    menu.getPermisoList().forEach((permiso) -> {
                        //añadir la profundidad por usuario
                        Map<Integer, ArrayList<Profundidad>> profundidadesPorSucursal = new HashMap<>(); //key sucursal id, value lista de profundidades
                        for (Tuple3<String, Profundidad, Integer> t : permisosPorUsuario) {
                            if (t.getOne().equals(permiso.getId())) {
                                ArrayList<Profundidad> profs = new ArrayList<>();
                                profs.add(t.getTwo());
                                profundidadesPorSucursal.put(t.getThree(), profs);
                            }
                        }
                        //añadir las profundidades por perfil
                        for (Tuple3<String, Profundidad, Integer> t : permisosDeLosPerfiles) {
                            if (t.getOne().equals(permiso.getId())) {
                                ArrayList<Profundidad> profundidades = profundidadesPorSucursal.get(t.getThree());
                                if (profundidades == null) {
                                    profundidadesPorSucursal.put(t.getThree(), new ArrayList(Arrays.asList(t.getTwo())));
                                } else {
                                    profundidades.add(t.getTwo());
                                    profundidadesPorSucursal.put(t.getThree(), profundidades);
                                }
                            }
                        }
                        for (Map.Entry<Integer, ArrayList<Profundidad>> entry : profundidadesPorSucursal.entrySet()) {
                            try {
                                permisos.add(new ModelPermiso(permiso.getNombre(), permiso.getId(), profundidadMayor(entry.getValue()), entry.getKey()));
                            } catch (ParametroInvalidoException ex) {
                                Logger.getLogger(UtilsPermissions.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    //si tiene permisos del menu agregar el menu
                    if (!permisos.isEmpty()) {
                        ModelMenu m = new ModelMenu(menu.getNombre(), menu.getId(), permisos);
                        menus.add(m);
                    }
                });
                //si tiene menus en el modulo, agregar el modulo
                if (!menus.isEmpty()) {
                    ModelModulo m = new ModelModulo(modulo.getNombre(), modulo.getId(), menus);
                    modulos.add(m);
                }
            });
            if (!modulos.isEmpty()) {
                ModelSeccion s = new ModelSeccion(seccion.getNombre(), seccion.getId(), modulos);
                seccionesDeUsuario.add(s);
            }
        });
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

    /**
     * Consulta si el usuario dueño del token de sesion tiene un permiso en particular
     *
     * @param tokenSesion token de sesion
     * @param permisoId identificador del permiso
     * @return modelo con la profundidad del permiso y
     */
    public static ModelPermisoValido tienePermiso(final String tokenSesion, final String permisoId) throws TokenInvalidoException, TokenExpiradoException, ParametroInvalidoException {
        EntityManager em = UtilsDB.getEMFactory().createEntityManager();
        ModelSesion modelSesion = UtilsJWT.getModelSesionFrom(tokenSesion);
        List<ModelPermisoProfundidad> permisosValidosUsuario = new ArrayList<>();
        //obtener las profundidades por parte del usuario        
        try {
            permisosValidosUsuario.add(em.createQuery("SELECT NEW com.auth.models.ModelPermisoProfundidad(usp.profundidad) FROM UsuariosPermisos usp WHERE usp.usuariosPermisosPK.permiso = :permisoId AND usp.usuariosPermisosPK.usuario = :usuarioId AND usp.usuariosPermisosPK.sucursal = :sucursalId", ModelPermisoProfundidad.class)
                    .setParameter("permisoId", permisoId)
                    .setParameter("usuarioId", modelSesion.getUserId())
                    .setParameter("sucursalId", modelSesion.getSucursalId())
                    .getSingleResult()
            );
        } catch (NoResultException e) {
        }
        try {
            permisosValidosUsuario.addAll(
                    em.createQuery("SELECT NEW com.auth.models.ModelPermisoProfundidad(pep.profundidad) FROM PerfilesPermisos pep WHERE pep.perfilesPermisosPK.permiso = :permisoId AND pep.perfilesPermisosPK.sucursal = :sucursalId AND pep.perfilesPermisosPK.perfil in ( SELECT usp.usuariosPerfilPK.perfil FROM UsuariosPerfil usp WHERE usp.usuariosPerfilPK.usuario = :usuarioId AND usp.hereda = true )", ModelPermisoProfundidad.class)
                            .setParameter("permisoId", permisoId)
                            .setParameter("usuarioId", modelSesion.getUserId())
                            .setParameter("sucursalId", modelSesion.getSucursalId())
                            .getResultList()
            );
        } catch (NoResultException e) {
        }
        if (permisosValidosUsuario.isEmpty()) {
            return new ModelPermisoValido(false, null);
        } else {
            return new ModelPermisoValido(profundidadMayor(permisosValidosUsuario.stream().map(e -> e.getProfundidad()).collect(toList())));
        }
    }
}
