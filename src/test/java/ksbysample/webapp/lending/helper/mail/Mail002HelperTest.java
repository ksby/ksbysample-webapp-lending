package ksbysample.webapp.lending.helper.mail;

import com.google.common.io.Files;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class Mail002HelperTest {

    @Autowired
    private Mail002Helper mail002Helper;

    @Value("ksbysample/webapp/lending/helper/mail/assertdata/002/message.txt")
    ClassPathResource messageTxtResource;

    @Test
    void testCreateMessage() throws Exception {
        MimeMessage message = mail002Helper.createMessage(new String[]{"test@sample.com", "sample@test.co.jp"}, 1L);
        assertAll(
                () -> assertThat(message.getRecipients(Message.RecipientType.TO))
                        .extracting(Object::toString)
                        .containsOnly("test@sample.com", "sample@test.co.jp"),
                () -> assertThat(message.getContent())
                        .isEqualTo(FileCopyUtils.copyToString(Files.newReader(
                                messageTxtResource.getFile(), StandardCharsets.UTF_8)))
        );
    }

}
