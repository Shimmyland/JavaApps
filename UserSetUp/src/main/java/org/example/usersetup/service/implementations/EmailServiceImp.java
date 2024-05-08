package org.example.usersetup.service.implementations;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.example.usersetup.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImp implements EmailService {

    private final JavaMailSender mailSender;


    //  @Value("${homePageUrl}");
    //  private String homePageUrl;


    // For our purposes we are going to use Java Mail Sender to send e-mails.
    // It automatically connects to the application.properties file and derives the required web mail set up information,
    // like smtp url, username and password.


    // Here we use the MimeMessage in combination with MimeMessageHelper
    // to compose our message.
    //
    // "MIME" stands for Multipurpose Internet Mail Extensions. It is an
    // Internet standard that extends the format of email messages to
    // support text in character sets other than ASCII, as well as attachments
    // of audio, video, images, and application programs.
    //
    // We can send messages without MimeMessageHelper, but it is really cumbersome, look:
    //
    // mimeMessage.setFrom(new InternetAddress("from@example.com"));
    // mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("to@example.com"));
    // mimeMessage.setSubject("Your Subject");
    // mimeMessage.setContent("<h1>Hello</h1><p>This is an HTML email.</p>", "text/html");
    //
    // mailSender.send(mimeMessage);
    //
    // Now compare it to the MimeMessageHelper:


    @Override
    public void sendUserVerifyEmail(final String to, final String token, final String name) throws MessagingException {

        // vymyslet validaci
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("UserSetUp - verify your email");
        helper.setText("<p>Hello, " + name + "!</p>" +
                "<p>In order to verify your e-mail address, please, <a href=\"" +
                System.getenv().get("homePageUrl") + "/users/verify-email/" +
                token + " \">" + "click this link</a>.</p>", true);
        //  setText - použít stringBuilder

        mailSender.send(message);
    }


    //    We can also send messages using SimpleMailMessage, but not HTMLs
    //      or messages that contain various attachments. This would be nice
    //      if we were trying to send really simple messages. Hence, the name.

    //    public void sendEmail(String to, String subject, String body) {
    //        SimpleMailMessage message = new SimpleMailMessage();
    //
    //        message.setFrom("example@example.com");
    //        message.setTo(to);
    //        message.setSubject(subject);
    //        message.setText(body);
    //
    //        mailSender.send(message);
    //    }
}
