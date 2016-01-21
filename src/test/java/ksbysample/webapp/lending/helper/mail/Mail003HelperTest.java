package ksbysample.webapp.lending.helper.mail;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import ksbysample.webapp.lending.Application;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.values.ValuesHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static ksbysample.webapp.lending.values.LendingBookApprovalResultValues.APPROVAL;
import static ksbysample.webapp.lending.values.LendingBookApprovalResultValues.REJECT;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class Mail003HelperTest {

    @Autowired
    private Mail003Helper mail003Helper;

    @Autowired
    private ValuesHelper vh;
    
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
                .isEqualTo(com.google.common.io.Files.toString(
                        new File("src/test/resources/ksbysample/webapp/lending/helper/mail/assertdata/003/message.txt")
                        , Charsets.UTF_8));
    }

}