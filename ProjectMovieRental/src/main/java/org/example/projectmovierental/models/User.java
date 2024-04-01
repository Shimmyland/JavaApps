package org.example.projectmovierental.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // stats
    private String username;
    private String password;
    private String email;

    private String name;
    private String surname;

    // private boolean userVerified;
    // private String emailVerificationToken;
    // private Role role;

    // custom constructors
    public User(String username, String password, String email, String name, String surname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
    }
}
