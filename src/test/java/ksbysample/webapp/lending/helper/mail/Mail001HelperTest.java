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
public class Mail001HelperTest {

    @Autowired
    private Mail001Helper mail001Helper;

    @Value("ksbysample/webapp/lending/helper/mail/assertdata/001/message.txt")
    ClassPathResource messageTxtResource;

    @Test
    void testCreateMessage() throws Exception {
        MimeMessage message = mail001Helper.createMessage("test@sample.com", 1L);
        assertAll(
                () -> assertThat(message.getRecipients(Message.RecipientType.TO))
                        .extracting(Object::toString)
                        .containsOnly("test@sample.com"),
                () -> assertThat(message.getContent())
                        .isEqualTo(FileCopyUtils.copyToString(Files.newReader(
                                messageTxtResource.getFile(), StandardCharsets.UTF_8)))
        );
    }

}
