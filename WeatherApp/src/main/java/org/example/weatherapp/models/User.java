package org.example.weatherapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor      // pro Hibernate / Spring ?
@Entity
// @Builder
// @EqualsAndHashCode
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "user")
    private List<Weather> weatherList;

    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    private boolean userVerified;
    private String emailVerificationToken;

    public User(String username, String password, String email, String name, String surname, boolean userVerified,
                String emailVerificationToken) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.userVerified = userVerified;
        this.emailVerificationToken = emailVerificationToken;
        this.weatherList = new ArrayList<>();
    }
}
