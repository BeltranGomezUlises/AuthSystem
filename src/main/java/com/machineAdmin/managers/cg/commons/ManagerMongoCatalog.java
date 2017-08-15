/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.daos.cg.commons.DaoMongoFacade;
import com.machineAdmin.entities.cg.commons.EntityMongoCatalog;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.exceptions.AccesoDenegadoException;
import com.machineAdmin.managers.cg.exceptions.ElementosSinAccesoException;
import com.machineAdmin.managers.cg.exceptions.ParametroInvalidoException;
import com.machineAdmin.managers.cg.exceptions.ProfundidadNoAsignadaException;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.utils.UtilsPermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
        entity.setUsuarioCreador(this.getUsuario());
        T t = (T) dao.persist(entity);
        return t;
    }

    @Override
    public List<T> persistAll(List<T> entities) throws Exception {
        for (T entity : entities) {
            entity.setUsuarioCreador(this.getUsuario());
        }
        List<T> ts = dao.persistAll(entities);
        return ts;
    }

    @Override
    public void delete(Object id) throws AccesoDenegadoException, Exception {
        T t = null;
        switch (profundidad) {
            case TODOS:
                dao.delete(id);
                break;
            case PROPIOS:
                t = (T) dao.findOne(id);
                if (t.getUsuarioCreador().equals(this.usuario)) {
                    dao.delete(id);
                } else {
                    throw new AccesoDenegadoException("No tiene permiso de ver los detalles de ésta entidad");
                }
                break;
            case PROPIOS_MAS_PERFILES:
                t = (T) dao.findOne(id);
                if (UtilsPermissions.idsDeUsuariosConLosPerfilesQueTieneElUsuario(this.usuario).contains(t.getUsuarioCreador())) {
                    dao.delete(id);
                } else {
                    throw new AccesoDenegadoException("No tiene permiso de ver los detalles de ésta entidad");
                }
                break;
        }
    }

    public void delete(DBQuery.Query query) throws ElementosSinAccesoException, Exception {
        //para borrar un query, obtener profundidad y filtrar los resultados para borrar solo los permitidos
        List<T> ts = null;
        List<T> elementosSinAcceso = null;
        switch (profundidad) {
            case TODOS:
                dao.delete(query);
                break;
            case PROPIOS:
                ts = dao.findAll(query);
                elementosSinAcceso = new ArrayList<>();
                for (int i = 0; i < ts.size(); i++) {
                    if (!ts.get(i).getUsuarioCreador().equals(this.usuario)) {
                        elementosSinAcceso.add(ts.get(i));
                        ts.remove(i);
                    }
                }
                dao.deleteAll(ts.stream().map(t -> t.getId()).collect(toList()));
                if (!elementosSinAcceso.isEmpty()) {
                    throw new ElementosSinAccesoException(elementosSinAcceso, "Entidades Que no se puedieron Eliminar");
                }
                break;
            case PROPIOS_MAS_PERFILES:
                ts = dao.findAll(query);
                elementosSinAcceso = new ArrayList<>();
                Set<Integer> usuariosCreadores = UtilsPermissions.idsDeUsuariosConLosPerfilesQueTieneElUsuario(usuario);
                for (int i = 0; i < ts.size(); i++) {
                    if (!usuariosCreadores.contains(ts.get(i).getUsuarioCreador())) {
                        elementosSinAcceso.add(ts.get(i));
                        ts.remove(i);
                    }
                }
                dao.deleteAll(ts.stream().map(t -> t.getId()).collect(toList()));
                if (!elementosSinAcceso.isEmpty()) {
                    throw new ElementosSinAccesoException(elementosSinAcceso, "Entidades Que no se puedieron Eliminar");
                }
                break;
        }

        dao.delete(query);
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

    public void update(DBQuery.Query query, T t) throws Exception {
        List<T> ts = this.findAll(query);
        dao.update(query, t);
    }

    @Override
    public T findOne(Object id) throws ParametroInvalidoException, AccesoDenegadoException, Exception {
        T t = (T) dao.findOne(id);
        try {
            switch (this.profundidad) {
                case PROPIOS:
                    //esto está asi para poder notificar con las excepciones que no tiene permiso o no existe                    
                    if (t.getUsuarioCreador().equals(this.usuario)) {
                        return t;
                    } else {
                        throw new AccesoDenegadoException("No tiene permiso de ver los detalles de ésta entidad");
                    }
                case PROPIOS_MAS_PERFILES:
                    if (UtilsPermissions.idsDeUsuariosConLosPerfilesQueTieneElUsuario(usuario).contains(t.getUsuarioCreador())) {
                        return t;
                    } else {
                        throw new AccesoDenegadoException("No tiene permiso de ver los detalles de ésta entidad");
                    }
                case TODOS:
                    return t;
            }
        } catch (NullPointerException e) {
            throw new ParametroInvalidoException("No existe entidad con ese indentificador");
        }
        return null;
    }

    public T findOne(DBQuery.Query query) throws Exception {
        Query queryProfundidad = this.queryProfundidad();
        if (queryProfundidad != null) {
            query = query.and(queryProfundidad);
        }
        T t = (T) dao.findOne(query);
        return t;
    }

    public T findOne(DBQuery.Query query, String... attributesProjection) throws Exception {
        Query queryProfundidad = this.queryProfundidad();
        if (queryProfundidad != null) {
            query = query.and(queryProfundidad);
        }
        return (T) dao.findOne(query, attributesProjection);
    }

    @Override
    public List<T> findAll() throws Exception {
        Query query = this.queryProfundidad();
        if (query != null) {
            return dao.findAll(query);
        } else {
            return dao.findAll();
        }
    }

    public List<T> findAll(String... attributesProjection) throws Exception {
        Query query = this.queryProfundidad();
        if (query != null) {
            return dao.findAll(query, attributesProjection);
        } else {
            return dao.findAll(attributesProjection);
        }
    }

    @Override
    public List<T> findAll(int max) throws Exception {
        Query query = this.queryProfundidad();
        if (query != null) {
            return dao.findAll(query, max);
        } else {
            return dao.findAll(max);
        }
    }

    public List<T> findAll(int max, String... attributesProjection) throws Exception {
        Query q = this.queryProfundidad();
        if (q != null) {
            return dao.findAll(q, max, attributesProjection);
        } else {
            return dao.findAll(max, attributesProjection);
        }
    }

    public List<T> findAll(DBQuery.Query query) throws Exception {
        Query queryProfundidad = this.queryProfundidad();
        if (queryProfundidad != null) {
            query = query.and(queryProfundidad);
        }
        return dao.findAll(query);
    }

    public List<T> findAll(DBQuery.Query query, String... attributesProjection) throws Exception {
        Query queryProfundidad = this.queryProfundidad();
        if (queryProfundidad != null) {
            query = query.and(queryProfundidad);
        }
        return dao.findAll(query, attributesProjection);
    }

    public List<T> findAll(DBQuery.Query query, int max) throws Exception {
        Query queryProfundidad = this.queryProfundidad();
        if (queryProfundidad != null) {
            query = query.and(queryProfundidad);
        }
        return dao.findAll(query, max);
    }

    public List<T> findAll(DBQuery.Query query, int max, String attributesProjection) throws Exception {
        Query queryProfundidad = this.queryProfundidad();
        if (queryProfundidad != null) {
            query = query.and(queryProfundidad);
        }
        return dao.findAll(query, max, attributesProjection);
    }

    public long count(DBQuery.Query query) throws Exception {
        Query queryProfundidad = this.queryProfundidad();
        if (queryProfundidad != null) {
            query = query.and(queryProfundidad);
        }
        return dao.count(query);
    }

    @Override
    public long count() throws Exception {
        Query query = this.queryProfundidad();
        if (query != null) {
            return dao.count(query);
        } else {
            return dao.count();
        }
    }

    @Override
    public T findFirst() throws AccesoDenegadoException, Exception {
        T t = null;
        switch (this.profundidad) {
            case PROPIOS:
                //esto está asi para poder notificar con las excepciones que no tiene permiso o no existe
                t = (T) dao.findFirst();
                if (t.getUsuarioCreador().equals(this.usuario)) {
                    return t;
                } else {
                    throw new AccesoDenegadoException("No tiene permiso de ver los detalles de ésta entidad");
                }
            case PROPIOS_MAS_PERFILES:
                t = (T) dao.findFirst();
                if (UtilsPermissions.idsDeUsuariosConLosPerfilesQueTieneElUsuario(this.usuario).contains(t.getUsuarioCreador())) {
                    return t;
                } else {
                    throw new AccesoDenegadoException("No tiene permiso de ver los detalles de ésta entidad");
                }
            case TODOS:
                t = (T) dao.findFirst();
                break;
        }

        return t;
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
                q = DBQuery.in("usuarioCreador", UtilsPermissions.idsDeUsuariosConLosPerfilesQueTieneElUsuario(usuario));
                break;
        }
        return q;
    }

}
