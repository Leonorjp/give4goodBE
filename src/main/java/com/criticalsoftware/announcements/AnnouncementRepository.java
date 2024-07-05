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
    public Announcement findById(ObjectId id) {
        return find("_id", id).firstResult();
    }

    // Find announcements by donor ID
    public List<Announcement> findByDonorId(String donorId) {
        return list("userDonorId", donorId);
    }

    // Find announcements by donee ID
    public List<Announcement> findByDoneeId(String doneeId) {
        return list("userDoneeId", doneeId);
    }

    // Find announcements by donor and donee ID
    public List<Announcement> findByDonorAndDoneeId(String donorId, String doneeId) {
        return list("userDonorId = ?1 and userDoneeId = ?2", donorId, doneeId);
    }

    // Find unclaimed announcements
    public List<Announcement> findByClaimedFalse() {
        return list("claimed", false);
    }

    // Find unclaimed announcements not owned by the given donor
    public List<Announcement> findUnclaimedNotOwnedByDonor(String donorId) {
        return list("userDonorId != ?1 and userDoneeId is null", donorId);
    }

    // Find announcements not owned by the given donor
    public List<Announcement> findNotOwnedByDonor(String donorId) {
        return list("userDonorId != ?1", donorId);
    }

    @Transactional
    public Response undoClaim(String id) {
        Announcement announcement = findById(new ObjectId(id));
        if (announcement == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Announcement not found.").build();
        }
        if (announcement.getUserDoneeId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Cannot remove donee as the announcement already has no donee.").build();
        }
        announcement.setUserDoneeId(null);
        persistOrUpdate(announcement);
        return Response.noContent().build();
    }
}
