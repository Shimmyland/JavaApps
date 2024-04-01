package org.example.weatherapp.services;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendEmailVerification(String to, String subject, String token, String username) throws MessagingException;
}
