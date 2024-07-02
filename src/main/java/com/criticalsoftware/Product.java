package com.criticalsoftware;

public class Product {

    private String id;
    private String description;
    private String photoUrl;
    private String category;
    private String name;

    public Product() {
    }

    public Product(String id, String description, String photoUrl, String category, String name) {
        this.id = id;
        this.description = description;
        this.photoUrl = photoUrl;
        this.category = category;
        this.name = name;
    }

    //description
    public String getId(){
        return this.id;
    }

    public void setId (String id) {
        this.id = id;
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
        return this.photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    //category
    public String getCategory(){
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //name
    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
