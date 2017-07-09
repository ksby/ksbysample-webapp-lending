package ksbysample.webapp.lending.helper.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * ???
 */
@Service
public class EmailHelper {

    private final JavaMailSender mailSender;

    /**
     * @param mailSender ???
     */
    public EmailHelper(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * @param mailMessage ???
     */
    public void sendSimpleMail(SimpleMailMessage mailMessage) {
        mailSender.send(mailMessage);
    }

    /**
     * @param message ???
     */
    public void sendMail(MimeMessage message) {
        mailSender.send(message);
    }

}
