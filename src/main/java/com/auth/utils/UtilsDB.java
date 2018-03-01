package com.auth.utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.jinq.jpa.JinqJPAStreamProvider;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsDB {

    //<editor-fold defaultstate="collapsed" desc="JPA utils">
    /*
        the jpa clients are defined here,
        you need to add the factories and streams providers
        for each persistence unit you nedd
     */
    //</editor-fold>
    private static EntityManagerFactory eMFactoryCG;
    private static EntityManagerFactory eMFactoryMachineAdmin;
    private static JinqJPAStreamProvider streamProviderCG;

    /**
     * PERSISTENCE UNIT NAMES
     */
    private static final String CG_UNIT_NAME = "auth";

    //<editor-fold defaultstate="collapsed" desc="Fabricas y proveedores">
    /**
     * metodo fábrica de manejadores de entidad de de la base de datos "cg"
     *
     * @return entityManagerFactory de la conexion a la base de datos CG
     */
    public static EntityManagerFactory getEMFactoryCG() {
        if (eMFactoryCG == null) {
            eMFactoryCG = Persistence.createEntityManagerFactory(CG_UNIT_NAME);
        }
        return eMFactoryCG;
    }

}
