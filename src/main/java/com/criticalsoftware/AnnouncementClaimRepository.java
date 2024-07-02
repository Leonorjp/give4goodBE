package com.criticalsoftware;

import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AnnouncementClaimRepository implements PanacheMongoRepositoryBase<Announcement, String> {

    public Announcement findById(String id) {
        return find("_id", id).firstResult();
    }

    @Transactional
    public Response undoClaim(String id) {
        Announcement announcement = findById(id);
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
