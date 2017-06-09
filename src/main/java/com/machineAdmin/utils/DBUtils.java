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

/**
 *
 * @author luisa
 */
public class DBUtils {

    //<editor-fold defaultstate="collapsed" desc="DESCRIPTION">
    /**
     * The MongoClient instance actually represents a pool of connections to the
     * database; you will only need one instance of class MongoClient even with multiple threads.
     * 
     */
//</editor-fold>        
    private static final String DATA_BASE_NAME = "machineAdmin";
    private static final MongoClientURI CONNECTION_STRING = new MongoClientURI("mongodb://localhost:27017");
    private static final MongoClient MONGO_CLIENT = new MongoClient(CONNECTION_STRING);    
    public static final MongoDatabase DB = MONGO_CLIENT.getDatabase(DATA_BASE_NAME);                
    
    public static DBCollection getCollection(String name){
        return MONGO_CLIENT.getDB(DATA_BASE_NAME).getCollection(name);
    }

}
