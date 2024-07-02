package com.criticalsoftware;

import com.criticalsoftware.users.User;
import org.bson.types.ObjectId;

public class Product {

    private ObjectId id;
    private String name;
    private String description;
    private String photo;
    private String category;

    public Product() {
    }

    public Product(String name,String description, String photo, String category, User userDonnor) {
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.category = category;
    }

    //Name
    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //description
    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //photo
    public String getPhotoUrl(){
        return this.photo;
    }

    public void setPhotoUrl(String photo) {
        this.photo = photo;
    }

    //category
    public String getCategory(){
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //Id
    public ObjectId getId(){
        return this.id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
