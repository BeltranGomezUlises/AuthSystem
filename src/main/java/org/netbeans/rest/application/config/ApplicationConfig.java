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
        resources.add(com.machineAdmin.services.administradores.juntas.Prueba.class);
        resources.add(com.machineAdmin.services.backoffice.cortes.ServiceMaquinitaSucursal.class);
        resources.add(com.machineAdmin.services.backoffice.maquinitas.ServiceMaquinita.class);
        resources.add(com.machineAdmin.services.cg.ServiceCommons.class);
        resources.add(com.machineAdmin.services.cg.admin.ServiceAccess.class);
        resources.add(com.machineAdmin.services.cg.admin.ServiceConfigMail.class);
        resources.add(com.machineAdmin.services.cg.admin.ServicePermission.class);
        resources.add(com.machineAdmin.services.cg.admin.ServiceUsers.class);
    }
    
}
