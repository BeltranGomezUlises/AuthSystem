/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auth.managers.commons;

import com.auth.entities.commons.IEntity;
import java.util.List;

/**
 * fachada de manager general
 *
 * @author Alonso --- alonso@kriblet.com
 * @param <T> class entity used to restrict the class of use
 * @param <K>
 */
public abstract class ManagerFacade<T extends IEntity, K> {

    public ManagerFacade() {
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
     * remueve de base de datos las entidades que su propiedad id corresponda con los objetos proporsionados
     *
     * @param ids lista de identificadores de las entidades
     * @throws Exception si existió algún problema al intentar remover
     */
    public abstract void deleteAll(List<K> ids) throws Exception;

    /**
     * reemplaza la entidad proporsionada por la existente en base de datos que coincida con su propiedad id
     *
     * @param entity la entidad con la cual reemplazar la existente en base de datos
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
     * @throws java.lang.Exception
     */
    public abstract T findFirst() throws Exception;

    /**
     * busca todas las entidades existentes en base de datos
     *
     * @return lista con las entidades manejadas que existen en base de datos
     * @throws java.lang.Exception
     */
    public abstract List<T> findAll() throws Exception;

    /**
     * busca todas las entidades existentes en base de datos con un numero maximo de elementos a retornas
     *
     * @param max numero maximo de entidades a tomar de base de datos
     * @return lista con las entidades manejadas que existen en base de datos
     * @throws java.lang.Exception
     */
    public abstract List<T> findAll(int max) throws Exception;

    /**
     * busca las entidades comprendidas en el rango especificado por los parametros
     *
     * @param initialPosition posicion inicial de busqueda
     * @param lastPosition posicion final de busqueda
     * @return lista de entidades comprendidas entre las posiciones proporcionadas
     */
    public abstract List<T> findRange(final int initialPosition, final int lastPosition);

    /**
     * cuenta las entidades manejadas
     *
     * @return numero de entidades existentes en base de datos
     * @throws java.lang.Exception
     */
    public abstract long count() throws Exception;

}
