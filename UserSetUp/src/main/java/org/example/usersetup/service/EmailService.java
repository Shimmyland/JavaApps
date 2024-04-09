package org.example.usersetup.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendUserVerifyEmail(String to, String token, String name) throws MessagingException;
}
