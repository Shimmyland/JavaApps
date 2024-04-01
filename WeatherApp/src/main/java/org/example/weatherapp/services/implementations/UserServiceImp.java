package org.example.weatherapp.services.implementations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import org.example.weatherapp.models.DTOs.UserLoginDTO;
import org.example.weatherapp.models.DTOs.UserRegistrationDTO;
import org.example.weatherapp.models.User;
import org.example.weatherapp.repositories.UserRepository;
import org.example.weatherapp.services.EmailService;
import org.example.weatherapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.jsonwebtoken.security.Keys;


@Service
public class UserServiceImp implements UserService {

    // dependencies
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // constants
    SecretKey key = Keys.hmacShaKeyFor(System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8));

    // constructor
    @Autowired
    public UserServiceImp(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    // methods
    @Override
    public User find(UUID userId) throws Exception {
        Optional<User> tmp = userRepository.findById(userId);
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
    public User save(UserRegistrationDTO userRegistrationDTO) throws MessagingException {
        User user = new User(
                userRegistrationDTO.getUsername(),
                userRegistrationDTO.getPassword(),
                userRegistrationDTO.getEmail(),

                userRegistrationDTO.getName(),
                userRegistrationDTO.getSurname(),

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
    public String encodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean usersEmailVerification(String token) {
        User user = userRepository.findUserByEmailVerificationToken(token);

        if (user != null) {
            user.setUserVerified(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean validateCredentials(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByUsername(userLoginDTO.getUsername());

        return user != null && !passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());
    }

    @Override
    public String generateJwtToken(String username) {
        // User user = findByUsername(username);

        Claims claims = Jwts.claims()
                .setSubject(username)
                .setIssuer("WeatherApp");

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000);

        return Jwts.builder()
                .setClaims(claims)
                // .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(key.getEncoded()))
                .compact();
    }
}
