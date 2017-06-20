/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import java.time.Instant;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsJWT {

    private static final Key KEY = MacProvider.generateKey();    
    private static final String STRING_KEY = "LLAVE ULTRA SECRETA";
    
    public static String generateToken() {        
        return Jwts.builder().setSubject(Instant.now().toString()).signWith(SignatureAlgorithm.HS512, STRING_KEY).compact();
    }

    public static String getBodyToken(String token) {
        return Jwts.parser().setSigningKey(STRING_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public static boolean isTokenValid(String token) {
        try {            
            Jwts.parser().setSigningKey(STRING_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            return false;
        }
    }

}
