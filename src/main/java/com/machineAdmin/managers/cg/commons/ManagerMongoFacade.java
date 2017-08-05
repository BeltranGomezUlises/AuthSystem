/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.daos.cg.commons.DaoMongoFacade;
import com.machineAdmin.entities.cg.commons.EntityMongo;
import com.machineAdmin.entities.cg.commons.Profundidad;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.mongojack.DBQuery;
import org.mongojack.DBQuery.Query;

/**
 * Facade, cumple con el comportamiento base de una entidad de una colección en mongoDB
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> is an entity
 */
public abstract class ManagerMongoFacade<T extends EntityMongo> extends ManagerFacade<T, Object> {

    /**
     * objeto de acceso a datos
     */
    protected DaoMongoFacade<T> dao;

    public ManagerMongoFacade(){
        super();
    }
    
    public ManagerMongoFacade(String usuario, DaoMongoFacade<T> dao) {
        super(usuario);
        this.dao = dao;
    }

    public ManagerMongoFacade(DaoMongoFacade<T> dao, Profundidad profundidad, String token) {
        super(profundidad, token);
        this.dao = dao;
    }

    @Override
    public T persist(T entity) {
        T t = (T) dao.persist(entity);
        return t;
    }

    @Override
    public List<T> persistAll(List<T> entities) throws Exception {
        List<T> ts = dao.persistAll(entities);
        return ts;
    }

    @Override
    public void delete(Object id) {
        dao.delete(id);
    }

    public void delete(Query q) {
        List<T> ts = this.findAll(q);
        dao.deleteAll(ts.stream().map(t -> t.getId()).collect(toList()));
    }

    @Override
    public void deleteAll(List<Object> ids) throws Exception {
        List<T> ts = this.findAll(DBQuery.in("_id", ids));
        dao.deleteAll(ids);
    }

    @Override
    public void update(T entity) throws Exception {
        dao.update(entity);
    }

    public void update(Query q, T t) {
        List<T> ts = this.findAll(q);
        dao.update(q, t);
    }

    @Override
    public T findOne(Object id) {
        T t = (T) dao.findOne(id);
        return t;
    }

    public T findOne(Query q) {        
        T t = (T) dao.findOne(q);
        return t;
    }

    public T findOne(Query q, String... attributesProjection) {
        return (T) dao.findOne(q, attributesProjection);
    }

    @Override
    public List<T> findAll() {
        return dao.findAll();
    }

    public List<T> findAll(String... attributesProjection) {
        return dao.findAll(attributesProjection);
    }

    public List<T> findall(String... attributesProjection) {
        return dao.findAll(attributesProjection);
    }

    @Override
    public List<T> findAll(int max) {
        return dao.findAll(max);
    }

    public List<T> findAll(int max, String... attributesProjection) {
        return dao.findAll(max, attributesProjection);
    }

    public List<T> findAll(Query q) {
        return dao.findAll(q);
    }

    public List<T> findall(Query query, String... attributesProjection) {
        return dao.findAll(query, attributesProjection);
    }

    public List<T> findAll(Query q, int max) {
        return dao.findAll(q, max);
    }

    public List<T> findAll(Query q, int max, String attributesProjection) {
        return dao.findAll(q, max, attributesProjection);
    }

    public long count(Query q) {
        return dao.count(q);
    }

    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public T findFirst() {
        return (T) dao.findFirst();
    }

    public DaoMongoFacade<T> getDao() {
        return dao;
    }

    @Override
    public Object stringToKey(String s) {
        return s;
    }

}
