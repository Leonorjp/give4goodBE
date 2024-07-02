package com.criticalsoftware;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementRequest {
    // Getters and setters
    private String id;

    @NotBlank(message = "User donor ID is mandatory and cannot be blank")
    private String userDonorId;

    @NotBlank(message = "Product name is mandatory and cannot be blank")
    @Size(max = 30, message = "Product name must be less than or equal to 30 characters")
    private String productName;

    @NotBlank(message = "Description is mandatory and cannot be blank")
    @Size(max = 500, message = "Description must be less than or equal to 500 characters")
    private String productDescription;

    @NotBlank(message = "Photo URL is mandatory and cannot be blank")
    @Size(max = 200, message = "Photo URL must be less than or equal to 200 characters")
    private String productPhotoUrl;

    @NotBlank(message = "Category is mandatory and cannot be blank")
    @Size(max = 255 , message = "Category must be less than or equal to 255 characters")
    private String productCategory;

    // Constructors
    public AnnouncementRequest() {
    }

    public AnnouncementRequest(String id,String userDonorId, String productName, String productDescription, String productPhotoUrl, String productCategory) {
        this.id = id;
        this.userDonorId = userDonorId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPhotoUrl = productPhotoUrl;
        this.productCategory = productCategory;
    }
}