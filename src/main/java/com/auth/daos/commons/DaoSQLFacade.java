/*
 * Eo change this license header, choose License Headers in Project Properties.
 * Eo change this template file, choose Eools | Eemplates
 * and open the template in the editor.
 */
package com.auth.daos.commons;

import com.auth.entities.admin.Usuario;
import com.auth.entities.commons.IEntity;
import com.auth.utils.UtilsDB;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.jinq.jpa.JPAJinqStream;
import org.jinq.jpa.JinqJPAStreamProvider;

/**
 * Facade Data Access Object para entidades SQL
 *
 * @author Alonso --- alonso@kriblet.com
 * @param <T> Entidad JPA a utilizar por el controlador C JPA respaldado de DaoSQLFacade
 * @param <K> Tipo de dato de la llave primaria de la entidad
 */
public abstract class DaoSQLFacade<T extends IEntity<K>, K> {

    private final Class<T> claseEntity;
    private final Class<K> clasePK;
    private final JinqJPAStreamProvider streamProvider;

    /**
     * al sobreescribir considerar la fabrica de EntityManager, que sea la que apunte a la base de datos adecuada, que la clase entidad sea correcta y la clase que represente la llave primaria tambien corresponda
     *
     * @param claseEntity clase de la entidad con la cual operar
     * @param clasePk clase que represente la llave primaria de la entidad
     */
    public DaoSQLFacade(Class<T> claseEntity, Class<K> clasePk) {
        this.claseEntity = claseEntity;
        this.clasePK = clasePk;

        streamProvider = new JinqJPAStreamProvider(UtilsDB.getEMFactory());
        streamProvider.registerAttributeConverterType(UUID.class);
    }

    public Class<T> getClaseEntity() {
        return claseEntity;
    }

    public Class<K> getClasePK() {
        return clasePK;
    }

    public JinqJPAStreamProvider getStreamProvider() {
        return streamProvider;
    }

    /**
     * obtiene una nueva instancia de un EntityManager de la fabrica proporsionada al construir el objeto
     *
     * @return EntityManager de la fabrica de este Data Access Object
     */
    public EntityManager getEM() {
        return UtilsDB.getEMFactory().createEntityManager();
    }

    /**
     * construye un JPQL Query con el parametro obtenido
     *
     * @param jpql cadena con el JPQL para construir un query
     * @return query contruido con el JPQL
     */
    protected Query createQuery(String jpql) {
        return this.getEM().createQuery(jpql);
    }

    /**
     * construye un Stream de datos de tipo JPAJinq, esto para poder realizar consultas con funciones lambda
     *
     * @return strema de datos de la entidad con la cual operar
     */
    public JPAJinqStream<T> stream() {
        return streamProvider.streamAll(UtilsDB.getEMFactory().createEntityManager(), claseEntity);
    }

    //<editor-fold defaultstate="collapsed" desc="¡LEEME!">
    //Todos los metodos siguientes tiene con objetivo hacer y solo hacer lo que su nombre indica       
    //</editor-fold>
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

    public List<T> persistAll(List<T> entities) throws Exception {
        EntityManager em = this.getEM();
        try {
            em.getTransaction().begin();
            entities.forEach(e -> em.persist(e));
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

    public void delete(K id) throws Exception {
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
            ids.forEach((id) -> em.remove(em.getReference(claseEntity, id)));
            em.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void update(T entity) throws Exception {
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

    public T findFirst() throws Exception {
        try {
            return findAll(false, 1, 0).get(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public T findOne(K id) throws Exception {
        return getEM().find(claseEntity, id);
    }

    public List<T> findAll(int max) throws Exception {
        return findAll(false, max, 0);
    }

    public List<T> findAll() throws Exception {
        return findAll(true, -1, -1);
    }

    public List<T> findAll(int maxResults, int firstResult) throws Exception {
        return findAll(false, maxResults, firstResult);
    }

    private List<T> findAll(boolean all, int maxResults, int firstResult) throws Exception {
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
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<T> findRange(final int rangoInicial, final int rangoFinal) {
        int resultados = rangoFinal - rangoInicial + 1;
        CriteriaQuery cq = getEM().getCriteriaBuilder().createQuery();
        cq.select(cq.from(claseEntity));
        Query q = getEM().createQuery(cq);
        q.setMaxResults(resultados);
        q.setFirstResult(rangoInicial);
        return q.getResultList();
    }

    public long count() {
        CriteriaQuery cq = getEM().getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(claseEntity);
        cq.select(getEM().getCriteriaBuilder().count(rt));
        Query q = getEM().createQuery(cq);
        return ((Long) q.getSingleResult());
    }

    /**
     * ejecuta un select con los atributos en attributes y efectua una paginación desde from hasta to
     *
     * @param from indice inferior
     * @param to indice superior
     * @param attributes strings con los nombres de los atributos
     * @return lista de resutados de la consulta a db
     */
    public List select(Integer from, Integer to, String... attributes) {
        EntityManager em = this.getEM();
        String selects = "";
        for (String attribute : attributes) {
            selects += "t." + attribute + ",";
        }
        selects = selects.substring(0, selects.length() - 1);
        Query q = em.createQuery("SELECT " + selects + " FROM " + claseEntity.getSimpleName() + " t");
        if (from != null) {
            q.setFirstResult(from);
        }
        if (to != null) {
            q.setMaxResults(to - from + 1);
        }
        return q.getResultList();
    }

    /**
     * ejecuta un select con los atributos en attributes y efectua una paginación desde from hasta to
     *
     * @param attributes strings con los nombres de los atributos
     * @return lista de resutados de la consulta a db
     */
    public List select(String... attributes) {
        EntityManager em = this.getEM();
        String selects = "";
        for (String attribute : attributes) {
            selects += "t." + attribute + ",";
        }
        selects = selects.substring(0, selects.length() - 1);
        Query q = em.createQuery("SELECT " + selects + " FROM " + claseEntity.getSimpleName() + " t");
        return q.getResultList();
    }

}
