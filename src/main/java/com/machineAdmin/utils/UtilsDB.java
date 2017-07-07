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
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.bson.types.ObjectId;
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
    private static final String DATA_BASE_NAME = "machineAdmin";
    //private static final MongoClientURI CONNECTION_STRING = new MongoClientURI("mongodb://admin:mongo.90Y9B8yh$@192.168.10.8:27170/admin");
    private static final MongoClientURI CONNECTION_STRING = new MongoClientURI("mongodb://localhost:27017");
    private static final MongoClient MONGO_CLIENT = new MongoClient(CONNECTION_STRING);
    public static final MongoDatabase DB = MONGO_CLIENT.getDatabase(DATA_BASE_NAME);

    public static DBCollection getCollection(String name) {
        return MONGO_CLIENT.getDB(DATA_BASE_NAME).getCollection(name);
    }
    
    //<editor-fold defaultstate="collapsed" desc="JPA utils">
    /*
        the jpa clients are defined here,
        you need to add the factories and streams providers
        for each persistence unit you nedd
     */
    //</editor-fold>
    private static EntityManagerFactory EMFactoryPostgres;
    private static JinqJPAStreamProvider streamProviderPostgres;
    
    /**
     * PERSISTENCE UNIT NAMES
     */
    private static final String POSTGRES_UNIT_NAME = "postgres-unit";

    public static EntityManagerFactory getEMFactoryPostgres() {
        if (EMFactoryPostgres == null) {
            EMFactoryPostgres = Persistence.createEntityManagerFactory(POSTGRES_UNIT_NAME);
        }
        return EMFactoryPostgres;
    }

    public static JinqJPAStreamProvider getStreamProviderPostgres() {
        if (streamProviderPostgres == null) {
            streamProviderPostgres = new JinqJPAStreamProvider(getEMFactoryPostgres());
        }
        return streamProviderPostgres;
    }

}
