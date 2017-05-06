package ksbysample.webapp.lending.helper.mail;

import ksbysample.webapp.lending.helper.freemarker.FreeMarkerHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Component
public class Mail001Helper {

    private static final String TEMPLATE_LOCATION_TEXTMAIL = "mail/mail001-body.ftl";

    private static final String FROM_ADDR = "StatusOfBookChecker@sample.com";
    private static final String SUBJECT = "貸出状況を確認しました";

    private final FreeMarkerHelper freeMarkerHelper;

    private final JavaMailSender mailSender;

    /**
     * @param freeMarkerHelper ???
     * @param mailSender       ???
     */
    public Mail001Helper(FreeMarkerHelper freeMarkerHelper
            , JavaMailSender mailSender) {
        this.freeMarkerHelper = freeMarkerHelper;
        this.mailSender = mailSender;
    }

    /**
     * @param toAddr       ???
     * @param lendingAppId ???
     * @return ???
     * @throws MessagingException
     */
    public MimeMessage createMessage(String toAddr, Long lendingAppId) throws MessagingException {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        message.setFrom(FROM_ADDR);
        message.setTo(toAddr);
        message.setSubject(SUBJECT);
        message.setText(generateTextUsingVelocity(lendingAppId), false);
        return message.getMimeMessage();
    }

    private String generateTextUsingVelocity(Long lendingAppId) {
        Map<String, Object> model = new HashMap<>();
        model.put("lendingAppId", lendingAppId);
        return freeMarkerHelper.merge(TEMPLATE_LOCATION_TEXTMAIL, model);
    }

}
