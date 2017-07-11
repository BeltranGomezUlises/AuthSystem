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

import com.machineAdmin.daos.cg.admin.DaoConfigMail;
import com.machineAdmin.daos.cg.admin.DaoUser;
import com.machineAdmin.entities.cg.admin.ConfigMail;
import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.managers.cg.ManagerFacade;
import static com.machineAdmin.utils.UtilsConfig.COLLECTION_NAME;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.mongojack.JacksonDBCollection;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class InitServletContext implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.initDBConfigs();

        this.initDBPermissions();

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    private void initDBPermissions() {
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.machineAdmin.managers"))));
        
        Set<Class<?>> clases = reflections.getSubTypesOf(Object.class);
        
        clases.forEach((clase) -> {
            if (clase.getSimpleName() != null || !clase.getSimpleName().equals("")) {
                System.out.println(clase.getSimpleName());
            }            
        });
    }

    private void initDBConfigs() {
        System.out.println("INICIANDO LA CONFIGURACION EN BASE DE DATOS DEFAULT");

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
        }
        //</editor-fold>                
        System.out.println("Correo dado de alta por defecto:");
        System.out.println(mail);

        //<editor-fold defaultstate="collapsed" desc="DAFULT USER CONFIG">
        DaoUser daoUser = new DaoUser();

        User userMaster = daoUser.findFirst();
        if (userMaster == null) {
            userMaster = new User();

            userMaster.setUser("Admin");
            userMaster.setMail("usuariosexpertos@gmail.com");
            userMaster.setPass(UtilsSecurity.cifrarMD5("1234"));

            daoUser.persist(userMaster);
        }
        //</editor-fold>
        System.out.println("Usuario dado de alta por defecto: (contraseña : \"1234\")");
        System.out.println(userMaster);

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
            UtilsConfig.CGConfig.LoginAttemptConfig loginAttemptConfig = new UtilsConfig.CGConfig.LoginAttemptConfig();

            loginAttemptConfig.setMaxNumberAttemps(10); // 10 intentos fallidos maximo
            loginAttemptConfig.setSecondsBetweenEvents(120); // 2 minutos de intervalo para contar
            loginAttemptConfig.setSecondsTermporalBlockingUser(1800); // bloqueado por media hora al sobrepasar intentos maximos
            config.setLoginConfig(loginAttemptConfig);
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
        }
        //</editor-fold>        
        System.out.println("configuracion generales instaladas:");
        System.out.println(config);
    }
}
