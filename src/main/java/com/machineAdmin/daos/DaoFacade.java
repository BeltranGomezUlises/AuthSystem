package com.machineAdmin.daos;

import com.machineAdmin.entities.Entity;
import com.machineAdmin.utils.UtilsDB;
import java.util.List;
import org.mongojack.DBQuery.Query;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;


/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T>
 */
public abstract class DaoFacade<T extends Entity> {
        
    private final String collectionName;
    JacksonDBCollection<T, String> coll;

    protected DaoFacade(String collectionName, Class<T> clazz) {
        this.collectionName = collectionName;
        this.coll = JacksonDBCollection.wrap(UtilsDB.getCollection(collectionName), clazz, String.class);
    }                
    
    public JacksonDBCollection<T,String> getCollection(){
        return coll;
    }
    
    public WriteResult<T,String> persist(T f){
        return coll.insert(f);
    }
    
    public WriteResult<T,String> persistAll(List<T> entities){
        return coll.insert(entities);
    }
    
    public WriteResult<T,String> delete(T t){
        return coll.removeById(t.getId());
    }
    
    public WriteResult<T,String> delete(Query query){                       
        return coll.remove(query);
    }
    
    public WriteResult<T,String> update(T t){        
        return coll.updateById(t.getId(), t);
    }
    
    public WriteResult<T,String> update(Query query, T t){                       
        return coll.update(query, t);
    }
       
    public T find(Object id){        
        return coll.findOneById(id.toString());
    }
    
    public T find(Query query){        
        return coll.findOne(query);
    }
        
    public List<T> findAll(){
        return coll.find().toArray();
    }
    
    public List<T> findAll(int max){        
        return coll.find().limit(max).toArray();
    }       
    
    public List<T> findAll(Query query){
        return coll.find(query).toArray();
    }
    
    public List<T> findAll(Query query, int max){
        return coll.find(query).limit(max).toArray();
    }
    
    public long count(){
        return coll.count();
    }
                
}
