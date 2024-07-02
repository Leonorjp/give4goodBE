package com.criticalsoftware.users;
import com.criticalsoftware.Contact;
import org.bson.types.ObjectId;

import java.time.LocalDate;

//User Data Response
public class UserResponse {

    private ObjectId id;
    private String name;
    private LocalDate dateBirth;
    private Contact contact;

    public UserResponse() {

    }

    public UserResponse(ObjectId id, String name, LocalDate dateBirth, Contact contact) {
        this.id = id;
        this.name = name;
        this.dateBirth = dateBirth;
        this.contact = contact;
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(LocalDate dateBirth) {
        this.dateBirth = dateBirth;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}