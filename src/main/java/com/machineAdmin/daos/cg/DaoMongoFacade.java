package com.machineAdmin.daos.cg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.machineAdmin.entities.cg.EntityMongo;
import com.machineAdmin.utils.UtilsDB;
import com.machineAdmin.utils.UtilsJson;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toList;
import org.mongojack.DBQuery;
import org.mongojack.DBQuery.Query;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> entidad que extienda de la clase EntityMongo
 */
public class DaoMongoFacade<T extends EntityMongo> implements DaoFacade<T> {

    protected final String collectionName;
    protected JacksonDBCollection<T, String> coll;

    protected DaoMongoFacade(String collectionName, Class<T> clazz) {
        this.collectionName = collectionName;
        this.coll = JacksonDBCollection.wrap(UtilsDB.getCollection(collectionName), clazz, String.class);
    }

    public JacksonDBCollection<T, String> getCollection() {
        return coll;
    }

    @Override
    public T persist(T entity) {
        return coll.insert(entity).getSavedObject();
    }

    @Override
    public List<T> persistAll(List<T> entities) {
        return coll.insert(entities).getSavedObjects();
    }

    @Override
    public List<T> persistAll(T... entities) {
        return coll.insert(entities).getSavedObjects();
    }

    @Override
    public boolean delete(T entity) {
        WriteResult<T, String> result = coll.removeById(entity.getId());
        try {
            System.out.println(UtilsJson.jsonSerialize(result));
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DaoMongoFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean delete(Query query) {
        coll.remove(query);
        return true;
    }

    @Override
    public List<T> deleteAll(List<T> entities) {
        List<String> ids = entities.stream().map((T e) -> e.getId()).collect(toList());
        System.out.println(ids);

        Query q = DBQuery.in("_id", ids);
        coll.remove(q);

        return entities;
    }

    @Override
    public List<T> deleteAll(T... entities) {
        List<String> ids = Arrays.stream(entities).map((T e) -> e.getId()).collect(toList());
        System.out.println(ids);

        Query q = DBQuery.in("_id", ids);
        coll.remove(q);

        return Arrays.asList(entities);
    }

    @Override
    public boolean  update(T entity) {              
        return coll.updateById(entity.getId(), entity).isUpdateOfExisting();
    }

    public List<T> update(Query query, T t) {
        return coll.update(query, t).getSavedObjects();
    }

    @Override
    public T findOne(Object id) {
        return coll.findOneById(id.toString());
    }

    public T findOne(Query query) {
        return coll.findOne(query);
    }

    @Override
    public List<T> findAll() {
        return coll.find().toArray();
    }

    public List<T> findAll(Query query) {
        return coll.find(query).toArray();
    }

    @Override
    public List<T> findAll(int max) {
        return coll.find().toArray(max);
    }

    public List<T> findAll(Query query, int max) {
        return coll.find(query).limit(max).toArray();
    }

    @Override
    public long count() {
        return coll.count();
    }

    public long count(Query q){
        return coll.getCount(q);
    }
    

}
