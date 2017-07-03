/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.machineAdmin.entities.cg.admin.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsJWT {

    //llave de encriptacion generada por instancia desplegada
    private static final Key KEY = MacProvider.generateKey();    
    
    //llave de encriptacion por text
    private static final String STRING_KEY = "LLAVE ULTRA SECRETA";
    
    public static String generateToken(User usuarioLogeado) throws JsonProcessingException {                        
        JwtBuilder builder = Jwts.builder();        
        Calendar cal = new GregorianCalendar();        //calendario de tiempos        
        builder.setIssuedAt(cal.getTime());     //fecha de expedicion        
        
        cal.add(Calendar.SECOND, UtilsConfig.getJwtExp()); //aumentar tiempo para asignar expiracion
        builder.setExpiration(cal.getTime());
        
        builder.setSubject(UtilsJson.jsonSerialize(usuarioLogeado)); //poner el sujeto en jwt
        
        return builder.signWith(SignatureAlgorithm.HS512, STRING_KEY).compact();
    }

    public static String generateResetToken(){
        JwtBuilder builder = Jwts.builder();        
        Calendar cal = new GregorianCalendar();        //calendario de tiempos                
        cal.add(Calendar.SECOND, 900);// 15 minutos //aumentar tiempo para asignar expiracion
        builder.setExpiration(cal.getTime());                        
        return builder.signWith(SignatureAlgorithm.HS512, STRING_KEY).compact();
    }
    
    public static String getBodyToken(String token) {
        return Jwts.parser().setSigningKey(STRING_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public static boolean isTokenValid(String token) {
        try {            
            //si no es un token valido lanzará SignaturaException
            Jws<Claims> jws = Jwts.parser().setSigningKey(STRING_KEY).parseClaimsJws(token);
            return !jws.getBody().getExpiration().before(new Date());
        } catch (SignatureException | IllegalArgumentException e) {
            return false;
        }
    }

}
