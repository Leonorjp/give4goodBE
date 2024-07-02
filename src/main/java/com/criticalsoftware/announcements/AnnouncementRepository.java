package com.criticalsoftware.announcements;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.util.List;

// Repository for the Announcement entity
@ApplicationScoped
public class AnnouncementRepository implements PanacheMongoRepository<Announcement> {

    // Find an announcement by ID
    public Announcement findById(String id) {
        return find("_id", id).firstResult();
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

    @Transactional
    public Response undoClaim(String id) {
        Announcement announcement = findById(id);
        if (announcement == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Announcement not found.").build();
        }
        if (announcement.getUserDonee().id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Cannot remove donee as the announcement already has no donee.").build();
        }
        announcement.setUserDonee(null);
        persistOrUpdate(announcement);
        return Response.noContent().build();
    }
}