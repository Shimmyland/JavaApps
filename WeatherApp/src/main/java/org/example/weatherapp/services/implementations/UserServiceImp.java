package org.example.weatherapp.services.implementations;

import org.example.weatherapp.models.User;
import org.example.weatherapp.repositories.UserRepository;
import org.example.weatherapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    // dependencies
    private final UserRepository userRepository;

    // constructor
    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // methods
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User find(Long id) throws Exception {
        Optional<User> tmp = userRepository.findById(id);
        return tmp.orElseThrow(() -> new Exception("Not found"));
    }
}
