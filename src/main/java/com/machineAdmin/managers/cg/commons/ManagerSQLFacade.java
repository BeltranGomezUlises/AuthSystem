/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.daos.cg.commons.DaoSQLFacade;
import com.machineAdmin.daos.cg.exceptions.ConstraintException;
import com.machineAdmin.daos.cg.exceptions.SQLPersistenceException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.jinq.jpa.JPAJinqStream;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T>
 */
public class ManagerSQLFacade<T extends Serializable> implements ManagerFacade<T> {

    private final DaoSQLFacade dao;

    public ManagerSQLFacade(DaoSQLFacade dao) {
        this.dao = dao;
    }

    @Override
    public T persist(T entity) throws SQLPersistenceException, ConstraintException {
        dao.persist(entity);
        return entity;
    }

    @Override
    public List<T> persistAll(List<T> entities) throws Exception {
        dao.persistAll(entities);
        return entities;
    }

    @Override
    public List<T> persistAll(T... entities) throws Exception {
        dao.persistAll(entities);
        return Arrays.asList(entities);
    }

    @Override
    public void delete(Object id) throws Exception {
        dao.delete(id);
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
    public void update(T entity) throws SQLPersistenceException, ConstraintException {
        dao.update(entity);
    }

    @Override
    public T findOne(Object id) {
        return (T) dao.findOne(id);
    }

    @Override
    public List<T> findAll() {
        return dao.findAll();
    }

    @Override
    public List<T> findAll(int max) {
        return dao.findAll(max);
    }

    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public T findFirst() {
        return (T) dao.findFirst();
    }
    
    public JPAJinqStream<T> stream(){
        return dao.stream();
    }
    
}
