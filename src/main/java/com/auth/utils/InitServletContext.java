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
package com.auth.utils;

import com.auth.daos.admin.DaoGrupoPerfiles;
import com.auth.entities.admin.GrupoPerfiles;
import com.auth.entities.admin.Menu;
import com.auth.entities.admin.Modulo;
import com.auth.entities.admin.Perfil;
import com.auth.entities.admin.Permiso;
import com.auth.entities.admin.Seccion;
import com.auth.entities.admin.Usuario;
import com.auth.entities.admin.UsuariosPerfil;
import com.auth.daos.admin.DaoMenu;
import com.auth.daos.admin.DaoModulo;
import com.auth.daos.admin.DaoPerfil;
import com.auth.daos.admin.DaoPermiso;
import com.auth.daos.admin.DaoSeccion;
import com.auth.daos.admin.DaoUsuario;
import com.auth.daos.admin.DaoUsuariosPerfil;
import com.auth.entities.admin.PerfilesPermisos;
import com.auth.entities.admin.UsuariosPermisos;
import com.auth.entities.commons.Profundidad;
import static com.auth.entities.commons.Profundidad.TODOS;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
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
     * inicio de configuración del sistema en base de datos
     *
     * @throws Exception
     */
    private void initDBConfigs() throws Exception {
        System.out.println("INICIANDO LA CONFIGURACION EN BASE DE DATOS DEFAULT");

        //<editor-fold defaultstate="collapsed" desc="Creacion de usuario default master">
        DaoUsuario daoUsuario = new DaoUsuario();

        Usuario usuarioDB;
        try {
            usuarioDB = daoUsuario.stream().where(u -> u.getNombre().equals("Administrador")).findFirst().get();
        } catch (NoSuchElementException e) {
            usuarioDB = new Usuario();
            usuarioDB.setNombre("Administrador");
            usuarioDB.setCorreo("ubg700@gmail.com");
            usuarioDB.setTelefono("6671007264");
            usuarioDB.setContra(UtilsSecurity.cifrarMD5("1234"));
            daoUsuario.persist(usuarioDB);
            daoUsuario.update(usuarioDB);
        }

        //</editor-fold>  
        
        //<editor-fold defaultstate="collapsed" desc="Creacion del perfil Master">
        //crear perfil master
        DaoPerfil daoPerfil = new DaoPerfil();

        Perfil perfilMaster;
        try {
            perfilMaster = daoPerfil.stream().where(p -> p.getNombre().equals("Master")).findFirst().get();
        } catch (NoSuchElementException e) {
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
            gp.setDescripcion("Este grupo de roles puede administrar las configuraciones del sistemas, ademas de poder realizar las operaciones de negocio de toda la aplicacion");

            List<Perfil> perfilesDelRol = new ArrayList<>();
            perfilesDelRol.add(perfilMaster);
            gp.setPerfilList(perfilesDelRol);            
            daoGrupoPerfil.persist(gp);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Asignacion de permisos al perfil">        
        for (Permiso permiso : UtilsPermissions.getExistingPermissions()) {
            PerfilesPermisos perfilesPermisosRelacion = new PerfilesPermisos(perfilMaster.getId(), permiso.getId());
            perfilesPermisosRelacion.setProfundidad(Profundidad.TODOS);
            if (!perfilMaster.getPerfilesPermisosList().contains(perfilesPermisosRelacion)) {
                perfilMaster.getPerfilesPermisosList().add(perfilesPermisosRelacion);
            }
        }
        daoPerfil.update(perfilMaster);
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
        for (Permiso existingPermission : UtilsPermissions.getExistingPermissions()) {
            UsuariosPermisos usuariosPermisosRelacion = new UsuariosPermisos(usuarioDB.getId(), existingPermission.getId());
            usuariosPermisosRelacion.setProfundidad(TODOS);
            if (!usuarioDB.getUsuariosPermisosList().contains(usuariosPermisosRelacion)) {
                usuarioDB.getUsuariosPermisosList().add(usuariosPermisosRelacion);
            }
        }
        daoUsuario.update(usuarioDB);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Creacion de correo por default"> 
//        DaoConfigMail daoMail = new DaoConfigMail();
//        DaoCGConfig daoConfig = new DaoCGConfig();
//        ConfigMail mail = daoMail.findFirst();
//        if (mail == null) {
//            mail = new ConfigMail();
//            ConfigMail.AuthMail authMail = new ConfigMail.AuthMail("usuariosexpertos@gmail.com", "usuarios$1234");
//            mail.setAuth(authMail);
//            mail.setHostName("smtp.googlemail.com");
//            mail.setPort(465);
//            mail.setSsl(true);
//            mail = daoMail.persist(mail);
//
//            System.out.println("Correo dado de alta por defecto:");
//            System.out.println(mail);
//        }
        //</editor-fold>                

//        //<editor-fold defaultstate="collapsed" desc="Configuraciones generales">
//        CGConfig configuracionGeneral = daoConfig.findFirst();
//
//        if (configuracionGeneral == null) {
//            configuracionGeneral = new CGConfig();
//            //<editor-fold defaultstate="collapsed" desc="JWT CONFIGS">
//            CGConfig.JwtsConfig jwtConfig = new CGConfig.JwtsConfig();
//            jwtConfig.setSecondsRecoverJwtExp(1800); //30 minutos
//            jwtConfig.setSecondsSessionJwtExp(7200); //12 horas
//            configuracionGeneral.setJwtConfig(jwtConfig);
//            //</editor-fold>
//
//            //<editor-fold defaultstate="collapsed" desc="LOGIN CONFIGS">
//            CGConfig.AccessConfig loginAttemptConfig = new CGConfig.AccessConfig();
//
//            loginAttemptConfig.setMaxNumberAttemps(10); // 10 intentos fallidos maximo
//            loginAttemptConfig.setSecondsBetweenEvents(120); // 2 minutos de intervalo para contar
//            loginAttemptConfig.setSecondsTermporalBlockingUser(1800); // bloqueado por media hora al sobrepasar intentos maximos
//            loginAttemptConfig.setMaxPasswordRecords(3);
//
//            configuracionGeneral.setAccessConfig(loginAttemptConfig);
//            //</editor-fold>
//
//            //<editor-fold defaultstate="collapsed" desc="MAIL CONFIGS">
//            CGConfig.MailsConfig mailsConfig = new CGConfig.MailsConfig();
//            mailsConfig.setContactMailId(mail.getId()); //correo por defecto
//            mailsConfig.setResetPasswordMailId(mail.getId()); //correo por defecto
//            mailsConfig.setSupportMailId(mail.getId()); //correo por defecto
//            configuracionGeneral.setMailConfig(mailsConfig);
//            //</editor-fold>
//
//            //<editor-fold defaultstate="collapsed" desc="SMS CONFIGS">
//            CGConfig.SMSConfig sMSConfig = new CGConfig.SMSConfig();
//            sMSConfig.setDeviceImei("358424071611891");
//            sMSConfig.setUri("http://201.163.30.113:45200/api/mensajes");
//            sMSConfig.setUsuarioId("58bdbe0a043e44cfcdd0da24");
//            configuracionGeneral.setSmsConfig(sMSConfig);
//            //</editor-fold>
//
//            //<editor-fold defaultstate="collapsed" desc="BITACORAS CONFIG">
//            BitacorasConfig bitacorasConfig = new BitacorasConfig();
//            bitacorasConfig.setMesesAPersistir(6);
//            configuracionGeneral.setBitacorasConfig(bitacorasConfig);
//
//            //</editor-fold>
//            daoConfig.persist(configuracionGeneral);
//
//            System.out.println("configuracion generales instaladas:");
//            System.out.println(configuracionGeneral);
//        }
//        //</editor-fold>        

        System.out.println("LA CONFIRUACION DE BASE DE DATOS DEFAULT TERMINÓ");
    }

}
