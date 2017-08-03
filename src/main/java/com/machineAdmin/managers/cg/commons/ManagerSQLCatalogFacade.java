/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.daos.cg.commons.DaoSQLFacade;
import com.machineAdmin.entities.cg.commons.EntitySQLCatalog;
import java.util.List;
import java.util.UUID;

/**
 * Facade, contiene el comportamiento base, mas la particularidad de tener
 * bitacoras para las entidades que sean catalogos en SQL
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> Entidad a manejar
 * @param <K> Tipo de dato de llave primaria de la entidad
 */
public abstract class ManagerSQLCatalogFacade<T extends EntitySQLCatalog, K> extends ManagerSQLFacadeBase<T, K> {

    public ManagerSQLCatalogFacade(String usuario, DaoSQLFacade dao) {
        super(usuario, dao);
    }

    public ManagerSQLCatalogFacade(DaoSQLFacade dao) {
        super(dao);
    }

    @Override
    public T persist(T entity) throws Exception {
        entity.setUsuarioCreador(UUID.fromString(this.getUsuario()));
        dao.persist(entity);
        try {
            this.bitacorizar("alta", this.modeloBitacorizar(entity));
        } catch (UnsupportedOperationException ex) {
            //para omitir que esta entidad no soporta bitacoras
        }
        return entity;
    }

    @Override
    public List<T> persistAll(List<T> entities) throws Exception {
        entities.forEach((entity) -> entity.setUsuarioCreador(UUID.fromString(this.getUsuario())));
        List<T> ts = dao.persistAll(entities);
        try {
            ts.stream().forEach(t -> this.bitacorizar("alta", this.modeloBitacorizar(t)));
        } catch (Exception ex) {
            //para omitir que esta entidad no soporta bitacoras            
        }
        return ts;
    }

}
