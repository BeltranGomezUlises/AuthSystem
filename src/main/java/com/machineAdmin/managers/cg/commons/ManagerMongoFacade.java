/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.daos.cg.commons.DaoMongoFacade;
import com.machineAdmin.entities.cg.commons.EntityMongo;
import java.util.List;
import org.mongojack.DBQuery.Query;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> is an entity
 */
public class ManagerMongoFacade<T extends EntityMongo> implements ManagerFacade<T> {

    protected DaoMongoFacade dao;

    public ManagerMongoFacade(DaoMongoFacade<T> dao){
        this.dao = dao;
    }

    @Override
    public T persist(T entity) throws Exception {
        
        return (T) dao.persist(entity);
    }

    @Override
    public List<T> persistAll(List<T> entities) throws Exception {
        return dao.persistAll(entities);
    }

    @Override
    public List<T> persistAll(T... entities) throws Exception {
        return dao.persistAll(entities);
    }

    @Override
    public void delete(Object id) throws Exception {
        dao.delete(id);
    }

    public void delete(Query q) {
        dao.delete(q);
    }

    @Override
    public void deleteAll(List<Object> ids) throws Exception {
        dao.deleteAll(ids);
    }

    @Override
    public void deleteAll(Object... ids) throws Exception {
        dao.deleteAll(ids);
    }

    @Override
    public void update(T entity) throws Exception {
        dao.update(entity);
    }

    public List<T> update(Query q, T t) {
        return dao.update(q, t);
    }

    @Override
    public T findOne(Object id) {
        return (T) dao.findOne(id);
    }

    public T findOne(Query q) {
        return (T) dao.findOne(q);
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

    public List<T> findall(String... attributesProjection){
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
    
    public List<T> findall(Query query, String... attributesProjection){
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

}
