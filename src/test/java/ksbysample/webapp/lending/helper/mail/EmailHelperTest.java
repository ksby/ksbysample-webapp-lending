package ksbysample.webapp.lending.helper.mail;

import ksbysample.common.test.extension.mail.MailServerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class EmailHelperTest {

    @RegisterExtension
    @Autowired
    public MailServerExtension mailServer;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailHelper emailHelper;

    @Test
    void testSendSimpleMail() throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("test@sample.com");
        message.setTo("xxx@yyy.zzz");
        message.setSubject("テスト");
        message.setText("これはテストです");
        emailHelper.sendSimpleMail(message);

        assertThat(mailServer.getMessagesCount()).isEqualTo(1);
        MimeMessage receiveMessage = mailServer.getFirstMessage();
        assertAll(
                () -> assertThat(receiveMessage.getFrom()[0]).isEqualTo(new InternetAddress("test@sample.com")),
                () -> assertThat(receiveMessage.getAllRecipients()[0]).isEqualTo(new InternetAddress("xxx@yyy.zzz")),
                () -> assertThat(receiveMessage.getSubject()).isEqualTo("テスト"),
                () -> assertThat(receiveMessage.getContent()).isEqualTo("これはテストです")
        );
    }

    @Test
    void testSendMail() throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        message.setFrom("test@sample.com");
        message.setTo("xxx@yyy.zzz");
        message.setSubject("テスト");
        message.setText("これはテストです");
        emailHelper.sendMail(message.getMimeMessage());

        assertThat(mailServer.getMessagesCount()).isEqualTo(1);
        MimeMessage receiveMessage = mailServer.getFirstMessage();
        assertAll(
                () -> assertThat(receiveMessage.getFrom()[0]).isEqualTo(new InternetAddress("test@sample.com")),
                () -> assertThat(receiveMessage.getAllRecipients()[0]).isEqualTo(new InternetAddress("xxx@yyy.zzz")),
                () -> assertThat(receiveMessage.getSubject()).isEqualTo("テスト"),
                () -> assertThat(receiveMessage.getContent()).isEqualTo("これはテストです")
        );
    }

}
