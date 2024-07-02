package com.criticalsoftware;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import java.util.List;

// Repository for the Announcement entity
@ApplicationScoped
public class AnnouncementRepository implements PanacheMongoRepository<Announcement> {

    // Find an announcement by ID
    public Announcement findById(String id) {
        return find("_id", id).firstResult();
    }

    // List all announcements sorted by creation date (descending)
    public List<Announcement> listAllSortedByDate() {
        return listAll(Sort.by("date").descending());
    }

    // Find announcements by product description
    public List<Announcement> findByProductDescription(String description) {
        return list("product.description", description);
    }

    // Find announcements by donor ID
    public List<Announcement> findByDonorId(ObjectId donorId) {
        return list("userDonorId", donorId);
    }

    // Find announcements by donee ID
    public List<Announcement> findByDoneeId(ObjectId doneeId) {
        return list("userDoneeId", doneeId);
    }

    // Find announcements by donor and donee ID
    public List<Announcement> findByDonorAndDoneeId(ObjectId donorId, ObjectId doneeId) {
        return list("userDonorId = ?1 and userDoneeId = ?2", donorId, doneeId);
    }

    // Delete an announcement by ID
    public void deleteById(String id) {
        try {
            // Delete the announcement from the collection using the ID
            delete("_id", id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ID format", e);
        }
    }
}