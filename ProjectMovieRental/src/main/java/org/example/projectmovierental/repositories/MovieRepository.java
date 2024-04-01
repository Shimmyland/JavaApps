package org.example.projectmovierental.repositories;

import org.example.projectmovierental.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie, UUID> {

    List<Movie> findAllByTitleContainingIgnoreCase(String query);

}
