package com.criticalsoftware;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


//User Contact
public class Contact {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email may not be blank")
    private String email;

    @NotNull(message = "PhoneNumber may not be blank")
    @Max(value = 999999999, message = "Phone number must be less than 10 digits")
    private Integer phoneNumber;

    @NotBlank(message = "Address may not be blank")
    private String address;

    public Contact() {
        // It doesn't necessarily need implementation here
    }

    public Contact(String email, int phoneNumber, String address) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    //email
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //phoneNumber
    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}