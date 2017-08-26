/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.daos.cg.commons.DaoSQLFacade;
import com.machineAdmin.entities.cg.commons.EntitySQLCatalog;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.exceptions.AccesoDenegadoException;
import com.machineAdmin.managers.cg.exceptions.ElementosSinAccesoException;
import com.machineAdmin.managers.cg.exceptions.ParametroInvalidoException;
import com.machineAdmin.managers.cg.exceptions.ProfundidadNoAsignadaException;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;
import com.machineAdmin.utils.UtilsPermissions;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import static java.util.stream.Collectors.toList;
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

    @Override
    public T persist(T entity) throws Exception {
        entity.setUsuarioCreador(this.getUsuario());
        dao.persist(entity);
        return entity;
    }

    @Override
    public List<T> persistAll(List<T> entities) throws Exception {
        for (T entity : entities) {
            entity.setUsuarioCreador(this.getUsuario());
        }
        return dao.persistAll(entities);
    }

    @Override
    public void delete(K id) throws Exception {
        T t;
        int usuarioId = this.usuario;
        switch (profundidad) {
            case TODOS:
                dao.delete(id);
                break;
            case PROPIOS_MAS_PERFILES:
                t = dao.findOne(id);
                Set<Integer> idsDeUsuariosConPerfilesDelUsuario = UtilsPermissions.idsDeUsuariosConLosPerfilesQueTieneElUsuario(usuarioId);
                if (idsDeUsuariosConPerfilesDelUsuario.contains(t.getUsuarioCreador())) {
                    dao.delete(id);
                } else {
                    throw new AccesoDenegadoException("No tiene permiso de actualizar ésta entidad");
                }
                break;
            case PROPIOS:
                t = dao.findOne(id);
                if (t.getId().equals(usuarioId)) {
                    dao.delete(id);
                } else {
                    throw new AccesoDenegadoException("No tiene permiso de actualizar ésta entidad");
                }
                break;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public void deleteAll(List<K> ids) throws Exception {
        List<K> idsConAcceso;
        List<T> elementosSinAcceso;
        switch (profundidad) {
            case TODOS:
                dao.deleteAll(ids);
                break;
            case PROPIOS:
                //mandar borrar aquellos que solo sean del usuario actual                  
                idsConAcceso = dao.stream()
                        .where(t -> t.getUsuarioCreador().equals(this.usuario) && ids.contains(t.getId()))
                        .select(t -> (K) t.getId())
                        .collect(toList());
                dao.deleteAll(idsConAcceso);

                elementosSinAcceso = dao.stream()
                        .where(t -> t.getUsuarioCreador().equals(this.usuario) && !ids.contains(t.getId()))
                        .collect(toList());
                if (!elementosSinAcceso.isEmpty()) {
                    throw new ElementosSinAccesoException(elementosSinAcceso, "Existen entidades que no se puedieron eliminar");
                }
                break;
            case PROPIOS_MAS_PERFILES:
                Set<Integer> usuariosConPerfilesDeUsuarioActual = UtilsPermissions.idsDeUsuariosConLosPerfilesQueTieneElUsuario(usuario);
                //mandar borrar aquellos que solo sean del usuario actual                                                                
                idsConAcceso = dao.stream()
                        .where(t -> usuariosConPerfilesDeUsuarioActual.contains(t.getUsuarioCreador()) && ids.contains(t.getId()))
                        .select(t -> (K) t.getId())
                        .collect(toList());

                dao.deleteAll(idsConAcceso);

                elementosSinAcceso = dao.stream()
                        .where(t -> !usuariosConPerfilesDeUsuarioActual.contains(t.getUsuarioCreador()) && ids.contains(t.getId()))
                        .collect(toList());
                if (!elementosSinAcceso.isEmpty()) {
                    throw new ElementosSinAccesoException(elementosSinAcceso, "Existen entidades que no se puedieron eliminar");
                }
                break;
            default:
                throw new ProfundidadNoAsignadaException();
        }
    }

    @Override
    public void update(T entity) throws Exception {
        switch (profundidad) {
            case TODOS:
                dao.update(entity);
                break;
            case PROPIOS_MAS_PERFILES:
                Set<Integer> idsDeUsuariosConPerfilesDelUsuario = UtilsPermissions.idsDeUsuariosConLosPerfilesQueTieneElUsuario(this.usuario);
                if (idsDeUsuariosConPerfilesDelUsuario.contains(entity.getUsuarioCreador())) {
                    dao.update(entity);
                } else {
                    throw new AccesoDenegadoException("No tiene permiso de actualizar ésta entidad");
                }
                break;
            case PROPIOS:
                if (entity.getId().equals(this.usuario)) {
                    dao.update(entity);
                } else {
                    throw new AccesoDenegadoException("No tiene permiso de actualizar ésta entidad");
                }
                break;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public T findOne(K id) throws Exception {
        try {
            T t = null;
            if (id instanceof Integer) {
                int idInt = Integer.parseInt(id.toString());
                t = this.dao.stream().where(tElement -> tElement.getId().equals(idInt)).findFirst().get();
            } else {
                if (id instanceof Long) {
                    long idLong = Long.parseLong(id.toString());
                    t = this.dao.stream().where(tElement -> tElement.getId().equals(idLong)).findFirst().get();
                } else {
                    if (id instanceof String) {
                        String idString = id.toString();
                        t = this.stream().where(tElement -> tElement.getId().equals(idString)).findFirst().get();
                    }
                }
            }
            switch (profundidad) {
                case TODOS:
                    return t;                    
                case PROPIOS:
                    if (t.getUsuarioCreador().equals(this.usuario)) {
                        return t;
                    }else{
                        throw new AccesoDenegadoException("No tiene permiso de ver los detalles de ésta entidad");
                    }                    
                case PROPIOS_MAS_PERFILES:
                    Set<Integer> idsDeUsuariosConPerfilesDelUsuario = UtilsPermissions.idsDeUsuariosConLosPerfilesQueTieneElUsuario(this.usuario);                    
                    if (idsDeUsuariosConPerfilesDelUsuario.contains(t.getUsuarioCreador())) {
                        return t;
                    }else{
                        throw new AccesoDenegadoException("No tiene permiso de ver los detalles de ésta entidad");
                    }                    
                default:
                    throw new AssertionError();
            }
        } catch (NoSuchElementException e) {
            throw new ParametroInvalidoException("No existe una entidad con el parametro enviado");
        }        
    }

    @Override
    public List<T> findAll() throws Exception {
        return this.stream().collect(toList());
    }

    @Override
    public List<T> findAll(int max) throws Exception {
        return this.stream().limit(max).collect(toList());
    }

    @Override
    public long count() throws Exception {
        return this.stream().count();
    }

    @Override
    public T findFirst() throws Exception {
        return (T) dao.findFirst();
    }

    @Override
    public K stringToKey(String s) {
        return dao.stringToPK(s);
    }

    /**
     * stream de consulta tipo JPAJinqStream, añadir por defecto un filtro de
     * profundidad de acceso por ser entidad de catalogo
     *
     * @return
     * @throws
     * com.machineAdmin.managers.cg.exceptions.ProfundidadNoAsignadaException
     */
    public JPAJinqStream<T> stream() throws ProfundidadNoAsignadaException, Exception {
        if (this.profundidad == null) {
            throw new ProfundidadNoAsignadaException();
        }
        int usuarioId = this.usuario;
        switch (this.profundidad) {
            case TODOS:
                return dao.stream();
            case PROPIOS_MAS_PERFILES:
                Set<Integer> idsDeUsuariosConPerfilesDelUsuario = UtilsPermissions.idsDeUsuariosConLosPerfilesQueTieneElUsuario(usuarioId);
                return dao.stream().where(t -> idsDeUsuariosConPerfilesDelUsuario.contains(t.getUsuarioCreador()));
            case PROPIOS:
                return dao.stream().where(t -> t.getUsuarioCreador().equals(usuarioId));
            default:
                throw new AssertionError();
        }
    }

}
