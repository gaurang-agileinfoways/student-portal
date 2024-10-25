package com.task.studentPortal.utils.services;

import com.task.studentPortal.utils.exceptions.customExceptions.InternalServerException;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class EmailServices {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmail(String toAddress, String subject, String body) throws MessagingException {
        // Try block to check for exceptions
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(toAddress);
            mailMessage.setText(body);
            mailMessage.setSubject(subject);

            // Sending the mail
            javaMailSender.send(mailMessage);
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerException();
        }
//        MimeMessagePreparator preparator = mimeMessage -> {
//            final Address recipient = new InternetAddress(toAddress);
//            mimeMessage.setFrom(new InternetAddress(DEFAULT_EMAIL));
//            mimeMessage.setRecipient(Message.RecipientType.TO, recipient);
//            mimeMessage.setSentDate(new Date());
//            mimeMessage.setSubject(subject);
//            mimeMessage.setText(body);
//        };
//
//        // Send the e-mail
//        Thread t = Thread.currentThread();
//        ClassLoader orig = t.getContextClassLoader();
//        t.setContextClassLoader(InternetAddress.class.getClassLoader());
//        try {
//            javaMailSender.send(preparator);
//        } finally {
//            t.setContextClassLoader(orig);
//        }
    }
}
