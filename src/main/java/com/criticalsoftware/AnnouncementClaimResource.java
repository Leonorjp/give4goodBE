package com.criticalsoftware;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/announcement-claims")
public class AnnouncementClaimResource {

    @Inject
    AnnouncementClaimRepository repository;

    @PUT
    @Path("/{id}/undo-claim")
    public Response undoClaim(@PathParam("id") String id) {
        return repository.undoClaim(id);
    }
}
