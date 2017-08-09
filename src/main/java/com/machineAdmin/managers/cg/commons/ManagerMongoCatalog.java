/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.daos.cg.admin.postgres.DaoUsuario;
import com.machineAdmin.daos.cg.admin.postgres.DaoUsuariosPerfil;
import com.machineAdmin.daos.cg.commons.DaoMongoFacade;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.entities.cg.commons.EntityMongoCatalog;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.exceptions.ProfundidadNoAsignadaException;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import java.util.List;
import java.util.UUID;
import static java.util.stream.Collectors.toList;
import org.mongojack.DBQuery;
import org.mongojack.DBQuery.Query;

/**
 * fachada para manejadores de entidades mongo que tienen usuario creador y
 * habilidad de acceder con profundidad
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> is an entity
 */
public abstract class ManagerMongoCatalog<T extends EntityMongoCatalog> extends ManagerCatalog<T, Object> {

    /**
     * objeto de acceso a datos
     */
    protected DaoMongoFacade<T> dao;

    public ManagerMongoCatalog(DaoMongoFacade<T> dao) {
        super();
        this.dao = dao;
    }

    public ManagerMongoCatalog(DaoMongoFacade<T> dao, String token, Profundidad profundidad) throws TokenInvalidoException, TokenExpiradoException {
        super(token, profundidad);
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

    public void delete(DBQuery.Query q) {
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

    public void update(DBQuery.Query q, T t) {
        List<T> ts = this.findAll(q);
        dao.update(q, t);
    }

    @Override
    public T findOne(Object id) {
        T t = (T) dao.findOne(id);
        return t;
    }

    public T findOne(DBQuery.Query q) {
        T t = (T) dao.findOne(q);
        return t;
    }

    public T findOne(DBQuery.Query q, String... attributesProjection) {
        return (T) dao.findOne(q, attributesProjection);
    }

    @Override
    public List<T> findAll() throws Exception {
        Query q = this.queryProfundidad();
        if (q != null) {
            return dao.findAll(q);
        } else {
            throw new Exception("Query de profundidad imposible de generar");
        }
    }

    public List<T> findAll(String... attributesProjection) throws Exception {
        Query q = this.queryProfundidad();
        if (q != null) {
            return dao.findAll(q, attributesProjection);
        } else {
            return dao.findAll(attributesProjection);
        }
    }

    @Override
    public List<T> findAll(int max) throws Exception {
        Query q = this.queryProfundidad();
        if (q != null) {
            return dao.findAll(q, max);
        } else {
            return dao.findAll(max);
        }
    }

    public List<T> findAll(int max, String... attributesProjection) {
        return dao.findAll(max, attributesProjection);
    }

    public List<T> findAll(DBQuery.Query q) {
        return dao.findAll(q);
    }

    public List<T> findall(DBQuery.Query query, String... attributesProjection) {
        return dao.findAll(query, attributesProjection);
    }

    public List<T> findAll(DBQuery.Query q, int max) {
        return dao.findAll(q, max);
    }

    public List<T> findAll(DBQuery.Query q, int max, String attributesProjection) {
        return dao.findAll(q, max, attributesProjection);
    }

    public long count(DBQuery.Query q) {
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

    private Query queryProfundidad() throws Exception {
        Query q = null;
        if (this.profundidad == null) {
            throw new ProfundidadNoAsignadaException();
        }
        switch (this.profundidad) {
            case TODOS:
                //no poner query
                break;
            case PROPIOS:
                q = DBQuery.is("usuarioCreador", this.usuario);
                break;
            case PROPIOS_MAS_PERFILES:
                //buscar los id de los usuarios con mis perfiles 
                DaoUsuario daoUsuario = new DaoUsuario();

                Usuario u = daoUsuario.findOne(UUID.fromString(usuario));

                //ids de perfiles del usuario
                List<UUID> perfilesDelUsuario = u.getUsuariosPerfilList().stream()
                        .map(up -> up.getUsuariosPerfilPK().getPerfil())
                        .collect(toList());
                //ids de usuarios con esos perfiles
                DaoUsuariosPerfil daoUsuariosPerfil = new DaoUsuariosPerfil();
                List<UUID> usuariosDeLosPerfiles = daoUsuariosPerfil.stream()
                        .where(up -> perfilesDelUsuario.contains(up.getUsuariosPerfilPK().getPerfil()))
                        .map(up -> up.getUsuariosPerfilPK().getUsuario())
                        .collect(toList());

                q = DBQuery.in("usuarioCreador", perfilesDelUsuario);
                break;         
        }
        return q;
    }

}
