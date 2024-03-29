package org.example.projectlibrary.services;

import org.example.projectlibrary.models.DTOs.UserDTO;
import org.example.projectlibrary.models.User;

public interface UserService {

    // registration
    boolean isEmailValid(String email);
    boolean isPasswordValid(String password);
    boolean isUsernameInUse(String username);
    boolean isEmailInUse(String email);
    User save(UserDTO userDTO);

}
