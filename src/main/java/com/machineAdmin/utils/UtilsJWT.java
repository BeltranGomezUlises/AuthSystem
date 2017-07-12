/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.managers.cg.exceptions.ParametroInvalidoException;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.models.cg.ModelRecoverCodeUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.io.IOException;
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

    public static String generateSessionToken(String userId) throws JsonProcessingException {
        JwtBuilder builder = Jwts.builder();
        Calendar cal = new GregorianCalendar();        //calendario de tiempos        
        builder.setIssuedAt(cal.getTime());     //fecha de expedicion        

        cal.add(Calendar.SECOND, UtilsConfig.getSecondsSessionJwtExp()); //aumentar tiempo para asignar expiracion
        builder.setExpiration(cal.getTime());

        builder.setSubject(userId); //poner el sujeto en jwt

        return builder.signWith(SignatureAlgorithm.HS512, STRING_KEY).compact();
    }

    public static String generateValidateUserToken(ModelRecoverCodeUser model) throws JsonProcessingException {
        JwtBuilder builder = Jwts.builder();
        Calendar cal = new GregorianCalendar();        //calendario de tiempos                
        cal.add(Calendar.SECOND, UtilsConfig.getSecondsRecoverJwtExp());
        builder.setExpiration(cal.getTime());
        builder.setSubject(UtilsJson.jsonSerialize(model));

        return builder.signWith(SignatureAlgorithm.HS512, STRING_KEY).compact();
    }
    
    public static String generateTokenResetPassword(String token, String code) throws IOException, ParametroInvalidoException{
        JwtBuilder builder = Jwts.builder();
        Calendar cal = new GregorianCalendar();        //calendario de tiempos                
        cal.add(Calendar.SECOND, UtilsConfig.getSecondsRecoverJwtExp());
        builder.setExpiration(cal.getTime());    
        
        ModelRecoverCodeUser codeUser = UtilsJson.jsonDeserialize(UtilsJWT.getBodyToken(token), ModelRecoverCodeUser.class);        
        if (!codeUser.getCode().equals(code)) {
            throw new ParametroInvalidoException("El código proporsionado no es válido");
        }        
        builder.setSubject(codeUser.getIdUser());
        
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
    
    public static void validateToken(String token) throws TokenExpiradoException {
        try {
            //si no es un token valido lanzará SignaturaException
            Jws<Claims> jws = Jwts.parser().setSigningKey(STRING_KEY).parseClaimsJws(token);
            if (!jws.getBody().getExpiration().before(new Date())) { //token expirado
                throw new TokenExpiradoException("El token que proporsionó ya expiró");
            }                        
        } catch (SignatureException | IllegalArgumentException e) {
            
        }
    }

}
