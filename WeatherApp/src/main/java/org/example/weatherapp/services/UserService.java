package org.example.weatherapp.services;

import org.example.weatherapp.models.User;

public interface UserService {

    void save(User user);

    User find(Long id) throws Exception;
}
