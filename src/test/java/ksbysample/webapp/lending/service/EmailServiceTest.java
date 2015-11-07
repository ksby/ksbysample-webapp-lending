package ksbysample.webapp.lending.service;

import ksbysample.common.test.MailServerResource;
import ksbysample.webapp.lending.Application;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class EmailServiceTest {

    @Rule
    @Autowired
    public MailServerResource mailServer;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private EmailService emailService;

    @Test
    public void testSendSimpleMail() throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("test@sample.com");
        message.setTo("xxx@yyy.zzz");
        message.setSubject("テスト");
        message.setText("これはテストです");
        emailService.sendSimpleMail(message);

        assertThat(mailServer.getMessagesCount(), is(1));
        MimeMessage receiveMessage = mailServer.getFirstMessage();
        assertThat(receiveMessage.getFrom()[0], is(new InternetAddress("test@sample.com")));
        assertThat(receiveMessage.getAllRecipients()[0], is(new InternetAddress("xxx@yyy.zzz")));
        assertThat(receiveMessage.getSubject(), is("テスト"));
        assertThat(receiveMessage.getContent(), is("これはテストです"));
    }

    @Test
    public void testSendMail() throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        message.setFrom("test@sample.com");
        message.setTo("xxx@yyy.zzz");
        message.setSubject("テスト");
        message.setText("これはテストです");
        emailService.sendMail(message.getMimeMessage());
        
        assertThat(mailServer.getMessagesCount(), is(1));
        MimeMessage receiveMessage = mailServer.getFirstMessage();
        assertThat(receiveMessage.getFrom()[0], is(new InternetAddress("test@sample.com")));
        assertThat(receiveMessage.getAllRecipients()[0], is(new InternetAddress("xxx@yyy.zzz")));
        assertThat(receiveMessage.getSubject(), is("テスト"));
        assertThat(receiveMessage.getContent(), is("これはテストです"));
    }

}
