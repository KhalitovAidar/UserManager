package com.management.usersystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PersonDTO {
    @NotEmpty(message = "First name can't be empty")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    private String firstname;

    @NotEmpty(message = "Last name can't be empty")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
    private String lastname;

    @NotEmpty(message = "Email can't be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password can't be empty")
    private String password;
}
