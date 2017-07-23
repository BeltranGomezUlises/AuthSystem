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

import com.machineAdmin.daos.cg.admin.mongo.DaoConfig;
import com.machineAdmin.daos.cg.admin.mongo.DaoConfigMail;
import com.machineAdmin.daos.cg.admin.postgres.DaoPermiso;
import com.machineAdmin.daos.cg.exceptions.ConstraintException;
import com.machineAdmin.daos.cg.exceptions.SQLPersistenceException;
import com.machineAdmin.entities.cg.admin.mongo.CGConfig;
import com.machineAdmin.entities.cg.admin.mongo.ConfigMail;
import com.machineAdmin.entities.cg.admin.postgres.Menu;
import com.machineAdmin.entities.cg.admin.postgres.Modulo;
import com.machineAdmin.entities.cg.admin.postgres.Permiso;
import com.machineAdmin.entities.cg.admin.postgres.Seccion;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.managers.cg.admin.postgres.ManagerMenu;
import com.machineAdmin.managers.cg.admin.postgres.ManagerModulo;
import com.machineAdmin.managers.cg.admin.postgres.ManagerPermiso;
import com.machineAdmin.managers.cg.admin.postgres.ManagerSeccion;
import com.machineAdmin.managers.cg.admin.postgres.ManagerUsuario;
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

    //<editor-fold defaultstate="collapsed" desc="Generacion de permisos para la app">
    private void initDBPermissions() {
        //ESTRUCTURA DE ORGANIZACION DE SECCIONES
        // com.{nombre negocio}.services.{seccion}.{modulo}.{menu}
        ManagerPermiso managerPermiso = new ManagerPermiso();
        ManagerMenu managerMenu = new ManagerMenu();
        ManagerModulo managerModulo = new ManagerModulo();
        ManagerSeccion managerSeccion = new ManagerSeccion();

        Package[] paquetes = Package.getPackages();
        List<String> paquetesServicios = Arrays.stream(paquetes)
                .map(p -> p.getName())
                .filter(name -> name.startsWith(PACKAGE_SERVICES))
                .collect(toList());

        //<editor-fold defaultstate="collapsed" desc="SECCIONES">
        Set<String> nombreSecciones = paquetesServicios.stream()
                .map(seccion -> seccion.split("\\.")[3])
                .collect(Collectors.toSet());

        List<Seccion> secciones = new ArrayList<>();

        for (String nombreSeccion : nombreSecciones) {
            try {
                String packageSeccionName = PACKAGE_SERVICES + "." + nombreSeccion;
                Seccion seccion = new Seccion(packageSeccionName);
                seccion.setNombre(nombreSeccion);

                managerSeccion.persist(seccion);

                List<Modulo> modulos = new ArrayList<>();
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
                    if (nombreModulo.equals("commons")) { //omitir paquete commons
                        continue;
                    }
                    try {
                        String packageModuleName = packageSeccionName + "." + nombreModulo;
                        Modulo module = new Modulo(packageModuleName);
                        module.setNombre(nombreModulo);
                        module.setSeccion(seccion);
                        managerModulo.persist(module);

                        List<String> menusNames = getClasesSimpleNameFromPackage2(packageModuleName);
                        List<Menu> menus = new ArrayList<>();
                        //<editor-fold defaultstate="collapsed" desc="MENUS">
                        for (String menusName : menusNames) {
                            try {
                                String packageClassName = packageModuleName + "." + menusName;

                                Menu menu = new Menu(packageClassName);
                                menu.setNombre(menusName);
                                menu.setModulo(module);

                                managerMenu.persist(menu);

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

                                            Permiso permiso = new Permiso();
                                            permiso.setNombre(method.getName());
                                            permiso.setId(menu.getId() + "." + permiso.getNombre());
                                            permisos.add(permiso);
                                            permiso.setMenu(menu);

                                            managerPermiso.persist(permiso);

                                        } catch (SQLPersistenceException | ConstraintException ex) {
                                            Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
                                    continue;
                                }
                                menu.setPermisoList(permisos);
                                menus.add(menu);
                                //</editor-fold>
                            } catch (SQLPersistenceException | ConstraintException ex) {
                                Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        //</editor-fold>                        
                        module.setMenuList(menus);
                        modulos.add(module);
                    } catch (SQLPersistenceException | ConstraintException ex) {
                        Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //</editor-fold>
                seccion.setModuloList(modulos);
                secciones.add(seccion);
            } //</editor-fold>
            catch (SQLPersistenceException | ConstraintException ex) {
                Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //<editor-fold defaultstate="collapsed" desc="ACTUALIZAR DB">
        //actualizar catalogo de permisos disponibles
        for (Seccion seccion : secciones) {
            for (Modulo modulo : seccion.getModuloList()) {

                for (Menu menu : modulo.getMenuList()) {
                    for (Permiso permiso : menu.getPermisoList()) {

                    }

                }

            }

        }
        //</editor-fold>

        System.out.println("PERMISSION GENERATED");
        System.out.println(Arrays.toString(secciones.toArray()));

    }

    //</editor-fold>
    private void initDBConfigs() throws Exception {
        System.out.println("INICIANDO LA CONFIGURACION EN BASE DE DATOS DEFAULT");

        //<editor-fold defaultstate="collapsed" desc="DEFAULT PERMISSIONS CONFIG"> 
        this.initDBPermissions();
        //AGREGAR LOS PERMISOS AL USUARIO MASTER
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="DEFAULT MAIL CONFIGS"> 
        DaoConfigMail daoMail = new DaoConfigMail();
        DaoConfig daoConfig = new DaoConfig();
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

        //<editor-fold defaultstate="collapsed" desc="USER CONFIG DEFAULT">
        ManagerUsuario managerUsuario = new ManagerUsuario();

        Usuario usuarioDB = managerUsuario.findFirst();
        if (usuarioDB == null) {
            Usuario usuarioDefault = new Usuario();
            usuarioDefault.setNombre("Administrador");
            usuarioDefault.setCorreo("ubg700@gmail.com");
            usuarioDefault.setTelefono("6671007264");
            usuarioDefault.setContra("1234");
            managerUsuario.persist(usuarioDefault);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="GENERAL CONFIGS">
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

    }

    private List<String> getClasesSimpleNameFromPackage2(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends ServiceFacade>> subtypes = reflections.getSubTypesOf(ServiceFacade.class);

        return subtypes.stream().map(c -> c.getSimpleName()).collect(toList());
    }

}
