/*
 * Copyright (C) 2017 Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.daos.cg.commons.DaoSQLFacade;
import com.machineAdmin.daos.cg.exceptions.ConstraintException;
import com.machineAdmin.daos.cg.exceptions.SQLPersistenceException;
import com.machineAdmin.entities.cg.commons.IEntity;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.jinq.jpa.JPAJinqStream;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T>
 * @param <K>
 */
public abstract class ManagerSQLFacadeBase<T extends IEntity, K> extends ManagerFacade<T, K> {

    protected final DaoSQLFacade<T, K> dao;

    public ManagerSQLFacadeBase(String usuario, DaoSQLFacade dao) {
        super(usuario);
        this.dao = dao;
    }

    public ManagerSQLFacadeBase(DaoSQLFacade dao) {
        super();        
        this.dao = dao;
    }        

    @Override
    public void delete(K id) throws Exception {
        T t = dao.findOne(id);
        dao.delete(id);
        try {
            this.bitacorizar("eliminar", this.modeloBitacorizar(t));
        } catch (UnsupportedOperationException e) {
            //para omitir que esta entidad no soporta bitacoras
        }

    }

    @Override
    public void deleteAll(List<K> ids) throws SQLPersistenceException, Exception {
        List<T> ts = dao.stream().filter((T t) -> ids.contains((K) t.getId())).collect(toList());
        dao.deleteAll(ids);
        try {
            ts.stream().forEach(t -> this.bitacorizar("eliminar", this.modeloBitacorizar(t)));
        } catch (Exception  e) {
            //para omitir que esta entidad no soporta bitacoras
        }

    }

    @Override
    public void update(T entity) throws SQLPersistenceException, ConstraintException {
        dao.update(entity);        
        try {
            this.bitacorizar("actualizar", this.modeloBitacorizar(entity));
        } catch (UnsupportedOperationException e) {
        }
    }

    @Override
    public T findOne(K id) {
        T t = (T) dao.findOne(id);        
        try {
            this.bitacorizar("obtener", this.modeloBitacorizar(t));
        } catch (UnsupportedOperationException  e) {
            //para omitir que esta entidad no soporta bitacoras
        }                    
        return t;
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

    public JPAJinqStream<T> stream() {
        return dao.stream();
    }

    @Override
    public K stringToKey(String s) {
        return dao.stringToPK(s);
    }
    
}
