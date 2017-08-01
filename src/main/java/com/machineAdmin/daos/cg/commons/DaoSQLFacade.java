/*
 * Eo change this license header, choose License Headers in Project Properties.
 * Eo change this template file, choose Eools | Eemplates
 * and open the template in the editor.
 */
package com.machineAdmin.daos.cg.commons;

import com.machineAdmin.daos.cg.exceptions.ConstraintException;
import com.machineAdmin.daos.cg.exceptions.SQLPersistenceException;
import com.machineAdmin.entities.cg.commons.IEntity;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> Entidad JPA a utilizar por el controlador C JPA respaldado de
 * DaoSQLFacade
 * @param <K> Tipo de dato de la llave primaria de la entidad
 */
public abstract class DaoSQLFacade<T extends IEntity, K> {

    private final Class<T> claseEntity;
    private final Class<K> clasePK;
    private final EntityManagerFactory eMFactory;
    private final JinqJPAStreamProvider streams;

    public DaoSQLFacade(EntityManagerFactory eMFactory, Class<T> claseEntity, Class<K> clasePk) {
        this.eMFactory = eMFactory;
        this.claseEntity = claseEntity;
        this.clasePK = clasePk;
        streams = new JinqJPAStreamProvider(eMFactory);
    }

    public void persist(T entity) throws Exception {
        EntityManager em = this.getEM();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<T> persistAll(List<T> entities) {
        EntityManager em = this.getEM();
        try {
            em.getTransaction().begin();
            for (T entity : entities) {
                em.persist(entity);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return entities;
    }

    public void delete(K id) throws SQLPersistenceException {
        EntityManager em = this.getEM();
        try {
            em.getTransaction().begin();
            em.remove(em.getReference(claseEntity, id));
            em.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void deleteAll(List<K> ids) throws Exception {
        EntityManager em = this.getEM();
        try {
            em.getTransaction().begin();
            for (Object id : ids) {
                em.remove(em.getReference(claseEntity, id));
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void update(T entity) throws SQLPersistenceException, ConstraintException {
        EntityManager em = this.getEM();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public T findFirst() {
        try {
            return findAll(false, 1, 0).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public T findOne(K id) {
        return getEM().find(claseEntity, id);
    }

    public List<T> findAll(int max) {
        return findAll(false, max, 0);
    }

    public List<T> findAll() {
        return findAll(true, -1, -1);
    }

    public List<T> findAll(int maxResults, int firstResult) {
        return findAll(false, maxResults, firstResult);
    }

    private List<T> findAll(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEM();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(claseEntity));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public long count() {
        EntityManager em = getEM();
        long count = streams.streamAll(getEM(), claseEntity).count();
        if (em != null) {
            em.close();
        }
        return count;
    }

    public EntityManager getEM() {
        return eMFactory.createEntityManager();
    }

    protected Query createQuery(String query) {
        return this.getEM().createQuery(query);
    }

    public JPAJinqStream<T> stream() {
        JinqJPAStreamProvider stream = new JinqJPAStreamProvider(eMFactory);
        stream.registerAttributeConverterType(UUID.class);        
        return stream.streamAll(eMFactory.createEntityManager(), claseEntity);
    }

    public K stringToPK(String s) {
        if (clasePK.getName().equals(Integer.class.getName())) {
            return (K) Integer.valueOf(s);
        } else {
            if (clasePK.getName().equals(Long.class.getName())) {
                return (K) Long.valueOf(s);
            } else {
                if (clasePK.getName().equals(UUID.class.getName())) {
                    return (K) UUID.fromString(s);
                }
            }
        }
        return (K) s;
    }
}
