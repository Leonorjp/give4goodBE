package com.criticalsoftware.announcements;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnnouncementRequest {
    // Getters and setters
    private String id;

    // Fields
    @NotBlank(message = "User donor ID is mandatory and cannot be blank")
    private String userDonorId;

    @NotBlank(message = "Product name is mandatory and cannot be blank")
    @Size(max = 30, message = "Product name must be less than or equal to 30 characters")
    private String productName;

    @NotBlank(message = "Description is mandatory and cannot be blank")
    @Size(max = 500, message = "Description must be less than or equal to 500 characters")
    private String productDescription;

    @NotBlank(message = "Photo URL is mandatory and cannot be blank")
    private String productPhotoUrl;

    @NotBlank(message = "Category is mandatory and cannot be blank")
    @Size(max = 255, message = "Category must be less than or equal to 255 characters")
    private String productCategory;
}