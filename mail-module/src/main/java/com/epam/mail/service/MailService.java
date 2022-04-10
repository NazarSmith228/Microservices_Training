package com.epam.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailService {

    final private JavaMailSender javaMailSender;

    public void sendEmail(String name, String email, String message) {
        log.info("User name: {}", name);
        log.info("User email: {}", email);
        log.info("Message: {}", message);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(name);
        msg.setText(message);

        javaMailSender.send(msg);
    }

}
