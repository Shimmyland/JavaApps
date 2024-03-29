package org.example.projectlibrary.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profile")       // User is reserve word for H2 DB
public class User {

    // note - for JSON ignore use @JSONIgnore


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private int tel;

    private boolean verified;       // after registration, if the user verified his email
    private String emailToken;      // unique token for verification

    // custom constructor
    public User(String username, String password, String name, String surname, String email, int tel) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.tel = tel;
    }
}
