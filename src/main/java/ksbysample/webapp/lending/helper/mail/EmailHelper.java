package ksbysample.webapp.lending.helper.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailHelper {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(SimpleMailMessage mailMessage) {
        mailSender.send(mailMessage);
    }
    
    public void sendMail(MimeMessage message) {
        mailSender.send(message);
    }

}
