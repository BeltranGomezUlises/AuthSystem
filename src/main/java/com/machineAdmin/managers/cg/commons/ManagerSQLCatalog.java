/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.daos.cg.commons.DaoSQLFacade;
import com.machineAdmin.entities.cg.commons.EntitySQLCatalog;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import java.util.List;
import org.jinq.jpa.JPAJinqStream;

/**
 * fachada para manejar entidades sql que tienen usuario creador y hbailidad de
 * acceso con profundidad
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> Entidad a manejar
 * @param <K> Tipo de dato de llave primaria de la entidad
 */
public abstract class ManagerSQLCatalog<T extends EntitySQLCatalog, K> extends ManagerCatalog<T, K> {

    protected final DaoSQLFacade<T, K> dao;

    public ManagerSQLCatalog(DaoSQLFacade<T, K> dao) {
        super();
        this.dao = dao;
    }

    public ManagerSQLCatalog(DaoSQLFacade dao, String token, Profundidad profundidad) throws TokenInvalidoException, TokenExpiradoException {
        super(token, profundidad);
        this.dao = dao;
    }

    public JPAJinqStream<T> stream() {
        return dao.stream();
    }

    @Override
    public T persist(T entity) throws Exception {
        dao.persist(entity);
        return entity;
    }

    @Override
    public List<T> persistAll(List<T> entities) throws Exception {
        return dao.persistAll(entities);
    }

    @Override
    public void delete(K id) throws Exception {
        dao.delete(id);
    }

    @Override
    public void deleteAll(List<K> ids) throws Exception {
        dao.deleteAll(ids);
    }

    @Override
    public void update(T entity) throws Exception {
        dao.update(entity);
    }

    @Override
    public T findOne(K id) throws Exception{
        return dao.findOne(id);
    }

    @Override
    public List<T> findAll() throws Exception {
        //buscar el permiso
        
        return dao.findAll();
    }

    @Override
    public List<T> findAll(int max) throws Exception{
        return dao.findAll(max);
    }

    @Override
    public long count() throws Exception{
        return dao.count();
    }

    @Override
    public T findFirst() throws Exception{
        return (T) dao.findFirst();
    }

    @Override
    public K stringToKey(String s) {
        return dao.stringToPK(s);
    }

}
