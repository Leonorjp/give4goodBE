package com.criticalsoftware;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

public class AnnouncementResponse {

    private String id;
    private Product product;
    private ObjectId userDonorId;
    private ObjectId userDoneeId;
    private LocalDateTime date;

    public AnnouncementResponse() {
    }

    public AnnouncementResponse(String id, Product product, ObjectId userDonorId, ObjectId userDoneeId, LocalDateTime date) {
        this.id = id;
        this.product = product;
        this.userDonorId = userDonorId;
        this.userDoneeId = userDoneeId;
        this.date = date;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ObjectId getUserDonorId() {
        return userDonorId;
    }

    public void setUserDonorId(ObjectId userDonorId) {
        this.userDonorId = userDonorId;
    }

    public ObjectId getUserDoneeId() {
        return userDoneeId;
    }

    public void setUserDoneeId(ObjectId userDoneeId) {
        this.userDoneeId = userDoneeId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
