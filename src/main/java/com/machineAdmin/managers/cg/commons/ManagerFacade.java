/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.managers.cg.commons;

import java.util.List;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> class entity used to restrict the class of use
 */
public interface ManagerFacade<T> {
    
    public T persist(T entity) throws Exception;    
    
    public List<T> persistAll(List<T> entities) throws Exception;        
    
    public void delete(Object id) throws Exception;       
    
    public void deleteAll(List<Object> ids) throws Exception;
    
    public void update(T entity) throws Exception;
        
    public T findOne(Object id);
    
    public T findFirst();
            
    public List<T> findAll();
    
    public List<T> findAll(int max);
    
    public long count();     
    
}
