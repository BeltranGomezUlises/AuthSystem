/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.entities.cg.commons.IEntity;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.models.cg.ModelBitacoraGenerica;
import com.machineAdmin.utils.UtilsBitacora;
import com.machineAdmin.utils.UtilsBitacora.ModeloBitacora;
import com.machineAdmin.utils.UtilsJWT;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> class entity used to restrict the class of use
 * @param <K>
 */
public abstract class ManagerFacade<T extends IEntity, K> {

    protected String usuario;

    public ManagerFacade(String usuario) {
        this.usuario = usuario;
    }

    public ManagerFacade() {

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
     * método para generar el modelo de bitacora genérico con el nombre de la
     * colección y el objeto a persistir
     *
     * @param entity la entidad con la cual obtener la referencia a persistir
     * @return modelo generico para generar bitacoras de catálogos
     */
    public abstract ModelBitacoraGenerica modeloBitacorizar(T entity);

    /**
     * Débe de retorna el nombre de la colección que se generará en mongoDB para
     * almacenar las bitcaras de esta la clases entidad <T>
     *
     * @return nombre de la colección a utilizar para el registor de bitacoras
     */
    protected abstract String bitacoraCollectionName();

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

    /**
     * genera el registro de la bitátora con la accion proporsionada
     *
     * @param accion nombre de la accion a registrar
     * @param model modelo con el nombre de la colección en mongoDB y el objeto
     * a persistir
     */
    public void bitacorizar(String accion, ModelBitacoraGenerica model) {
        if (usuario != null) {
            UtilsBitacora.bitacorizar(usuario, accion, model.getCollectionName(), model.getObjectToPersist());
        }
    }

    /**
     * obtiene todas las bitacoras existentes de esta clase entidad <T>
     *
     * @return lista de movimientos bitacorizados en la coleccion de mongoDB
     * generados por esta clase entidad <T>
     */
    public List<ModeloBitacora> bitacoras() throws UnsupportedOperationException {
        try {
            return UtilsBitacora.bitacoras(this.bitacoraCollectionName());
        } catch (UnsupportedOperationException e) {
            throw e;
        }
    }

    /**
     * obtiene las bitácoras existentes de la entidad <T> que su fecha esta
     * entre las 2 fechas propuestas, donde fecha sea mayor o igual a
     * fechaInicial y fecha sea menor o igual a fecha final
     *
     * @param fechaInicial fecha donde inicia el rango de filtrado
     * @param fechaFinal fecha donte termina el rango de filtrado
     * @return lista de modelos con la bitacora de esta entidad <T>
     */
    public List<ModeloBitacora> bitacorasEntre(Date fechaInicial, Date fechaFinal) throws UnsupportedOperationException {
        try {
            return UtilsBitacora.bitacorasEntre(this.bitacoraCollectionName(), fechaInicial, fechaFinal);
        } catch (UnsupportedOperationException e) {
            throw e;
        }
    }

    /**
     * obtiene las bitácoras existentes de la entidad <T> que su fecha esta
     * despues la fecha propuesta, donde fecha sea mayor o igual a fechaInicial
     *
     * @param fechaInicial fecha donde inicial rango de filtrado
     * @return lista de modelos con la bitacora de esta entidad
     */
    public List<ModeloBitacora> bitacorasDesde(Date fechaInicial) throws UnsupportedOperationException {
        try {
            return UtilsBitacora.bitacorasDesde(this.bitacoraCollectionName(), fechaInicial);
        } catch (UnsupportedOperationException e) {
            throw e;
        }
    }

    /**
     * obtiene las bitácoras existentes de la entidad <T> que su fecha esta
     * antes la fecha propuesta, donde fecha sea menor o igual a fechaFinal
     *
     * @param fechaFinal fecha donde termina le rango de filtrado
     * @return lista de modelos con la bitacora de esta entidad
     */
    public List<ModeloBitacora> bitacorasHasta(Date fechaFinal) throws UnsupportedOperationException{
        try {
            return UtilsBitacora.bitacorasHasta(this.bitacoraCollectionName(), fechaFinal);
        } catch (UnsupportedOperationException e) {
            throw e;
        }
    }

}
