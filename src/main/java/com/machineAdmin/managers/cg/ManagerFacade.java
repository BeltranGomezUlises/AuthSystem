/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg;

import com.machineAdmin.entities.cg.Entity;
import java.util.List;

/**
 *
 * @author ulises
 * @param <T> class entity used to restrict the class of use
 */
public interface ManagerFacade<T extends Entity> {
    
    public T persist(T entity);
    
    public List<T> persistAll(List<T> entities);
    
    public List<T> persistAll(T... entities);        
    
    public boolean delete(T entity);
    
    public List<T> deleteAll(List<T> entities);
    
    public List<T> deleteAll(T... entities);
    
    public T update(T entity);
        
    public T findOne(Object id);
    
    public List<T> findAll();
    
    public List<T> findAll(int max);
    
    public long count();        
    
}
