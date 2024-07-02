package com.criticalsoftware.users;

import com.criticalsoftware.Contact;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

//User Data Request
public class UserRequest {
    @NotBlank(message = "Name may not be blank")
    private String name;

    @NotNull(message ="Date Birth is mandatory")
    private LocalDate dateBirth;

    @Valid
    @NotNull(message = "Contact is mandatory" )
    private Contact contact;

    public UserRequest() {
        // It doesn't necessarily need implementation here
    }

    public UserRequest(String name, LocalDate dateBirth, Contact contact) {
        this.name = name;
        this.dateBirth = dateBirth;
        this.contact = contact;
    }

    // Getters and Setters
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