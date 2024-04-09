package org.example.usersetup.service.implementations;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.usersetup.dto.UserSignInDTO;
import org.example.usersetup.dto.UserSignUpDTO;
import org.example.usersetup.entity.Role;
import org.example.usersetup.entity.User;
import org.example.usersetup.exception.UserNotFoundException;
import org.example.usersetup.exception.UserNotVerifiedException;
import org.example.usersetup.repository.UserRepository;
import org.example.usersetup.service.EmailService;
import org.example.usersetup.service.UserService;
import org.example.usersetup.utility.EncoderUtility;
import org.example.usersetup.utility.JwtUtility;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtUtility jwtUtility;
    private final EncoderUtility encoderUtility;


    @Override
    public User getUserByName(final String username) {
        Optional<User> tmp = userRepository.findByUsername(username);
        return tmp.orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    @Override
    public User getUserByToken(final String token) {
        Optional<User> tmp = userRepository.findByToken(token);
        return tmp.orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    @Override
    public void usernameInUse(final String username) {    // boolean / exception?
        userRepository.existsByUsername(username);

    }

    @Override
    public void emailInUser(final String email) {         // boolean / exception?
        userRepository.existsByEmail(email);
    }


    @Override
    @Transactional
    public void CreateUser(final UserSignUpDTO userSignUpDTO) throws MessagingException {
        usernameInUse(userSignUpDTO.getUsername());

        emailInUser(userSignUpDTO.getEmail());

        User user = new User(
                userSignUpDTO.getUsername(),
                encoderUtility.encodedPassword(userSignUpDTO.getPassword()),
                userSignUpDTO.getEmail(),
                userSignUpDTO.getName(),
                UUID.randomUUID().toString(),
                Role.USER
        );

        if (userRepository.count() <= 0){
            user.setRole(Role.ADMIN);
        }

        emailService.sendUserVerifyEmail(user.getEmail(), user.getToken(), user.getName());

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public String verifyUsersCredentials(final UserSignInDTO userSignInDTO) {
        User user = getUserByName(userSignInDTO.username());

        encoderUtility.decodedPassword(userSignInDTO.password(), user.getPassword());

        if (!user.getToken().equals("Token")) {
            throw new UserNotVerifiedException("User is not verified.");
        }

        return jwtUtility.createNewJwtToken(user.getUsername());
    }

    @Override
    public void setUsersToken(final String token) {
        User user = getUserByToken(token);

        user.setToken("Token");
        userRepository.save(user);
    }


}
