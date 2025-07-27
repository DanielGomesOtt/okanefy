package com.okanefy.okanefy.services;

import com.okanefy.okanefy.dto.email.EmailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${smtp.mail}")
    private String email;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(EmailDTO email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.email);
        message.setTo(email.to());
        message.setSubject(email.subject());
        message.setText(email.body());
        mailSender.send(message);
    }
}
