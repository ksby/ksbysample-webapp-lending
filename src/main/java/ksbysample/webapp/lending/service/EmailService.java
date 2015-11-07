package ksbysample.webapp.lending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(SimpleMailMessage mailMessage) {
        mailSender.send(mailMessage);
    }
    
    public void sendMail(MimeMessage message) {
        mailSender.send(message);
    }

}
