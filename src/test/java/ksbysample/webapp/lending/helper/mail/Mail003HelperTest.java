package ksbysample.webapp.lending.helper.mail;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import ksbysample.webapp.lending.entity.LendingBook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ksbysample.webapp.lending.values.lendingbook.LendingBookApprovalResultValues.APPROVAL;
import static ksbysample.webapp.lending.values.lendingbook.LendingBookApprovalResultValues.REJECT;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Mail003HelperTest {

    @Autowired
    private Mail003Helper mail003Helper;

    @Value("ksbysample/webapp/lending/helper/mail/assertdata/003/message.txt")
    ClassPathResource messageTxtResource;

    @Test
    public void testCreateMessage() throws Exception {
        List<LendingBook> lendingBookList = new ArrayList<>();
        // 1件目
        LendingBook lendingBook = new LendingBook();
        lendingBook.setBookName("x");
        lendingBook.setApprovalResult(APPROVAL.getValue());
        lendingBookList.add(lendingBook);
        // 2件目
        lendingBook = new LendingBook();
        lendingBook.setBookName(Strings.repeat("Ｘ", 128));
        lendingBook.setApprovalResult(REJECT.getValue());
        lendingBookList.add(lendingBook);

        MimeMessage message = mail003Helper.createMessage("test@sample.com", 1L, lendingBookList);
        assertThat(message.getRecipients(Message.RecipientType.TO))
                .extracting(Object::toString)
                .containsOnly("test@sample.com");
        assertThat(message.getContent())
                .isEqualTo(FileCopyUtils.copyToString(Files.newReader(
                        messageTxtResource.getFile(), StandardCharsets.UTF_8)));
    }

}