/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.entities.cg.commons.IEntity;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.utils.UtilsJWT;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> class entity used to restrict the class of use
 * @param <K>
 */
public abstract class ManagerFacade<T extends IEntity, K> {

    protected String usuario;
    protected Profundidad profundidad;

    public ManagerFacade(String usuario) {
        this.usuario = usuario;
    }

    public ManagerFacade(Profundidad profundidad, String token){
        this.profundidad = profundidad;
        try {
            this.usuario = UtilsJWT.getBodyToken(token);
        } catch (TokenInvalidoException | TokenExpiradoException ex) {
            Logger.getLogger(ManagerFacade.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public ManagerFacade() {
    }

    public Profundidad getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(Profundidad profundidad) {
        this.profundidad = profundidad;
    }

    public String getUsuario() {
        return usuario;
    }

    private void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * metodo para persistir la entidad a base de datos
     *
     * @param entity entidad a persistir en base de datos
     * @return la entidad persistida con su _id
     * @throws Exception si existió un problema al intertar persistir
     */
    public abstract T persist(T entity) throws Exception;

    /**
     * metodo para persistir todas las entidades proporsioadas en base de datos
     *
     * @param entities lista de entidades a persistir
     * @return la lista de entidades persistidas con su propiedad _id
     * @throws Exception si existió un problema la intentar persistir
     */
    public abstract List<T> persistAll(List<T> entities) throws Exception;

    /**
     * remueve de base de datos la entidad que corresponda al id proporsionado
     *
     * @param id propiedad que identifia al objeto
     * @throws Exception
     */
    public abstract void delete(K id) throws Exception;

    /**
     * remueve de base de datos las entidades que su propiedad id corresponda
     * con los objetos proporsionados
     *
     * @param ids lista de identificadores de las entidades
     * @throws Exception si existió algún problema al intentar remover
     */
    public abstract void deleteAll(List<K> ids) throws Exception;

    /**
     * reemplaza la entidad proporsionada por la existente en base de datos que
     * coincida con su propiedad id
     *
     * @param entity la entidad con la cual reemplazar la existente en base de
     * datos
     * @throws Exception si exitió un problema al actualizar
     */
    public abstract void update(T entity) throws Exception;

    /**
     * busca la entidad correspondiente al identificador proporsionado
     *
     * @param id identificador de la entidad
     * @return entidad de la base de datos
     * @throws java.lang.Exception
     */
    public abstract T findOne(K id) throws Exception;

    /**
     * busca la primer entidad existente en base de datos
     *
     * @return la entidad manejada en primer posición en base de datos
     */
    public abstract T findFirst();

    /**
     * busca todas las entidades existentes en base de datos
     *
     * @return lista con las entidades manejadas que existen en base de datos
     */
    public abstract List<T> findAll();

    /**
     * busca todas las entidades existentes en base de datos con un numero
     * maximo de elementos a retornas
     *
     * @param max numero maximo de entidades a tomar de base de datos
     * @return lista con las entidades manejadas que existen en base de datos
     */
    public abstract List<T> findAll(int max);

    /**
     * cuenta las entidades manejadas
     *
     * @return numero de entidades existentes en base de datos
     */
    public abstract long count();

    /**
     * transforma un String s a el tipo de datos del identificador de la entidad
     *
     * @param s cadena a trasformar
     * @return K, objeto del tipo de dato del identificador de la entidad
     */
    public abstract K stringToKey(String s);

    /**
     * Débe de retorna el nombre de la colección que se generará en mongoDB para
     * almacenar las bitcaras, auditorias, registros estadisticos de esta la
     * clases entidad <T>
     *
     * @return nombre de la colección a utilizar para el registor de bitacoras
     */
    public abstract String nombreColeccionParaRegistros();

    /**
     * asignar un token de sesion a este manager, con la intencion de validar el
     * usuario en pemisos y registros de bitacoras
     *
     * @param token token de sesion
     * @throws TokenInvalidoException si el token proporsionado no es válido
     * @throws TokenExpiradoException si el token proporsionado ya expiró
     */
    public void setToken(String token) throws TokenInvalidoException, TokenExpiradoException {
        this.setUsuario(UtilsJWT.getBodyToken(token));
    }

    public static String obtenerAccionActual() {
        return Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
