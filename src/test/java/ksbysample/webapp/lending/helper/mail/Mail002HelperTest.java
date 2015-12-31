package ksbysample.webapp.lending.helper.mail;

import com.google.common.base.Charsets;
import ksbysample.webapp.lending.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class Mail002HelperTest {

    @Autowired
    private Mail002Helper mail002Helper;

    @Test
    public void testCreateMessage() throws Exception {
        MimeMessage message = mail002Helper.createMessage(new String[]{"test@sample.com", "sample@test.co.jp"}, 1L);
        assertThat(message.getRecipients(Message.RecipientType.TO))
                .extracting(Object::toString)
                .containsOnly("test@sample.com", "sample@test.co.jp");
        assertThat(message.getContent())
                .isEqualTo(com.google.common.io.Files.toString(
                        new File("src/test/resources/ksbysample/webapp/lending/helper/mail/assertdata/002/message.txt")
                        , Charsets.UTF_8));
    }

}