package com.criticalsoftware;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.bson.types.ObjectId;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/announcements")
public class AnnouncementResource {

    @Inject
    private AnnouncementRepository announcementRepository;

    @Inject
    private AnnouncementService announcementService;

    @Inject
    private UserRepository userRepository;

    @POST
    public Response create(@Valid AnnouncementRequest request) {
        try {
            User userDonor = userRepository.findById(new ObjectId(request.getUserDonorId()));
            if (userDonor == null) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("User donor ID does not exist.")
                        .build();
            }

            if (isProductFieldsEmpty(request)) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("All product fields must be filled.")
                        .build();
            }

            Product product = new Product(
                    request.getProductName(),
                    request.getProductDescription(),
                    request.getProductPhotoUrl(),
                    request.getProductCategory()
            );

            Announcement announcement = new Announcement(
                    product,
                    userDonor

            );

            announcement.setId(new ObjectId().toString());

            announcementRepository.persist(announcement);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Announcement created successfully with ID: " + announcement.getId());
            responseMap.put("announcement", new AnnouncementResponse(
                    announcement.getId(),
                    announcement.getProduct(),
                    announcement.getUserDonor().getId(),
                    announcement.getUserDonee().getId(),
                    announcement.getDate()
            ));

            return Response.created(new URI("/announcements/" + announcement.getId()))
                    .entity(responseMap)
                    .build();
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations().stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
            return Response.status(Status.BAD_REQUEST)
                    .entity("Validation error: " + errorMessages)
                    .build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Error in processing the request: " + e.getMessage())
                    .build();
        }
    }

    private boolean isProductFieldsEmpty(AnnouncementRequest request) {
        return request.getProductName() == null || request.getProductDescription() == null ||
                request.getProductPhotoUrl() == null || request.getProductCategory() == null;
    }

    @GET
    @Path("/donor/{donorId}")
    public Response getByDonorId(@PathParam("donorId") String donorId) {
        try {
            List<AnnouncementResponse> announcements = announcementService.getAnnouncementsByDonorId(donorId);
            if (announcements == null || announcements.isEmpty()) {
                return Response.status(Status.NOT_FOUND)
                        .entity("No announcements found for the given donor ID.")
                        .build();
            }
            return Response.ok(announcements).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Error in processing the request: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/donee/{doneeId}")
    public Response getByDoneeId(@PathParam("doneeId") String doneeId) {
        try {
            List<AnnouncementResponse> announcements = announcementService.getAnnouncementsByDoneeId(doneeId);
            if (announcements == null || announcements.isEmpty()) {
                return Response.status(Status.NOT_FOUND)
                        .entity("No announcements found for the given donee ID.")
                        .build();
            }
            return Response.ok(announcements).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Error in processing the request: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/donor/{donorId}/donee/{doneeId}")
    public Response getByDonorAndDoneeId(@PathParam("donorId") String donorId, @PathParam("doneeId") String doneeId) {
        try {
            List<AnnouncementResponse> announcements = announcementService.getAnnouncementsByDonorAndDoneeId(donorId, doneeId);
            return Response.ok(announcements).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Error in processing the request: " + e.getMessage())
                    .build();
        }
    }
}
