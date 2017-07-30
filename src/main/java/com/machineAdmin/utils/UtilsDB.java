/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.utils;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.jinq.jpa.JinqJPAStreamProvider;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsDB {

    //<editor-fold defaultstate="collapsed" desc="mongo utils">
    /**
     * The MongoClient instance actually represents a pool of connections to the
     * database; you will only need one instance of class MongoClient even with
     * multiple threads.
     *
     */
    //</editor-fold>        
    private static final String CG_DATA_BASE_NAME = "cg";
    //private static final MongoClientURI CONNECTION_STRING = new MongoClientURI("mongodb://admin:mongo.90Y9B8yh$@192.168.10.8:27170/admin");
    private static final MongoClientURI CG_CONNECTION_STRING = new MongoClientURI("mongodb://localhost:27017");
    private static final MongoClient CG_MONGO_CLIENT = new MongoClient(CG_CONNECTION_STRING);
    public static final MongoDatabase CG_DB = CG_MONGO_CLIENT.getDatabase(CG_DATA_BASE_NAME);

    public static DBCollection getCGCollection(String name) {        
        return CG_MONGO_CLIENT.getDB(CG_DATA_BASE_NAME).getCollection(name);
    }
    
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
    private static final String CG_UNIT_NAME = "cg";
    private static final String MACHINE_ADMIN_UNIT_NAME = "machineAdmin";
        
    
    /**
     * fabricas y proveedores
     * @return 
     */
    public static EntityManagerFactory getEMFactoryCG() {
        if (eMFactoryCG == null) {
            eMFactoryCG = Persistence.createEntityManagerFactory(CG_UNIT_NAME);
        }
        return eMFactoryCG;
    }
    
    public static EntityManagerFactory getEMFactoryMachineAdmin(){
        if (eMFactoryMachineAdmin == null) {
            eMFactoryMachineAdmin = Persistence.createEntityManagerFactory(MACHINE_ADMIN_UNIT_NAME);
        }
        return eMFactoryMachineAdmin;
    }
    
    public static JinqJPAStreamProvider getCGStreamProvider() {
        if (streamProviderCG == null) {
            streamProviderCG = new JinqJPAStreamProvider(getEMFactoryCG());
        }
        return streamProviderCG;
    }
               
}
