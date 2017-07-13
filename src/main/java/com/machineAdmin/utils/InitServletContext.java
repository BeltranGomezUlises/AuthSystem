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
import com.machineAdmin.daos.cg.admin.DaoConfigMail;
import com.machineAdmin.daos.cg.admin.DaoPermission;
import com.machineAdmin.daos.cg.admin.DaoUser;
import com.machineAdmin.entities.cg.admin.ConfigMail;
import com.machineAdmin.entities.cg.admin.Permission;
import com.machineAdmin.entities.cg.admin.Permission.PermissionType;
import com.machineAdmin.entities.cg.admin.Permission.Seccion;
import com.machineAdmin.entities.cg.admin.Permission.Seccion.Module;
import com.machineAdmin.entities.cg.admin.Permission.Seccion.Module.Menu;
import com.machineAdmin.entities.cg.admin.Permission.Seccion.Module.Menu.Action;
import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.services.cg.ServiceFacade;
import static com.machineAdmin.utils.UtilsConfig.COLLECTION_NAME;
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
import org.mongojack.JacksonDBCollection;
import org.reflections.Reflections;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class InitServletContext implements ServletContextListener {

    private final String PACKAGE_SERVICES = "com.machineAdmin.services";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.initDBConfigs();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    private void initDBPermissions() {

        //ESTRUCTURAD DE ORGANIZACION DE SECCIONES
        // com.{nombre negocio}.services.{seccion}.{modulo}.{menu}                
        Permission permission = new Permission();
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
            if (!nombreSeccion.equals("cg")) {
                Seccion s = new Seccion(nombreSeccion);

                String packageSeccionName = PACKAGE_SERVICES + "." + nombreSeccion;

                List<Module> modulos = new ArrayList<>();
                List<String> modulosPackageNombre = Arrays.stream(paquetes)
                        .map(p -> p.getName())
                        .filter(name -> name.startsWith(packageSeccionName))
                        .collect(toList());

                List<String> modulosNombres = modulosPackageNombre.stream()
                        .map(n -> n.split("\\.")[4])
                        .collect(toList());
                //<editor-fold defaultstate="collapsed" desc="MODULOS">
                for (String nombreModulo : modulosNombres) {
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

                                List<PermissionType> types = new ArrayList<>();
                                types.add(PermissionType.OWNER);

                                if (!action.getName().equals("post")) {
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
                s.setModulos(modulos);
                secciones.add(s);
            }
        }
        //</editor-fold>
        permission.setSecciones(secciones);

        //<editor-fold defaultstate="collapsed" desc="ACTUALIZAR DB">
        //actualizar catalogo de permisos disponibles
        DaoPermission daoPermission = new DaoPermission();
        Permission dbPermission = daoPermission.findFirst();
        
        if (dbPermission == null) {
            daoPermission.persist(permission);
        } else {
            daoPermission.update(DBQuery.is("_id", dbPermission.getId()), permission);            
        }
        //</editor-fold>

        System.out.println("PERMISSION GENERATED");
        try {
            System.out.println(UtilsJson.jsonSerialize(permission));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(InitServletContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initDBConfigs() {
        System.out.println("INICIANDO LA CONFIGURACION EN BASE DE DATOS DEFAULT");
        
        //<editor-fold defaultstate="collapsed" desc="DEFAULT PERMISSIONS CONFIG"> 
        this.initDBPermissions();        
        //AGREGAR LOS PERMISOS AL USUARIO MASTER
        //</editor-fold>
                
        //<editor-fold defaultstate="collapsed" desc="DEFAULT MAIL CONFIGS">
        DaoConfigMail daoMail = new DaoConfigMail();
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

        //<editor-fold defaultstate="collapsed" desc="DAFULT USER CONFIG">
        DaoUser daoUser = new DaoUser();
        DaoPermission daoPermission = new DaoPermission();
        
        User userMaster = daoUser.findFirst();                
        if (userMaster == null) {
            userMaster = new User();

            userMaster.setUser("Admin");
            userMaster.setMail("usuariosexpertos@gmail.com");
            userMaster.setPass(UtilsSecurity.cifrarMD5("1234"));
            userMaster.setPermissions(daoPermission.findFirst());
            
            daoUser.persist(userMaster);

            System.out.println("Usuario dado de alta por defecto: (contraseña : \"1234\")");
            System.out.println(userMaster);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="GENERAL CONFIGS">
        JacksonDBCollection<UtilsConfig.CGConfig, String> collection = JacksonDBCollection.wrap(UtilsDB.getCollection(COLLECTION_NAME), UtilsConfig.CGConfig.class, String.class);
        UtilsConfig.CGConfig config = collection.findOne();
        if (config == null) {
            config = new UtilsConfig.CGConfig();
            //<editor-fold defaultstate="collapsed" desc="JWT CONFIGS">
            UtilsConfig.CGConfig.JwtsConfig jwtConfig = new UtilsConfig.CGConfig.JwtsConfig();
            jwtConfig.setSecondsRecoverJwtExp(900); //15 minutos
            jwtConfig.setSecondsSessionJwtExp(3600); //1 hora
            config.setJwtConfig(jwtConfig);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="LOGIN CONFIGS">
            UtilsConfig.CGConfig.AccessConfig loginAttemptConfig = new UtilsConfig.CGConfig.AccessConfig();

            loginAttemptConfig.setMaxNumberAttemps(10); // 10 intentos fallidos maximo
            loginAttemptConfig.setSecondsBetweenEvents(120); // 2 minutos de intervalo para contar
            loginAttemptConfig.setSecondsTermporalBlockingUser(1800); // bloqueado por media hora al sobrepasar intentos maximos
            loginAttemptConfig.setMaxPasswordRecords(3);

            config.setAccessConfig(loginAttemptConfig);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="MAIL CONFIGS">
            UtilsConfig.CGConfig.MailsConfig mailsConfig = new UtilsConfig.CGConfig.MailsConfig();
            mailsConfig.setContactMailId(mail.getId()); //correo por defecto
            mailsConfig.setResetPasswordMailId(mail.getId()); //correo por defecto
            mailsConfig.setSupportMailId(mail.getId()); //correo por defecto
            config.setMailConfig(mailsConfig);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="SMS CONFIGS">
            UtilsConfig.CGConfig.SMSConfig sMSConfig = new UtilsConfig.CGConfig.SMSConfig();
            sMSConfig.setDeviceImei("358424071611891");
            sMSConfig.setUri("http://201.163.30.113:45200/api/mensajes");
            sMSConfig.setUsuarioId("58bdbe0a043e44cfcdd0da24");
            config.setSmsConfig(sMSConfig);
            //</editor-fold>

            collection.insert(config);

            System.out.println("configuracion generales instaladas:");
            System.out.println(config);
        }
        //</editor-fold>        

    }

    private List<String> getClasesSimpleNameFromPackage2(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends ServiceFacade>> subtypes = reflections.getSubTypesOf(ServiceFacade.class);

        return subtypes.stream().map(c -> c.getSimpleName()).collect(toList());
    }

}
