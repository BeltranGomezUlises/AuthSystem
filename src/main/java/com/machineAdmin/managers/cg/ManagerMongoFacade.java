/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg;

import com.machineAdmin.daos.cg.DaoMongoFacade;
import com.machineAdmin.entities.cg.EntityMongo;
import java.util.List;
import org.mongojack.DBQuery.Query;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> is an entity
 */
public class ManagerMongoFacade<T extends EntityMongo> implements ManagerFacade<T> {

    protected DaoMongoFacade dao;

    protected ManagerMongoFacade(DaoMongoFacade dao) {
        this.dao = dao;
    }

    @Override
    public T persist(T entity) {
        return (T) dao.persist(entity);
    }

    @Override
    public List<T> persistAll(List<T> entities) {
        return dao.persistAll(entities);
    }

    @Override
    public List<T> persistAll(T... entities) {
        return dao.persistAll(entities);
    }

    @Override
    public boolean delete(T entity) {
        return dao.delete(entity);
    }
    
    public boolean delete(Query q){
        return dao.delete(q);
    }
    
    @Override
    public List<T> deleteAll(List<T> entities) {
        return dao.deleteAll(entities);
    }

    @Override
    public List<T> deleteAll(T... entities) {
        return dao.deleteAll(entities);
    }

    @Override
    public boolean update(T entity) {        
        return dao.update(entity);
    }

    public List<T> update(Query q, T t){
        return dao.update(q, t);
    }
    
    @Override
    public T findOne(Object id) {
        return (T) dao.findOne(id);
    }

    public T findOne(Query q){
        return (T) dao.findOne(q);
    }
    
    @Override
    public List<T> findAll() {
        return dao.findAll();
    }

    public List<T> findAll(Query q){
        return dao.findAll(q);
    }
    
    @Override
    public List<T> findAll(int max) {
        return dao.findAll(max);
    }

    public List<T> findAll(Query q, int max){
        return dao.findAll(q, max);
    }
    
    @Override
    public long count() {
        return dao.count();
    }            
    
    public long count(Query q) {
        return dao.count(q);
    }            
    
}
