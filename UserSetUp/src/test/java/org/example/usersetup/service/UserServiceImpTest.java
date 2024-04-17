package org.example.usersetup.service;

import org.example.usersetup.entity.Role;
import org.example.usersetup.entity.User;
import org.example.usersetup.exception.UserNotFoundException;
import org.example.usersetup.repository.UserRepository;
import org.example.usersetup.service.implementations.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImp userService;

    @BeforeEach
    void setUp(){
        User user = new User(
                "Simon",
                "Password1!",
                "test.test@testing.com",
                "Test",
                "Testovic",
                Role.USER
        );
        when(userRepository.findByUsername("Simon")).thenReturn(Optional.of(user));

}

    @Test
    void getUserByName_success(){
        String username = "Simon";

        User user = userService.getUserByName(username);

        assertEquals(username, user.getUsername());

    }

}
