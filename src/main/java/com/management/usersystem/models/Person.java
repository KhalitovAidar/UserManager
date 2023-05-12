package com.management.usersystem.models;

import com.management.usersystem.util.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "firstname")
    @NotEmpty(message = "First name can't be empty")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    private String firstname;

    @Column(name = "lastname")
    @NotEmpty(message = "Last name can't be empty")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
    private String lastname;

    @Column(name = "email")
    @NotEmpty(message = "Email can't be empty")
    @Email
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Password can't be empty")
    private String password;

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @Column(name = "updatedat")
    private LocalDateTime updatedAt;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
