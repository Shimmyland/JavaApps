package org.example.projectmovierental.services;

import org.example.projectmovierental.models.DTOs.InputRegistrationDTO;
import org.example.projectmovierental.models.User;

public interface UserService {

    // registration
    boolean isInputValid(InputRegistrationDTO inputRegistrationDTO);
    boolean isInputInUse(InputRegistrationDTO inputRegistrationDTO);
    User save(InputRegistrationDTO inputRegistrationDTO);


    boolean isPasswordValid(String password);
    boolean isEmailValid(String email);

}
