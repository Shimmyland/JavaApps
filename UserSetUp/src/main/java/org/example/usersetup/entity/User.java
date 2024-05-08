package org.example.usersetup.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
// @Builder + @AllArgsConstructor
// @ToString(of = {"id"})
// @EqualsAndHashCode(of = {"id"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;
    private String password;
    private String email;
    private String name;
    private String token;
    private Role role;

    public User(String username, String password, String email, String name, String token, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.token = token;
        this.role = role;
    }
}
