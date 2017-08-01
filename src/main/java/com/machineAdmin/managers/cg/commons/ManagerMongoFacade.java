/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.daos.cg.commons.DaoMongoFacade;
import com.machineAdmin.entities.cg.commons.EntityMongo;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.mongojack.DBQuery;
import org.mongojack.DBQuery.Query;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> is an entity
 */
public abstract class ManagerMongoFacade<T extends EntityMongo> extends ManagerFacade<T, Object> {

    /**
     * objeto de acceso a datos
     */
    protected DaoMongoFacade<T> dao;

    public ManagerMongoFacade(String usuario, DaoMongoFacade<T> dao) {
        super(usuario);
        this.dao = dao;
    }

    public ManagerMongoFacade(DaoMongoFacade<T> dao) {
        super();
        this.dao = dao;
    }

    @Override
    public T persist(T entity) {
        T t = (T) dao.persist(entity);
        try {
            entity.setUsuarioCreador(this.getUsuario());
            this.bitacorizar("alta", this.modeloBitacorizar(entity));
        } catch (UnsupportedOperationException ex) {
            //para omitir que esta entidad no soporta bitacoras
        }
        return t;
    }

    @Override
    public List<T> persistAll(List<T> entities) throws Exception {
        List<T> ts = dao.persistAll(entities);
        ts.parallelStream().forEach(t -> {
            try {
                t.setUsuarioCreador(this.getUsuario());
                this.bitacorizar("alta", this.modeloBitacorizar(t));
            } catch (UnsupportedOperationException ex) {
                //para omitir que esta entidad no soporta bitacoras
            }
        });
        return ts;
    }

    @Override
    public void delete(Object id) {
        dao.delete(id);
        try {
            this.bitacorizar("eliminar", this.modeloBitacorizar(this.findOne(id)));
        } catch (UnsupportedOperationException ex) {
            //para omitir que esta entidad no soporta bitacoras
        }
    }

    public void delete(Query q) {
        List<T> ts = this.findAll(q);
        dao.deleteAll(ts.stream().map(t -> t.getId()).collect(toList()));
        try {
            ts.parallelStream().forEach(t -> this.bitacorizar("eliminar", this.modeloBitacorizar(t)));
        } catch (Exception e) {
            //para omitir que esta entidad no soporta bitacoras
        }
    }

    @Override
    public void deleteAll(List<Object> ids) throws Exception {
        List<T> ts = this.findAll(DBQuery.in("_id", ids));
        dao.deleteAll(ids);
        try {
            ts.parallelStream().forEach(t -> this.bitacorizar("eliminar", this.modeloBitacorizar(t)));
        } catch (Exception e) {
        }

    }

    @Override
    public void update(T entity) throws Exception {
        dao.update(entity);
        this.bitacorizar("actualizar", this.modeloBitacorizar(entity));
    }

    public void update(Query q, T t) {
        List<T> ts = this.findAll(q);
        dao.update(q, t);
        try {
            ts.parallelStream().forEach(ts1 -> this.bitacorizar("actualizar", this.modeloBitacorizar(ts1)));
        } catch (Exception e) {
        }
    }

    @Override
    public T findOne(Object id) {
        T t = (T) dao.findOne(id);
        try {
            this.bitacorizar("obtener", this.modeloBitacorizar(t));
        } catch (UnsupportedOperationException ex) {
            //para omitir que esta entidad no soporta bitacoras
        }
        return t;
    }

    public T findOne(Query q) {
        T t = (T) dao.findOne(q);
        try {
            this.bitacorizar("obtener", this.modeloBitacorizar(t));
        } catch (UnsupportedOperationException e) {
            //para omitir que esta entidad no soporta bitacoras
        }
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
