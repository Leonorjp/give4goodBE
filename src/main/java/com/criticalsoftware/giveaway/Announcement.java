package com.criticalsoftware.giveaway;

import com.criticalsoftware.Product;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;

public class Announcement extends PanacheMongoEntity {

    private String id;
    private Product product;
    private LocalDateTime date;
    private ObjectId userDonorId;
    private ObjectId userDoneeId;
    private boolean claimed;
    private String claimedBy;

    public Announcement() {}

    public Announcement(String Id, Product product, ObjectId userDonorId, ObjectId userDoneeId, boolean claimed, String claimedBy) {
        this.id = id;
        this.product = product;
        this.date = LocalDateTime.now();
        this.userDonorId = userDonorId;
        this.userDoneeId = userDoneeId;
        this.claimed = claimed;
        this.claimedBy = claimedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //Product
    public Product getProduct(){
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    //Date
    public LocalDateTime getdate(){
        return this.date;
    }

    public LocalDateTime getLocalDateTime() {
        return this.date;
    }

    //userDonor
    public ObjectId getUserDonorId(){
        return this.userDonorId;
    }

    public void setUserDonorId (ObjectId userDonorId) {
        this.userDonorId = userDonorId;
    }

    //userDonee
    public ObjectId getUserDoneeId(){
        return this.userDoneeId;
    }

    public void setUserDoneeId (ObjectId userDoneeId) {
        this.userDoneeId = userDoneeId;
    }

    //Used for boolean properties representing states as true or false.
    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public String getClaimedBy() {
        return claimedBy;
    }

    public void setClaimedBy(String claimedBy) {
        this.claimedBy = claimedBy;
    }

}
