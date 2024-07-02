package com.criticalsoftware;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
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

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {

        // Check if the provided ID is valid
        if (!id.matches("[a-fA-F0-9]{24}")) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID format").build();
        }
        // Find the announcement by ID
        Announcement announcement = announcementRepository.findById(id);
        if (announcement == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Announcement not found.").build();
        }

        // Use the repository method to delete by ID
        announcementRepository.deleteById(id);

        // Check if the announcement still exists
        announcement = announcementRepository.findById(id);
        if (announcement != null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while deleting the announcement").build();
        }
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, AnnouncementRequest announcementRequest) {
        try {
            Announcement announcement = announcementRepository.findById(id);
            if (!id.matches("[a-fA-F0-9]{24}")) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid ID format").build();
            }

            if (announcement == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Announcement not found").build();
            }

            // Check if the product details are present and not just spaces in the request
            if (announcementRequest.getProductName() == null || announcementRequest.getProductName().trim().isEmpty() ||
                    announcementRequest.getProductDescription() == null || announcementRequest.getProductDescription().trim().isEmpty() ||
                    announcementRequest.getProductPhotoUrl() == null || announcementRequest.getProductPhotoUrl().trim().isEmpty() ||
                    announcementRequest.getProductCategory() == null || announcementRequest.getProductCategory().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Product details are missing or empty").build();
            }

            Product newProduct = new Product(announcementRequest.getProductName(), announcementRequest.getProductDescription(), announcementRequest.getProductPhotoUrl(), announcementRequest.getProductCategory());
            announcement.setProduct(newProduct);

            // Update the announcement in the repository
            announcementRepository.persistOrUpdate(announcement);

            return Response.ok(announcement).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while updating the announcement").build();
        }
    }

    @POST
    public Response create(@Valid AnnouncementRequest request) {
        try {
            User userDonor = userRepository.findById(new ObjectId(request.getUserDonorId()));
            if (userDonor == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("User donor ID does not exist.")
                        .build();
            }

            if (isProductFieldsEmpty(request)) {
                return Response.status(Response.Status.BAD_REQUEST)
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

            announcementRepository.persist(announcement);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Announcement created successfully with ID: " + announcement.id);
            responseMap.put("announcement", new AnnouncementResponse(
                    announcement.id.toString(),
                    announcement.getProduct(),
                    announcement.getUserDonor().getId(),
                    announcement.getUserDonee().getId(),
                    announcement.getDate()
            ));

            return Response.created(new URI("/announcements/" + announcement.id))
                    .entity(responseMap)
                    .build();
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations().stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Validation error: " + errorMessages)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No announcements found for the given donor ID.")
                        .build();
            }
            return Response.ok(announcements).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No announcements found for the given donee ID.")
                        .build();
            }
            return Response.ok(announcements).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error in processing the request: " + e.getMessage())
                    .build();
        }
    }
}