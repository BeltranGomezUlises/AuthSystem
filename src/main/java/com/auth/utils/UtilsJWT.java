/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auth.utils;

import com.auth.managers.exceptions.ParametroInvalidoException;
import com.auth.managers.exceptions.TokenExpiradoException;
import com.auth.managers.exceptions.TokenInvalidoException;
import com.auth.models.ModelCodigoRecuperacionUsuario;
import com.auth.models.ModelSesion;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.io.IOException;
import java.security.Key;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class UtilsJWT {

    //llave de encriptacion generada por instancia desplegada
    private static final Key KEY = MacProvider.generateKey();

    //llave de encriptacion por text
    private static final String STRING_KEY = "LLAVE ULTRA SECRETA";

    /**
     * Genera un token jwt con el contenido en subject, expiracion segun configuracion de UtilsConfig
     *
     * @param content contenido a cifrar dentro del subject del jwt
     * @return jwt token de sesion
     * @throws JsonProcessingException
     */
    public static String generateSessionToken(ModelSesion content) throws JsonProcessingException {
        JwtBuilder builder = Jwts.builder();
        Calendar cal = new GregorianCalendar();  //calendario de tiempos        
        builder.setIssuedAt(cal.getTime());     //fecha de expedicion        

        cal.add(Calendar.SECOND, 6000);//aumentar tiempo para asignar expiracion
        builder.setExpiration(cal.getTime());

        builder.setSubject(UtilsJson.jsonSerialize(content)); //poner el sujeto en jwt

        return builder.signWith(SignatureAlgorithm.HS512, STRING_KEY).compact();
    }

    /**
     * Genera un token jwt para la validacion de codigo de recuperacion de contraseña
     *
     * @param model
     * @return
     * @throws JsonProcessingException
     */
    public static String generateValidateUserToken(ModelCodigoRecuperacionUsuario model) throws JsonProcessingException {
        JwtBuilder builder = Jwts.builder();
        Calendar cal = new GregorianCalendar(); //calendario de tiempos                
        cal.add(Calendar.SECOND, 500);
        builder.setExpiration(cal.getTime());
        builder.setSubject(UtilsJson.jsonSerialize(model));

        return builder.signWith(SignatureAlgorithm.HS512, STRING_KEY).compact();
    }

    /**
     * Genera un token jwt para poder hacer un reseteo de contraseña, hacer la validacion pertinente de código de recuperación
     *
     * @param token token jwt de verficacion
     * @param code codigo de recuperacion entregado al usuario
     * @return token jwt para hacer un reseteo de contraseña
     * @throws IOException
     * @throws ParametroInvalidoException si el codigo proporcionado y el token no coinciden en valor
     * @throws TokenInvalidoException si el token proporcionado no es parseable con el cifrado utilizado de esta utileria
     * @throws TokenExpiradoException
     */
    public static String generateTokenResetPassword(String token, String code) throws IOException, ParametroInvalidoException, TokenInvalidoException, TokenExpiradoException {
        ModelCodigoRecuperacionUsuario codeUser = UtilsJson.jsonDeserialize(UtilsJWT.getBodyToken(token), ModelCodigoRecuperacionUsuario.class);
        if (!codeUser.getCode().equals(code)) {
            throw new ParametroInvalidoException("El código proporsionado no es válido");
        }
        ModelSesion modelSesion = new ModelSesion(codeUser.getIdUser(), 0);
        return generateSessionToken(modelSesion);
    }

    /**
     * obtiene el contenido del token de jwt
     *
     * @param token token del cual sacar el contenido en subject
     * @return cadena del contenido
     * @throws TokenInvalidoException si el token proporcionado no es parseable con el cifrado utilizado de esta utileria
     * @throws TokenExpiradoException
     */
    public static String getBodyToken(String token) throws TokenInvalidoException, TokenExpiradoException {
        try {
            return Jwts.parser().setSigningKey(STRING_KEY).parseClaimsJws(token).getBody().getSubject();
        } catch (SignatureException | IllegalArgumentException e) {
            throw new TokenInvalidoException("Token Invalido");
        } catch (ExpiredJwtException exe) {
            throw new TokenExpiradoException("Token expirado");
        }
    }

    /**
     * Obtiene el id del usuario dentro del token de sesion
     *
     * @param token token de sesion jwt
     * @return el id del usuario dentro del token de sesion
     * @throws TokenInvalidoException si el token proporcionado no es parseable con el cifrado utilizado de esta utileria
     * @throws TokenExpiradoException
     */
    public static Integer getUserIdFrom(String token) throws TokenInvalidoException, TokenExpiradoException {
        try {
            ModelSesion sesion = UtilsJson.jsonDeserialize(Jwts.parser().setSigningKey(STRING_KEY).parseClaimsJws(token).getBody().getSubject(), ModelSesion.class);
            return sesion.getUserId();
        } catch (SignatureException | IllegalArgumentException | IOException e) {
            throw new TokenInvalidoException("Token Invalido");
        } catch (ExpiredJwtException exe) {
            throw new TokenExpiradoException("Token expirado");
        }
    }

    /**
     * obtiene el id de la sucursal contenida en el token de sesion
     *
     * @param token token de sesion
     * @return el id de la sucursal dentro del token de sesion
     * @throws TokenInvalidoException si el token proporcionado no es parseable con el cifrado utilizado de esta utileria
     * @throws TokenExpiradoException
     */
    public static Integer getSucursalIdFrom(String token) throws TokenInvalidoException, TokenExpiradoException {
        try {
            ModelSesion sesion = UtilsJson.jsonDeserialize(Jwts.parser().setSigningKey(STRING_KEY).parseClaimsJws(token).getBody().getSubject(), ModelSesion.class);
            return sesion.getSucursalId();
        } catch (SignatureException | IllegalArgumentException | IOException e) {
            throw new TokenInvalidoException("Token Invalido");
        } catch (ExpiredJwtException exe) {
            throw new TokenExpiradoException("Token expirado");
        }
    }

    /**
     * Obtiene el modelo ModelSesion dentro del token jwt
     *
     * @param token token de sesion
     * @return modelo con el usuario y sucursal dentro del token de sesion
     * @throws TokenInvalidoException si el token proporcionado no es parseable con el cifrado utilizado de esta utileria
     * @throws TokenExpiradoException
     */
    public static ModelSesion getModelSesionFrom(String token) throws TokenInvalidoException, TokenExpiradoException {
        try {
            return UtilsJson.jsonDeserialize(Jwts.parser().setSigningKey(STRING_KEY).parseClaimsJws(token).getBody().getSubject(), ModelSesion.class);
        } catch (SignatureException | IllegalArgumentException | IOException e) {
            throw new TokenInvalidoException("Token Invalido");
        } catch (ExpiredJwtException exe) {
            throw new TokenExpiradoException("Token expirado");
        }
    }

    /**
     * Valida si el token de sesion es valido en esta utileria intentandolo parsear
     *
     * @param token token de sesion
     * @throws TokenExpiradoException si el token proporcionado no es parseable con el cifrado utilizado de esta utileria
     * @throws TokenInvalidoException
     */
    public static void validateSessionToken(String token) throws TokenExpiradoException, TokenInvalidoException {
        try {
            Jwts.parser().setSigningKey(STRING_KEY).parseClaimsJws(token);
        } catch (SignatureException | IllegalArgumentException e) {
            throw new TokenInvalidoException("Token Invalido");
        } catch (ExpiredJwtException exe) {
            throw new TokenExpiradoException("Token expirado");
        }
    }

}
