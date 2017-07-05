/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.daos.cg;

import com.machineAdmin.entities.cg.Entity;
import java.util.List;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T>
 */
public interface DaoFacade<T extends Entity> {
    
    public T persist(T entity);
    
    public List<T> persistAll(List<T> entities);
    
    public List<T> persistAll(T... entities);        
    
    public boolean delete(T entity);
    
    public List<T> deleteAll(List<T> entities);
    
    public List<T> deleteAll(T... entities);
    
    public boolean  update(T entity);
        
    public T findOne(Object id);
    
    public List<T> findAll();
    
    public List<T> findAll(int max);
    
    public long count();
    
}
