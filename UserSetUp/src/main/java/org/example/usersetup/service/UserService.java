package org.example.usersetup.service;

import jakarta.mail.MessagingException;
import org.example.usersetup.dto.UserSignInDTO;
import org.example.usersetup.dto.UserSignUpDTO;
import org.example.usersetup.entity.User;

public interface UserService {

    User getUserByName(final String username);
    User getUserByToken(final String token);
    void usernameInUse(final String username);
    void emailInUser(final String email);
    void CreateUser(final UserSignUpDTO userSignUpDTO) throws MessagingException;
    String verifyUsersCredentials(final UserSignInDTO userSignInDTO);
    void setUsersToken(final String token);
}
