package org.example.weatherapp.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.weatherapp.exceptions.IncorrectCredentialsException;
import org.example.weatherapp.exceptions.InvalidInputException;
import org.example.weatherapp.exceptions.UserNotFoundException;
import org.example.weatherapp.exceptions.UserNotVerifiedException;
import org.example.weatherapp.models.DTOs.UserLoginDTO;
import org.example.weatherapp.models.DTOs.UserRegistrationDTO;
import org.example.weatherapp.models.User;
import org.example.weatherapp.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import io.jsonwebtoken.security.Keys;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private static final SecretKey key = Keys.hmacShaKeyFor(System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8));
    // get en. variable and hash it (hide date behind "random" collection of characters)


    // @Transactional(readOnly = true)
    protected User getUserById(UUID userId) throws UserNotFoundException {
        Optional<User> tmp = userRepository.findById(userId);
        return tmp.orElseThrow(() -> new UserNotFoundException());
    }

    // @Transactional(readOnly = true)
    private void isUsernameOrEmailInUse(String username, String email) {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            throw new InvalidInputException();
        }
    }

    @Transactional
    public void createNewUser(UserRegistrationDTO userRegistrationDTO) throws MessagingException {
        isUsernameOrEmailInUse(userRegistrationDTO.getUsername(), userRegistrationDTO.getEmail());

        User user = new User(
                userRegistrationDTO.getUsername(),
                encodedPassword(userRegistrationDTO.getPassword()),
                userRegistrationDTO.getEmail(),

                userRegistrationDTO.getName(),
                userRegistrationDTO.getSurname(),

                verifyUsersEmail(),
                UUID.randomUUID().toString()
        );

        if (!verifyUsersEmail()) {
            emailService.sendVerificationEmail(user.getEmail(), user.getEmailVerificationToken(), user.getName(), user.getSurname());
        }

        userRepository.save(user);
    }

    private boolean verifyUsersEmail() {
        // method = if the user verification through email is on or off
        // verification on = after registration, user will receive an email and have to click on the link
        // verification on = when environmental variable is set to true (the method then returns false, which means that user has to be verified)

        if (System.getenv().get("verification") == null || System.getenv().get("verification").equals("false")) {
            return true; // it is empty or the en. variable is FALSE -> it is turned off -> user doesn't need to be verified (no email) -> return true
        } else {
            // vložit metodu sendVerificationEmail!
            return false; // it is turned on -> user should get value "false" as it needs to be verified (send email)
        }
    }

    private String encodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Transactional
    public void setEmailVerified(String token) {
        Optional<User> tmp = userRepository.findUserByEmailVerificationToken(token);
        User user = tmp.orElseThrow(() -> new UserNotFoundException());
        user.setUserVerified(true);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public String generateJwtToken(UserLoginDTO userLoginDTO) {
        Optional<User> tmp = userRepository.findByUsername(userLoginDTO.username());
        User user = tmp.orElseThrow(() -> new UserNotFoundException());

        if (!passwordEncoder.matches(userLoginDTO.password(), user.getPassword())) {
            throw new IncorrectCredentialsException();
        }

        if (!user.isUserVerified()) {
            throw new UserNotVerifiedException();
        }

        Claims claims = Jwts.claims()
                .setSubject(userLoginDTO.username())
                .setIssuer("WeatherApp");

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(key.getEncoded()))
                .compact();
    }
}
