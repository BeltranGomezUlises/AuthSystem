package com.auth.utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class UtilsDB {

    private static EntityManagerFactory eMFactoryCG;
    private static final String UNIT_NAME = "auth";

    /**
     * metodo fábrica de manejadores de entidad de de la base de datos "auth"
     *
     * @return entityManagerFactory de la conexion a la base de datos auth
     */
    public static EntityManagerFactory getEMFactory() {
        if (eMFactoryCG == null) {
            eMFactoryCG = Persistence.createEntityManagerFactory(UNIT_NAME);
        }
        return eMFactoryCG;
    }

}
