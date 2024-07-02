package com.criticalsoftware;
import java.util.List;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

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
}