package ksbysample.webapp.lending.helper.mail;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import ksbysample.webapp.lending.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class Mail001HelperTest {

    @Autowired
    private Mail001Helper mail001Helper;

    @Test
    public void testCreateMessage() throws Exception {
        MimeMessage message = mail001Helper.createMessage("test@sample.com", 1L);
        assertThat(message.getRecipients(Message.RecipientType.TO))
                .extracting(Object::toString)
                .containsOnly("test@sample.com");
        assertThat(message.getContent())
                .isEqualTo(Files.toString(
                        new File("src/test/resources/ksbysample/webapp/lending/helper/mail/assertdata/001/message.txt")
                        , Charsets.UTF_8));
    }
}