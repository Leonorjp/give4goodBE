package com.criticalsoftware.users;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "Name may not be blank")
    private String name;

    @NotNull(message = "Date Birth is mandatory")
    private LocalDate dateBirth;

    @Valid
    @NotNull(message = "Contact is mandatory")
    private Contact contact;

    @NotBlank(message = "Password is mandatory")
    private String password;
}