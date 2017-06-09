/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luisa
 */
public class Utileria {
    
    public static final ObjectMapper MAPPER = new ObjectMapper();
    
    public static String jsonSerialize(Object o){
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Utileria.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
