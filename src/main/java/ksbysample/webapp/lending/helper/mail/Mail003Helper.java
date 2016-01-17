package ksbysample.webapp.lending.helper.mail;

import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.util.velocity.VelocityUtils;
import ksbysample.webapp.lending.values.ValuesHelper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Mail003Helper {

    private final String TEMPLATE_LOCATION_TEXTMAIL = "mail/mail003-body.vm";

    private final String FROM_ADDR = "lendingapp@sample.com";
    private final String SUBJECT = "貸出申請が承認・却下されました";

    @Autowired
    private VelocityUtils velocityUtils;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ValuesHelper<?> vh;

    public MimeMessage createMessage(String toAddr, Long lendingAppId, List<LendingBook> lendingBookList)
            throws MessagingException {
        List<Mail003BookData> mail003BookDataList = lendingBookList.stream()
                .map(Mail003BookData::new)
                .collect(Collectors.toList());

        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        message.setFrom(FROM_ADDR);
        message.setTo(toAddr);
        message.setSubject(SUBJECT);
        message.setText(generateTextUsingVelocity(lendingAppId, mail003BookDataList), false);
        return message.getMimeMessage();
    }

    private String generateTextUsingVelocity(Long lendingAppId, List<Mail003BookData> mail003BookDataList) {
        Map<String, Object> model = new HashMap<>();
        model.put("lendingAppId", lendingAppId);
        model.put("mail003BookDataList", mail003BookDataList);
        return velocityUtils.merge(this.TEMPLATE_LOCATION_TEXTMAIL, model);
    }

    @Data
    public class Mail003BookData {
        private String approvalResultStr;
        private String bookName;

        public Mail003BookData(LendingBook lendingBook) {
            try {
                this.approvalResultStr = vh.getText("LendingBookApprovalResultValues", lendingBook.getApprovalResult());
                this.bookName = lendingBook.getBookName();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
