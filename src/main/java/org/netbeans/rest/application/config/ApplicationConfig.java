package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
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
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.machineAdmin.services.backoffice.maquinitas.ServiceMaquinita.class);
        resources.add(com.machineAdmin.services.cg.administracion.Accesos.class);
        resources.add(com.machineAdmin.services.cg.administracion.Correos.class);
        resources.add(com.machineAdmin.services.cg.administracion.Perfiles.class);
        resources.add(com.machineAdmin.services.cg.administracion.Permisos.class);
        resources.add(com.machineAdmin.services.cg.administracion.Usuarios.class);
        resources.add(com.machineAdmin.services.cg.commons.ServiceCommons.class);
        resources.add(com.machineAdmin.services.cg.commons.ServiceFacade.class);
    }
    
}
