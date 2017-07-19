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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.machineAdmin.daos.cg.admin.DaoConfig;
import com.machineAdmin.daos.cg.admin.DaoConfigMail;
import com.machineAdmin.daos.cg.admin.DaoPermission;
import com.machineAdmin.daos.cg.admin.DaoUser;
import com.machineAdmin.entities.cg.admin.CGConfig;
import com.machineAdmin.entities.cg.admin.ConfigMail;
import com.machineAdmin.entities.cg.admin.AvailablePermission;
import com.machineAdmin.entities.cg.admin.AvailablePermission.Seccion;
import com.machineAdmin.entities.cg.admin.AvailablePermission.Seccion.Module;
import com.machineAdmin.entities.cg.admin.AvailablePermission.Seccion.Module.Menu;
import com.machineAdmin.entities.cg.admin.AvailablePermission.Seccion.Module.Menu.Action;
import com.machineAdmin.entities.cg.admin.Profile;
import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.managers.cg.admin.ManagerProfile;
import com.machineAdmin.models.cg.ModelAsignedUser;
import com.machineAdmin.models.cg.enums.PermissionType;
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
import org.mongojack.DBQuery;
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

    private void initDBPermissions() {

        //ESTRUCTURAD DE ORGANIZACION DE SECCIONES
        // com.{nombre negocio}.services.{seccion}.{modulo}.{menu}                
        AvailablePermission permission = new AvailablePermission();
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
            Seccion seccion = new Seccion(nombreSeccion);

            String packageSeccionName = PACKAGE_SERVICES + "." + nombreSeccion;

            List<Module> modulos = new ArrayList<>();
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
                if (nombreModulo.equals("commons")) {
                    continue;
                }

                Module module = new Module(nombreModulo);

                String packageModuleName = packageSeccionName + "." + nombreModulo;

                List<String> menusNames = getClasesSimpleNameFromPackage2(packageModuleName);
                List<Menu> menus = new ArrayList<>();
                //<editor-fold defaultstate="collapsed" desc="MENUS">
                for (String menusName : menusNames) {
                    Menu menu = new Menu(menusName);

                    String packageClassName = packageModuleName + "." + menusName;

                    //<editor-fold defaultstate="collapsed" desc="ACCIONES">
                    List<Action> acciones = new ArrayList<>();
                    try {
                        Class<?> clase = Class.forName(packageClassName);
                        Method[] methods = clase.getDeclaredMethods();
                        for (Method method : methods) {
                            method.setAccessible(true);

                            if (acciones.stream()
                                    .map(a -> a.getName())
                                    .collect(toList())
                                    .contains(method.getName())) {
                                continue;
                            }

                            Action action = new Action();
                            action.setName(method.getName());
                            String actionId = seccion.getName() + "." + module.getName() + "." + menu.getName() + "." + action.getName();
                            action.setId(actionId);

                            List<PermissionType> types = new ArrayList<>();
                            types.add(PermissionType.OWNER);

                            if (!action.getName().equals("alta")) {
                                types.add(PermissionType.ALL);
                                types.add(PermissionType.OWNER_AND_PROFILE);
                            }
                            action.setTypes(types);
                            acciones.add(action);
                        }
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
                        continue;
                    }
                    menu.setAcciones(acciones);
                    menus.add(menu);
                    //</editor-fold>
                }
                //</editor-fold>
                module.setMenus(menus);
                modulos.add(module);
            }
            //</editor-fold>
            seccion.setModulos(modulos);
            secciones.add(seccion);

        }
        permission.setSecciones(secciones);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="ACTUALIZAR DB">
        //actualizar catalogo de permisos disponibles
        DaoPermission daoPermission = new DaoPermission();
        AvailablePermission dbPermission = daoPermission.findFirst();

        if (dbPermission == null) {
            daoPermission.persist(permission);
        } else {
            try {
                dbPermission.setSecciones(secciones);
                daoPermission.update(DBQuery.exists("_id"), dbPermission);
            } catch (Exception ex) {
                Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //</editor-fold>

        System.out.println("PERMISSION GENERATED");
        try {
            System.out.println(UtilsJson.jsonSerialize(permission));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initDBConfigs() throws Exception {
        System.out.println("INICIANDO LA CONFIGURACION EN BASE DE DATOS DEFAULT");

        //<editor-fold defaultstate="collapsed" desc="DEFAULT PERMISSIONS CONFIG"> 
        this.initDBPermissions();
        //AGREGAR LOS PERMISOS AL USUARIO MASTER
        //</editor-fold>

        DaoConfigMail daoMail = new DaoConfigMail();
        DaoUser daoUser = new DaoUser();
        DaoConfig daoConfig = new DaoConfig();

        //<editor-fold defaultstate="collapsed" desc="DEFAULT MAIL CONFIGS">        
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

        //<editor-fold defaultstate="collapsed" desc="DAFULT USER CONFIG AND PROFILE">        
        DaoPermission daoPermission = new DaoPermission();

        User userMaster = daoUser.findFirst();
        if (userMaster == null) {

            //crear el perfil default de master
            Profile profile = new Profile();
            profile.setName("Master");
            profile.setPermisos(UtilsPermissions.getAsignedPermissionsAvailable());

            userMaster = new User();
            userMaster.setUser("Admin");
            userMaster.setMail("usuariosexpertos@gmail.com");
            userMaster.setPass(UtilsSecurity.cifrarMD5("1234"));
            userMaster.setAsignedPermissions(profile.getPermisos());
            userMaster = daoUser.persist(userMaster); //obtener el id de base de datos

            ManagerProfile managerProfile = new ManagerProfile();

            ModelAsignedUser modelAsignedUser = new ModelAsignedUser();
            modelAsignedUser.setHeritage(true);
            modelAsignedUser.setUserId(userMaster.getId());

            profile.setUsers(Arrays.asList(modelAsignedUser));  //asignar el usuario al perfil            
            managerProfile.persist(profile); //persistir el perfil con los permisos y el usuario default

            System.out.println("Usuario dado de alta por defecto: (contraseña : \"1234\")");
            System.out.println(userMaster);
            System.out.println("Perfil dado de alta por defecto: " + profile.getName());

        } else {
            userMaster.setAsignedPermissions(UtilsPermissions.getAsignedPermissionsAvailable());
            daoUser.update(userMaster);
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
