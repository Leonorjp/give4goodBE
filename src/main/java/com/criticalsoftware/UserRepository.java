package com.criticalsoftware;

import java.util.List;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {

    //This method searches for a user by the 'name' field
    public User findByName(String name) {
        return find("name", name).firstResult();
    }

    // This method retrieves all User entities from the database, sorted by the 'name' field
    public List<User> findOrderedByName() {
        return listAll(Sort.by("name"));
    }

    public User findById(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            return find("_id", objectId).firstResult();
        } catch (IllegalArgumentException e) {
            // Handle the exception (e.g., log the error, throw a new exception, etc.)
            return null;
        }
    }
}