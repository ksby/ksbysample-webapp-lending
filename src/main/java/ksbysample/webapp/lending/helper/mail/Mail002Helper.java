package ksbysample.webapp.lending.helper.mail;

import ksbysample.webapp.lending.util.freemarker.FreeMarkerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Component
public class Mail002Helper {

    private final String TEMPLATE_LOCATION_TEXTMAIL = "mail/mail002-body.ftl";

    private final String FROM_ADDR = "lendingapp@sample.com";
    private final String SUBJECT = "貸出申請がありました";

    @Autowired
    private FreeMarkerUtils freeMarkerUtils;

    @Autowired
    private JavaMailSender mailSender;

    public MimeMessage createMessage(String[] toAddrList, Long lendingAppId) throws MessagingException {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        message.setFrom(FROM_ADDR);
        message.setTo(toAddrList);
        message.setSubject(SUBJECT);
        message.setText(generateTextUsingVelocity(lendingAppId), false);
        return message.getMimeMessage();
    }

    private String generateTextUsingVelocity(Long lendingAppId) {
        Map<String, Object> model = new HashMap<>();
        model.put("lendingAppId", lendingAppId);
        return freeMarkerUtils.merge(this.TEMPLATE_LOCATION_TEXTMAIL, model);
    }

}
