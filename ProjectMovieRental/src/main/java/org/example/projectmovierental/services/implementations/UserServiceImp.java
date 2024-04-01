package org.example.projectmovierental.services.implementations;

import org.example.projectmovierental.models.DTOs.InputRegistrationDTO;
import org.example.projectmovierental.models.User;
import org.example.projectmovierental.repositories.UserRepository;
import org.example.projectmovierental.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public boolean isInputValid(InputRegistrationDTO inputRegistrationDTO) {
        return isEmailValid(inputRegistrationDTO.getEmail()) && isPasswordValid(inputRegistrationDTO.getPassword());
    }

    @Override
    public boolean isEmailValid(String email) {
        String email_regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(email_regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    @Override
    public boolean isPasswordValid(String password) {
        // has to have digit (0-9),
        // upper and lower case letter (a-z, A-Z),
        // special character
        // at least 8 characters up to 20
        String password_regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&!+=()]).{8,20}$";
        Pattern pattern = Pattern.compile(password_regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public boolean isInputInUse(InputRegistrationDTO inputRegistrationDTO) {
        return userRepository.existsByUsernameIgnoreCase(inputRegistrationDTO.getUsername()) || userRepository.existsByEmailIgnoreCase(inputRegistrationDTO.getEmail());
    }

    @Override
    public User save(InputRegistrationDTO inputRegistrationDTO) {
        User user = new User(
                inputRegistrationDTO.getUsername(),
                inputRegistrationDTO.getPassword(),
                inputRegistrationDTO.getEmail(),
                inputRegistrationDTO.getName(),
                inputRegistrationDTO.getSurname()
        );

        return userRepository.save(user);
    }
}
