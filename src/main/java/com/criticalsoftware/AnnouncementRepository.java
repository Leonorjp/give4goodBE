package com.criticalsoftware;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class AnnouncementRepository implements PanacheMongoRepository<Announcement> {

    // Find an announcement by ID
    public Announcement findById(String id) {
        return find("_id",id).firstResult();
    }

    // List all announcements sorted by date
    public List<Announcement> listAllSortedByDate() {
        return listAll(Sort.by("date").descending());
    }

    // Delete an announcement by ID
    public void deleteById(String id) {
        try {
            // Delete the announcement from the collection using the ObjectId
            delete("_id", id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ID format", e);
        }
    }
}