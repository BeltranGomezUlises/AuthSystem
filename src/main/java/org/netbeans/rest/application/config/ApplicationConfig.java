package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically populated with all resources defined in the project. If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.auth.services.Accesos.class);
        resources.add(com.auth.services.Commons.class);
        resources.add(com.auth.services.GruposPerfiles.class);
        resources.add(com.auth.services.Menus.class);
        resources.add(com.auth.services.Modulos.class);
        resources.add(com.auth.services.Perfiles.class);
        resources.add(com.auth.services.Permisos.class);
        resources.add(com.auth.services.Secciones.class);
        resources.add(com.auth.services.Usuarios.class);
        resources.add(com.auth.services.commons.ServiceFacade.class);
        resources.add(com.auth.services.commons.ServiceFacadeCRUD.class);
        resources.add(com.auth.services.commons.ServiceFacadeLCRD.class);
        resources.add(com.auth.services.commons.ServiceFacadeLRUD.class);
        resources.add(com.auth.services.commons.ServiceFaceLCRU.class);
    }

}
