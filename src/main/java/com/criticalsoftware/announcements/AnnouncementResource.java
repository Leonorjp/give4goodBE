package com.criticalsoftware.announcements;

import com.criticalsoftware.users.User;
import com.criticalsoftware.users.UserRepository;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/announcements")
public class AnnouncementResource {

    public static final String ID_REGEX = "[a-fA-F0-9]{24}";

    public static final String INVALID_ID_FORMAT = "Invalid ID format";

    public static final String REQUEST_ERROR = "Error in processing the request: ";

    @Inject
    AnnouncementRepository announcementRepository;

    @Inject
    AnnouncementService announcementService;

    @Inject
    UserRepository userRepository;

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        ObjectId objectId = new ObjectId(id);

        // Check if the provided ID is valid
        if (!id.matches(ID_REGEX)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(INVALID_ID_FORMAT).build();
        }
        // Find the announcement by ID
        Announcement announcement = announcementRepository.findById(objectId);
        if (announcement == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Announcement not found.").build();
        }

        // Use the repository method to delete by ID
        announcementRepository.deleteById(objectId);

        // Check if the announcement still exists
        announcement = announcementRepository.findById(objectId);
        if (announcement != null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while deleting the announcement").build();
        }
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, AnnouncementRequest announcementRequest) {
        try {
            Announcement announcement = announcementRepository.findById(new ObjectId(id));
            if (!id.matches(ID_REGEX)) {
                return Response.status(Response.Status.BAD_REQUEST).entity(INVALID_ID_FORMAT).build();
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

    // Create an announcement
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
                    userDonor.id.toString()
            );

            announcementRepository.persist(announcement);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Announcement created successfully with ID: " + announcement.id);
            responseMap.put("announcement", new AnnouncementResponse(
                    announcement.id.toString(),
                    announcement.getProduct(),
                    announcement.getUserDonorId(),
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
                    .entity(REQUEST_ERROR + e.getMessage())
                    .build();
        }
    }

    private boolean isProductFieldsEmpty(AnnouncementRequest request) {
        return request.getProductName() == null || request.getProductDescription() == null ||
                request.getProductPhotoUrl() == null || request.getProductCategory() == null;
    }

    // Get announcements by donor ID
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
                    .entity(REQUEST_ERROR + e.getMessage())
                    .build();
        }
    }

    // Get announcements by donee ID
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
                    .entity(REQUEST_ERROR + e.getMessage())
                    .build();
        }
    }

    // Get announcements by donor and donee ID
    @GET
    @Path("/donor/{donorId}/donee/{doneeId}")
    public Response getByDonorAndDoneeId(@PathParam("donorId") String donorId, @PathParam("doneeId") String doneeId) {
        try {
            List<AnnouncementResponse> announcements = announcementService.getAnnouncementsByDonorAndDoneeId(donorId, doneeId);
            return Response.ok(announcements).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(REQUEST_ERROR + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{announcementId}/userDonee/{userId}")
    public Response updateUserDonee(@PathParam("announcementId") String announcementId, @PathParam("userId") String userId) {
        try {

            // Check if the announcementId is valid
            Announcement announcement = announcementRepository.findById(new ObjectId(announcementId));
            if (!announcementId.matches(ID_REGEX)) {
                return Response.status(Response.Status.BAD_REQUEST).entity(INVALID_ID_FORMAT).build();
            }

            // Find the announcement by ID
            if (announcement == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Announcement not found").build();
            }

            // Check if the userDoneeId is valid
            User userDonee = userRepository.findById(new ObjectId((userId)));
            if (!userId.matches(ID_REGEX)) {
                return Response.status(Response.Status.BAD_REQUEST).entity(INVALID_ID_FORMAT).build();
            }

            // Find the user by ID
            if (userDonee == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }

            // Check if userDonorId and userDoneeId are not the same
            if (announcement.getUserDonorId().equals(userId)) {
                return Response.status(Response.Status.BAD_REQUEST).entity("The doner Id and donee Id cannot be the same!").build();
            }

            // Set the userDoneeId field of the announcement
            announcement.setUserDoneeId(userId);

            // Update the announcement in the repository
            announcementRepository.persistOrUpdate(announcement);

            return Response.ok(announcement).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while updating the userDonee").build();
        }
    }

    //Get by id
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        Announcement announcement = announcementRepository.findById(new ObjectId(id));
        if (announcement == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Announcement not found.").build();
        }
        AnnouncementResponse announcementResponse = new AnnouncementResponse(announcement.id.toString(), announcement.getProduct(), announcement.getUserDonorId(), announcement.getUserDoneeId(), announcement.getDate());
        return Response.ok(announcementResponse).build();
    }

    //Get ALL
    @GET
    public Response getAll() {
        List<Announcement> announcements = announcementRepository.listAll();

        List<AnnouncementResponse> announcementResponses = new ArrayList<>();
        for (Announcement announcement : announcements) {
            AnnouncementResponse announcementResponse = new AnnouncementResponse(announcement.id.toString(), announcement.getProduct(), announcement.getUserDonorId(), announcement.getUserDoneeId(), announcement.getDate());
            announcementResponses.add(announcementResponse);
        }
        return Response.ok(announcementResponses).build();
    }

    @PUT
    @Path("/{id}/undo-claim")
    public Response undoClaim(@PathParam("id") String id) {
        return announcementRepository.undoClaim(id);
    }
}