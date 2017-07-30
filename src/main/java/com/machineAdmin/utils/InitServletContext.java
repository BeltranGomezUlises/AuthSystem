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

import com.machineAdmin.daos.cg.admin.mongo.DaoCGConfig;
import com.machineAdmin.daos.cg.admin.mongo.DaoConfigMail;
import com.machineAdmin.daos.cg.admin.postgres.DaoGrupoPerfiles;
import com.machineAdmin.entities.cg.admin.mongo.CGConfig;
import com.machineAdmin.entities.cg.admin.mongo.ConfigMail;
import com.machineAdmin.entities.cg.admin.postgres.GrupoPerfiles;
import com.machineAdmin.entities.cg.admin.postgres.Menu;
import com.machineAdmin.entities.cg.admin.postgres.Modulo;
import com.machineAdmin.entities.cg.admin.postgres.Perfil;
import com.machineAdmin.entities.cg.admin.postgres.PerfilesPermisos;
import com.machineAdmin.entities.cg.admin.postgres.Permiso;
import com.machineAdmin.entities.cg.admin.postgres.Seccion;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPerfil;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPermisos;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.daos.cg.admin.postgres.DaoMenu;
import com.machineAdmin.daos.cg.admin.postgres.DaoModulo;
import com.machineAdmin.daos.cg.admin.postgres.DaoPerfil;
import com.machineAdmin.daos.cg.admin.postgres.DaoPerfilesPermisos;
import com.machineAdmin.daos.cg.admin.postgres.DaoPermiso;
import com.machineAdmin.daos.cg.admin.postgres.DaoSeccion;
import com.machineAdmin.daos.cg.admin.postgres.DaoUsuario;
import com.machineAdmin.daos.cg.admin.postgres.DaoUsuariosPerfil;
import com.machineAdmin.daos.cg.admin.postgres.DaoUsuariosPermisos;
import com.machineAdmin.services.cg.commons.ServiceFacade;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.reflections.Reflections;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class InitServletContext implements ServletContextListener {

    private final String PACKAGE_SERVICES = "com.machineAdmin.services";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            this.initDBConfigs();
        } catch (Exception ex) {
            Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    /**
     * inicio de configuracion del sistema en base de datos
     *
     * @throws Exception
     */
    private void initDBConfigs() throws Exception {
        System.out.println("INICIANDO LA CONFIGURACION EN BASE DE DATOS DEFAULT");

        this.initDBPermissions();

        //<editor-fold defaultstate="collapsed" desc="Creacion del perfil Master">
        //crear perfil master
        DaoPerfil daoPerfil = new DaoPerfil();

        Perfil perfilMaster = daoPerfil.findFirst();
        if (perfilMaster == null) {
            perfilMaster = new Perfil();
            perfilMaster.setNombre("Master");
            perfilMaster.setDescripcion("Perfil de control total del sistema");
            daoPerfil.persist(perfilMaster);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Creacion del grupo de perfiles">
        DaoGrupoPerfiles daoGrupoPerfil = new DaoGrupoPerfiles();

        GrupoPerfiles gp = daoGrupoPerfil.findFirst();
        if (gp == null) {
            gp = new GrupoPerfiles();
            gp.setNombre("Administradores del sistema");
            gp.setDescripcoin("Este grupo de roles puede administrar las configuraciones del sistemas, ademas de poder realizar las operaciones de negocio de toda la aplicacion");

            List<Perfil> perfilesDelRol = new ArrayList<>();
            perfilesDelRol.add(perfilMaster);
            gp.setPerfilList(perfilesDelRol);
            daoGrupoPerfil.persist(gp);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Asignacion de permisos al perfil">
        //crear relacion de perfil con permisos disponibles
        DaoPerfilesPermisos daoPerfilesPermisos = new DaoPerfilesPermisos();
        //asignar permisos al perfil
        for (Permiso permiso : UtilsPermissions.getExistingPermissions()) {
            PerfilesPermisos perfilPermisoRelacion = new PerfilesPermisos(perfilMaster.getId(), permiso.getId());

            if (!daoPerfilesPermisos.stream().anyMatch(pp -> pp.equals(perfilPermisoRelacion))) { // si no existe la relacion
                perfilPermisoRelacion.setPerfil1(perfilMaster);
                perfilPermisoRelacion.setPermiso1(permiso);
                perfilPermisoRelacion.setProfundidad(Profundidad.TODOS);
                daoPerfilesPermisos.persist(perfilPermisoRelacion);
            }

        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Creacion de usuario default master">
        DaoUsuario daoUsuario = new DaoUsuario();

        Usuario usuarioDB = daoUsuario.findFirst();
        if (usuarioDB == null) {
            usuarioDB = new Usuario();
            usuarioDB.setNombre("Administrador");
            usuarioDB.setCorreo("ubg700@gmail.com");
            usuarioDB.setTelefono("6671007264");
            usuarioDB.setContra("1234");
            daoUsuario.persist(usuarioDB);
        }
        //</editor-fold>        

        //<editor-fold defaultstate="collapsed" desc="Asignacion de perfil al usuario">
        DaoUsuariosPerfil daoUsuariosPerfil = new DaoUsuariosPerfil();
        if (daoUsuariosPerfil.findFirst() == null) {
            UsuariosPerfil usuariosPerfil = new UsuariosPerfil(usuarioDB.getId(), perfilMaster.getId());
            usuariosPerfil.setHereda(true);
            daoUsuariosPerfil.persist(usuariosPerfil);
        }

        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Asignaion de permisos al usuario">
        DaoUsuariosPermisos daoUsuariosPermisos = new DaoUsuariosPermisos();
        UsuariosPermisos usuariosPermisos;
        for (Permiso existingPermission : UtilsPermissions.getExistingPermissions()) {

            usuariosPermisos = new UsuariosPermisos(usuarioDB.getId(), existingPermission.getId());
            usuariosPermisos.setProfundidad(Profundidad.TODOS);

            UsuariosPermisos checkDB = new UsuariosPermisos(usuarioDB.getId(), existingPermission.getId());
            if (!daoUsuariosPermisos.stream().anyMatch(up -> up.equals(checkDB))) {
                daoUsuariosPermisos.persist(usuariosPermisos);
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Creacion de correo por default"> 
        DaoConfigMail daoMail = new DaoConfigMail();
        DaoCGConfig daoConfig = new DaoCGConfig();
        ConfigMail mail = daoMail.findFirst();
        if (mail == null) {
            mail = new ConfigMail();
            ConfigMail.AuthMail authMail = new ConfigMail.AuthMail("usuariosexpertos@gmail.com", "90Y8Byh$");
            mail.setAuth(authMail);
            mail.setHostName("smtp.googlemail.com");
            mail.setPort(465);
            mail.setSsl(true);
            mail = daoMail.persist(mail);

            System.out.println("Correo dado de alta por defecto:");
            System.out.println(mail);
        }
        //</editor-fold>                

        //<editor-fold defaultstate="collapsed" desc="Configuraciones generales">
        CGConfig configuracionGeneral = daoConfig.findFirst();

        if (configuracionGeneral == null) {
            configuracionGeneral = new CGConfig();
            //<editor-fold defaultstate="collapsed" desc="JWT CONFIGS">
            CGConfig.JwtsConfig jwtConfig = new CGConfig.JwtsConfig();
            jwtConfig.setSecondsRecoverJwtExp(1800); //30 minutos
            jwtConfig.setSecondsSessionJwtExp(7200); //12 horas
            configuracionGeneral.setJwtConfig(jwtConfig);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="LOGIN CONFIGS">
            CGConfig.AccessConfig loginAttemptConfig = new CGConfig.AccessConfig();

            loginAttemptConfig.setMaxNumberAttemps(10); // 10 intentos fallidos maximo
            loginAttemptConfig.setSecondsBetweenEvents(120); // 2 minutos de intervalo para contar
            loginAttemptConfig.setSecondsTermporalBlockingUser(1800); // bloqueado por media hora al sobrepasar intentos maximos
            loginAttemptConfig.setMaxPasswordRecords(3);

            configuracionGeneral.setAccessConfig(loginAttemptConfig);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="MAIL CONFIGS">
            CGConfig.MailsConfig mailsConfig = new CGConfig.MailsConfig();
            mailsConfig.setContactMailId(mail.getId()); //correo por defecto
            mailsConfig.setResetPasswordMailId(mail.getId()); //correo por defecto
            mailsConfig.setSupportMailId(mail.getId()); //correo por defecto
            configuracionGeneral.setMailConfig(mailsConfig);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="SMS CONFIGS">
            CGConfig.SMSConfig sMSConfig = new CGConfig.SMSConfig();
            sMSConfig.setDeviceImei("358424071611891");
            sMSConfig.setUri("http://201.163.30.113:45200/api/mensajes");
            sMSConfig.setUsuarioId("58bdbe0a043e44cfcdd0da24");
            configuracionGeneral.setSmsConfig(sMSConfig);
            //</editor-fold>

            daoConfig.persist(configuracionGeneral);

            System.out.println("configuracion generales instaladas:");
            System.out.println(configuracionGeneral);
        }
        //</editor-fold>        

        System.out.println("LA CONFIRUACION DE BASE DE DATOS DEFAULT TERMINÓ");
    }

    /**
     * generacion de permisos del sistema al arrancar despliegue
     */
    private void initDBPermissions() {
        //ESTRUCTURA DE ORGANIZACION DE SECCIONES
        // com.{nombre negocio}.services.{seccion}.{modulo}.{menu}        
        DaoPermiso daoPermiso = new DaoPermiso();        
        DaoMenu daoMenu = new DaoMenu();        
        DaoModulo daoModulo = new DaoModulo();
        
        DaoSeccion daoSeccion = new DaoSeccion();

        Package[] paquetes = Package.getPackages();
        List<String> paquetesServicios = Arrays.stream(paquetes)
                .map(p -> p.getName())
                .filter(name -> name.startsWith(PACKAGE_SERVICES))
                .collect(toList());

        //<editor-fold defaultstate="collapsed" desc="SECCIONES">
        Set<String> nombreSecciones = paquetesServicios.stream()
                .map(seccion -> seccion.split("\\.")[3])
                .collect(Collectors.toSet());

        for (String nombreSeccion : nombreSecciones) {
            try {
                String packageSeccionName = PACKAGE_SERVICES + "." + nombreSeccion;

                Seccion seccion = daoSeccion.findOne(packageSeccionName);
                if (seccion == null) {
                    seccion = new Seccion(packageSeccionName);
                    seccion.setNombre(nombreSeccion);
                    daoSeccion.persist(seccion);
                }

                List<String> modulosPackageNombre = Arrays.stream(paquetes)
                        .map(p -> p.getName())
                        .filter(name -> name.startsWith(packageSeccionName))
                        .collect(toList());

                List<String> modulosNombres = modulosPackageNombre.stream()
                        .filter(packageName -> !packageName.endsWith(nombreSeccion))
                        .map(n -> n.split("\\.")[4])
                        .collect(toList());
                //<editor-fold defaultstate="collapsed" desc="MODULOS">
                for (String nombreModulo : modulosNombres) {
                    if (nombreModulo.equals("commons") || nombreModulo.equals("generales")) { //omitir paquete commons
                        continue;
                    }
                    try {
                        String packageModuleName = packageSeccionName + "." + nombreModulo;

                        Modulo module = daoModulo.findOne(packageModuleName);
                        if (module == null) {
                            module = new Modulo(packageModuleName);
                            module.setNombre(nombreModulo);
                            module.setSeccion(seccion);
                            daoModulo.persist(module);
                        }

                        List<String> menusNames = getClasesSimpleNameFromPackage2(packageModuleName);

                        //<editor-fold defaultstate="collapsed" desc="MENUS">
                        for (String menusName : menusNames) {
                            try {
                                String packageClassName = packageModuleName + "." + menusName;

                                Menu menu = daoMenu.findOne(packageClassName);
                                if (menu == null) {
                                    menu = new Menu(packageClassName);
                                    menu.setNombre(this.generatePublicMenuName(menusName));
                                    menu.setModulo(module);
                                    daoMenu.persist(menu);
                                }

                                //<editor-fold defaultstate="collapsed" desc="ACCIONES">
                                List<Permiso> permisos = new ArrayList<>();
                                try {
                                    Class<?> clase = Class.forName(packageClassName);
                                    Method[] methods = clase.getDeclaredMethods();
                                    for (Method method : methods) {
                                        try {
                                            method.setAccessible(true);

                                            //para omitir repeticion
                                            if (permisos.stream()
                                                    .map(a -> a.getNombre())
                                                    .collect(toList())
                                                    .contains(method.getName())) {
                                                continue;
                                            }

                                            String permisoId = menu.getId() + "." + method.getName();
                                            Permiso permiso = daoPermiso.findOne(permisoId);

                                            if (permiso == null) {
                                                permiso = new Permiso(permisoId);
                                                permiso.setNombre(generatePublicMenuName(method.getName()));
                                                permisos.add(permiso);
                                                permiso.setMenu(menu);

                                                daoPermiso.persist(permiso);
                                            }

                                        } catch (Exception ex) {
                                            Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                } catch (Exception ex) {
                                    Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                //</editor-fold>
                            } catch (Exception ex) {
                                Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        //</editor-fold>                                                
                    } catch (Exception ex) {
                        Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //</editor-fold>
            } catch (Exception ex) {
                Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
            }
            //</editor-fold>
        }
    }

    private List<String> getClasesSimpleNameFromPackage2(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends ServiceFacade>> subtypes = reflections.getSubTypesOf(ServiceFacade.class);

        return subtypes.stream().map(c -> c.getSimpleName()).collect(toList());
    }

    private String generatePublicMenuName(String menuName) {
        String res = "" + Character.toUpperCase(menuName.charAt(0));
        for (int i = 1; i < menuName.length(); i++) {
            if (Character.isUpperCase(menuName.charAt(i))) {
                res += " " + menuName.charAt(i);
            } else {
                res += menuName.charAt(i);
            }
        }
        return res;
    }

}
