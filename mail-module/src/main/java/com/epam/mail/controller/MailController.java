package com.epam.mail.controller;


import com.epam.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/")
public class MailController {

    final private MailService emailService;

    @PostMapping(path = "send")
    public void sendMessageToEmail(@RequestParam String name,
                                   @RequestParam String email,
                                   @RequestParam String message) {
        emailService.sendEmail(name, email, message);
    }

}