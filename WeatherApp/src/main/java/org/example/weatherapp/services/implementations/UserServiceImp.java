package org.example.weatherapp.services.implementations;

import jakarta.mail.MessagingException;
import org.example.weatherapp.models.DTOs.UserDTO;
import org.example.weatherapp.models.User;
import org.example.weatherapp.repositories.UserRepository;
import org.example.weatherapp.services.EmailService;
import org.example.weatherapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImp implements UserService {

    // dependencies
    private final UserRepository userRepository;
    private final EmailService emailService;

    // constructor
    @Autowired
    public UserServiceImp(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }


    // methods
    @Override
    public User find(UUID id) throws Exception {
        Optional<User> tmp = userRepository.findById(id);
        return tmp.orElseThrow(() -> new Exception("Not found"));
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
        // special character and at least 8 characters up to 20
        String password_regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&!+=()]).{8,20}$";
        Pattern pattern = Pattern.compile(password_regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public boolean isUsernameInUse(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public boolean isEmailInUse(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public User save(UserDTO userDTO) throws MessagingException {
        User user = new User(
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail(),

                userDTO.getName(),
                userDTO.getSurname(),

                emailVerificationStatus(),
                UUID.randomUUID().toString()   // generate unique emailVerificationToken
        );


        if (!emailVerificationStatus()) {
            emailService.sendEmailVerification(user.getEmail(), "WeatherApp email verification", user.getEmailVerificationToken(), user.getUsername());
            System.out.println("Email sent.");  // manual testing
        }

        return userRepository.save(user);
    }

    @Override
    public boolean emailVerificationStatus() {
        // method = if the user verification through email is on or off
        // verification on = after registration, user will receive an email and have to click on the link
        // verification on = when environmental variable is set to true (the method then returns false, which means that user has to be verified)

        if (System.getenv().get("verification") == null || System.getenv().get("verification").equals("false")){
            return true; // it is empty or the en. variable is FALSE -> it is turned off -> user doesn't need to be verified (no email) -> return true
        } else {
            return false; // it is turned on -> user should get value "false" as it needs to be verified (send email)
        }
    }

    @Override
    public boolean usersEmailValidation(String token) {
        User user = userRepository.findUserByEmailVerificationToken(token);

        if (user != null) {
            user.setUserVerified(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
}
