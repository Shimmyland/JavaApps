package org.example.weatherapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private UUID id;

    // stats
    private String username;
    private String password;
    private String email;

    private String name;
    private String surname;

    private boolean userVerified;
    private String emailVerificationToken;
    // private Role role;

    // db
    @OneToMany (mappedBy = "user")
    private List<Weather> weatherList;

    // custom constructor
    public User(String username, String password, String email, String name, String surname, boolean userVerified,
                String emailVerificationToken) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.userVerified = userVerified;
        this.emailVerificationToken = emailVerificationToken;
    }
}
