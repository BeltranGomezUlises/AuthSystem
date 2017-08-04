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
import com.machineAdmin.entities.cg.commons.EntitySQL;
import java.util.List;

/**
 * Facade, contiene el comportamiento base, mas la particularidad de cualquier
 * entindad que no sea un catalogo de SQL
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> Entidad a manejar
 * @param <K> Tipo de dato de llave primaria de la entidad
 */
public abstract class ManagerSQLFacade<T extends EntitySQL, K> extends ManagerSQLFacadeBase<T, K> {

    public ManagerSQLFacade(String usuario, DaoSQLFacade dao) {
        super(usuario, dao);
    }

    public ManagerSQLFacade(DaoSQLFacade dao) {
        super(dao);
    }

    @Override
    public List<T> persistAll(List<T> entities) throws Exception {
        List<T> ts = dao.persistAll(entities);
//        try {
//            ts.stream().forEach(t -> this.auditar("alta", this.modeloRegistroGenerico(t)));
//        } catch (Exception ex) {
//            //para omitir que esta entidad no soporta bitacoras            
//        }
        return ts;
    }

    @Override
    public T persist(T entity) throws Exception {
        dao.persist(entity);
//        try {
//            this.auditar("alta", this.modeloRegistroGenerico(entity));
//        } catch (UnsupportedOperationException ex) {
//            //para omitir que esta entidad no soporta bitacoras            
//        }
        return entity;
    }

}
