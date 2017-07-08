/*
 * Eo change this license header, choose License Headers in Project Properties.
 * Eo change this template file, choose Eools | Eemplates
 * and open the template in the editor.
 */
package com.machineAdmin.daos.cg;

import com.machineAdmin.daos.cg.exceptions.SQLPersistenceException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import org.jinq.jpa.JinqJPAStreamProvider;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <E> Entidad JPA a utilizar por el controlador C JPA respaldado de DaoSQLFacade
 */
public abstract class DaoSQLFacade<E extends Serializable>{
    
    private final Class<?> claseController;
    private final Class<E> claseEntity;
    private final EntityManagerFactory eMFactory;
    private final JinqJPAStreamProvider streams;    
    private final String binnacleName;    
        
    public DaoSQLFacade(EntityManagerFactory eMFactory, Class<?> claseController, Class<E> claseEntity, String binnacleName){
        this.eMFactory = eMFactory;
        this.claseController = claseController;        
        this.claseEntity = claseEntity;
        this.binnacleName = binnacleName;
        streams = new JinqJPAStreamProvider(eMFactory);
    }
        
    protected abstract Class<?> getIdAttributeType();
               
    public void persist(E entity) throws SQLPersistenceException{                     
        try {
            Method method = claseController.getMethod("create", claseEntity);
            Constructor constructor = claseController.getConstructor(EntityManagerFactory.class);
            Object t = constructor.newInstance(eMFactory);            
            method.setAccessible(true);
            method.invoke(t,entity);            
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {            
            throw new SQLPersistenceException("No fue posible persistir la entidad, cause: " + e.getMessage());
        }                        
    }
    
    public List<E> persistAll(List<E> entities){
        return entities;
    }
    
    public List<E> persistAll(E... entities){
        return Arrays.asList(entities);
    }       
    
    public void delete(Object id) throws SQLPersistenceException{        
        try {
            Method method = claseController.getMethod("destroy", this.getIdAttributeType());
            Constructor constructor = claseController.getConstructor(EntityManagerFactory.class);
            Object t = constructor.newInstance(eMFactory);            
            method.setAccessible(true);
            method.invoke(t,id);            
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new SQLPersistenceException("No fue posible eliminar la entidad, cause: " + e.getMessage());
        }          
    }
    
    public List<E> deleteAll(List<E> entities){
        return entities;
    }
    
    public List<E> deleteAll(E... entities){
        return Arrays.asList(entities);
    }
    
    public void update(E entity) throws SQLPersistenceException{
        try {
            Method method = claseController.getMethod("edit", claseEntity);
            Constructor constructor = claseController.getConstructor(EntityManagerFactory.class);
            Object t = constructor.newInstance(eMFactory);            
            method.setAccessible(true);
            method.invoke(t,entity);            
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new SQLPersistenceException("No fue posible actualizar la entidad, cause: " + e.getMessage());
        }          
    }
        
    public E findOne(Object id){
        return getEM().find(claseEntity, id);        
    }
   
    public List<E> findAll(int max){
        return findAll(false, max, 0);
    }
    
    public List<E> findAll() {
        return findAll(true, -1, -1);
    }

    public List<E> findAll(int maxResults, int firstResult) {
        return findAll(false, maxResults, firstResult);
    }

    private List<E> findAll(boolean all, int maxResults, int firstResult) {
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
    
    public long count(){
        EntityManager em = getEM();
        long count = streams.streamAll(getEM(), claseEntity).count();        
        if (em != null) em.close();        
        return count;
    }
        
    private EntityManager getEM(){
        return eMFactory.createEntityManager();
    }
    
    public String getBinnacleName(){
        return binnacleName;
    }
        
}
