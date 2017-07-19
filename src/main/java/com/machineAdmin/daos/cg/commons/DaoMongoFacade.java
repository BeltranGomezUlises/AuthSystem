package com.machineAdmin.daos.cg.commons;

import com.machineAdmin.entities.cg.commons.EntityMongo;
import com.machineAdmin.utils.UtilsDB;
import com.mongodb.BasicDBObject;
import java.util.List;
import org.mongojack.DBProjection;
import org.mongojack.DBQuery;
import org.mongojack.DBQuery.Query;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> entidad que extienda de la clase EntityMongo
 */
public class DaoMongoFacade<T extends EntityMongo>{

    protected final String collectionName;
    protected JacksonDBCollection<T, String> coll;

    public DaoMongoFacade(String collectionName, Class<T> clazz) {
        this.collectionName = collectionName;
        this.coll = JacksonDBCollection.wrap(UtilsDB.getCollection(collectionName), clazz, String.class);
    }

    public JacksonDBCollection<T, String> getCollection() {
        return coll;
    }

    public T persist(T entity) {
        return coll.insert(entity).getSavedObject();
    }

    public List<T> persistAll(List<T> entities) {
        return coll.insert(entities).getSavedObjects();
    }

    public List<T> persistAll(T... entities) {
        return coll.insert(entities).getSavedObjects();
    }

    public boolean delete(Object id) {
        WriteResult<T, String> result = coll.removeById(id.toString());        
        return true;
    }

    public void delete(Query query) {
        coll.remove(query);
    }

    public void deleteAll(List<Object> ids) {
        Query q = DBQuery.in("_id", ids);
        coll.remove(q);
    }

    public void deleteAll(Object... ids) {       
        Query q = DBQuery.in("_id", ids);
        coll.remove(q);
    }

    public void update(T entity) throws Exception {
//        try {
//            T toUpdate = coll.findOneById(entity.getId());
//            BeanUtils.copyProperties(toUpdate, entity);
//            coll.updateById(entity.getId(), toUpdate);            
//        } catch (IllegalAccessException | InvocationTargetException ex) {
//            throw new Exception("No fue posible actualizar la entidad, Causa: " + ex.getMessage());
//        }
          coll.updateById(entity.getId(), entity);            
    }

    public List<T> update(Query query, T t) {                   
        return coll.update(query, t).getSavedObjects();       
    }

    public T findFirst(){
        return coll.findOne();
    }
        
    public T findOne(Object id) {
        return coll.findOneById(id.toString());
    }

    public T findOne(Object id, String... attributesProject) {
        return coll.findOneById(id.toString(), DBProjection.include(attributesProject));
    }

    public T findOne(Query query) {
        return coll.findOne(query);
    }

    public T findOne(Query query, String... attributesProject) {
        return coll.findOne(query, DBProjection.include(attributesProject));
    }

    public List<T> findAll() {
        return coll.find().toArray();
    }

    public List<T> findAll(String... attributesProject) {
        BasicDBObject keys = new BasicDBObject();
        for (String attribute : attributesProject) {
            keys.put(attribute, 1);
        }
        return coll.find(new BasicDBObject(), keys).toArray();
    }

    public List<T> findAll(Query query, String... attributesProject) {
        BasicDBObject keys = new BasicDBObject();
        for (String attribute : attributesProject) {
            keys.put(attribute, 1);
        }
        return coll.find(query, keys).toArray();
    }

    public List<T> findAll(int max) {
        return coll.find().toArray(max);
    }

    public List<T> findAll(int max, String... attributesProject) {
        BasicDBObject keys = new BasicDBObject();
        for (String attribute : attributesProject) {
            keys.put(attribute, 1);
        }
        return coll.find(new BasicDBObject(), keys).toArray(max);
    }

    public List<T> findAll(Query query, int max) {
        return coll.find(query).limit(max).toArray();
    }

    public List<T> findAll(Query query, int max, String... attributesProject) {
        BasicDBObject keys = new BasicDBObject();
        for (String attribute : attributesProject) {
            keys.put(attribute, 1);
        }
        return coll.find(query, keys).limit(max).toArray();
    }

    public long count() {
        return coll.count();
    }

    public long count(Query q) {
        return coll.getCount(q);
    }

}
