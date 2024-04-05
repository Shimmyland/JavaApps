package org.example.weatherapp.repositories;

import org.example.weatherapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findUserByEmailVerificationToken(String token);
    Optional<User> findByUsername(String username);
}
