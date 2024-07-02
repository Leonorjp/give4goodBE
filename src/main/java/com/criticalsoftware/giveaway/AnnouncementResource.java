package com.criticalsoftware.giveaway;

import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.List;


@Path("/announcements")
public class AnnouncementResource {

    @Inject
    AnnouncementRepository repository;

    //Get by id
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        Announcement announcement = repository.findById(id);
        if (announcement == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Announcement not found.").build();
        }
        AnnouncementResponse announcementResponse = new AnnouncementResponse(announcement.getId(),announcement.getProduct(), announcement.getLocalDateTime(), announcement.getUserDonorId(), announcement.getUserDoneeId());
        return Response.ok(announcementResponse).build();
    }

    //Get ALL
    @GET
    public Response getAll() {
        List<Announcement> announcements = repository.listAll();

        List<AnnouncementResponse> announcementResponses = new ArrayList<>();
        for (Announcement announcement : announcements) {
            AnnouncementResponse announcementResponse = new AnnouncementResponse(announcement.getId(),announcement.getProduct(), announcement.getLocalDateTime(), announcement.getUserDonorId(), announcement.getUserDoneeId());
            announcementResponses.add(announcementResponse);
        }
        return Response.ok(announcementResponses).build();
    }
}
