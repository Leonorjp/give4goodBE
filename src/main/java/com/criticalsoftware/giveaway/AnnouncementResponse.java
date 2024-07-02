package com.criticalsoftware.giveaway;

import com.criticalsoftware.Product;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

//Announcement Response
public class AnnouncementResponse {

    private String id;
    private Product product;
    private LocalDateTime date;
    private ObjectId userDonorId;
    private ObjectId userDoneeId;


    public AnnouncementResponse(String id, Product product, String userDonorId,String userDoneeId) {

    }

    public AnnouncementResponse(String id, Product product, LocalDateTime date, ObjectId userDonorId, ObjectId userDoneeId) {
        this.id = id;
        this.product = product;
        this.date = LocalDateTime.now();
        this.userDonorId = userDonorId;
        this.userDoneeId = userDoneeId;

    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //Product
    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    //Date
    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    //userDonor
    public ObjectId getUserDonorId() {
        return this.userDonorId;
    }

    public void setUserDonorId(ObjectId userDonorId) {
        this.userDonorId = userDonorId;
    }

    //userDonee
    public ObjectId getUserDoneeId() {
        return this.userDoneeId;
    }

    public void setUserDoneeId(ObjectId userDoneeId) {
        this.userDoneeId = userDoneeId;
    }

}