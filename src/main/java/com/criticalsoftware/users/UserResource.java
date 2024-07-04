package com.criticalsoftware.users;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.bson.types.ObjectId;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
public class UserResource {

    @Inject
    UserRepository repository;

    @POST
    public Response create(@Valid UserRequest userRequest) {
        try {
            if (repository.findByEmail(userRequest.getContact().getEmail()) != null) {
                return Response.status(Status.CONFLICT).entity("Email already in use").build();
            }
            User user = new User(userRequest.getName(), userRequest.getDateBirth(), userRequest.getContact());
            repository.persist(user);
            return Response.created(new URI("/users/" + user.id))
                    .entity("User created successfully with ID: " + user.id)
                    .build();
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations().stream().map(violation -> violation.getPropertyPath() + ": " + violation.getMessage()).collect(Collectors.joining(", "));

            return Response.status(Status.BAD_REQUEST).entity("Validation error: " + errorMessages).build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error creating user: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        User user = repository.findById(new ObjectId(id));
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
        }
        UserResponse userResponse = new UserResponse(user.id, user.getName(), user.getDateBirth(), user.getContact());
        return Response.ok(userResponse).build();
    }

    @GET
    public Response getAll() {
        List<User> users = repository.listAll();

        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = new UserResponse(user.id, user.getName(), user.getDateBirth(), user.getContact());
            userResponses.add(userResponse);
        }
        return Response.ok(userResponses).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, Contact contact) {
        try {
            ObjectId objectId = new ObjectId(id);
            User userFromDb = repository.findById(objectId);
            if (userFromDb == null) {
                return Response.status(Status.NOT_FOUND).entity("User not found").build();
            }
            userFromDb.setContact(contact);
            repository.update(userFromDb);
            return Response.ok(contact).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST).entity("Invalid user ID").build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("An error occurred while updating the user").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            User user = repository.findById(objectId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
            }
            repository.delete(user);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST).entity("Invalid user ID").build();
        } catch (Exception e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("An error occurred while deleting the user").build();
        }
    }
}