package org.example.usersetup.utility;

import lombok.RequiredArgsConstructor;
import org.example.usersetup.exception.IncorrectCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EncoderUtility {

    private final PasswordEncoder passwordEncoder;

    public String encodedPassword(final String password) {
        return passwordEncoder.encode(password);
    }

    public void decodedPassword(final String input, final String userPassword){
        if (!passwordEncoder.matches(input, userPassword)) {
            throw new IncorrectCredentialsException("Wrong password.");
        }
    }
}
