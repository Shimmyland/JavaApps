package org.example.projectlibrary.services.implementations;

import org.example.projectlibrary.repositories.UserRepo;
import org.example.projectlibrary.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    // dependencies
    private final UserRepo userRepo;

    @Autowired
    public UserServiceImp(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // methods


}
