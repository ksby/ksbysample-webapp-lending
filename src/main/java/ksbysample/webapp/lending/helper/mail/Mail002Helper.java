package ksbysample.webapp.lending.helper.mail;

import ksbysample.webapp.lending.util.freemarker.FreeMarkerUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Component
public class Mail002Helper {

    private static final String TEMPLATE_LOCATION_TEXTMAIL = "mail/mail002-body.ftl";

    private static final String FROM_ADDR = "lendingapp@sample.com";
    private static final String SUBJECT = "貸出申請がありました";

    private final FreeMarkerUtils freeMarkerUtils;

    private final JavaMailSender mailSender;

    /**
     * @param freeMarkerUtils ???
     * @param mailSender      ???
     */
    public Mail002Helper(FreeMarkerUtils freeMarkerUtils
            , JavaMailSender mailSender) {
        this.freeMarkerUtils = freeMarkerUtils;
        this.mailSender = mailSender;
    }

    /**
     * @param toAddrList   ???
     * @param lendingAppId ???
     * @return ???
     * @throws MessagingException
     */
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
        return freeMarkerUtils.merge(TEMPLATE_LOCATION_TEXTMAIL, model);
    }

}
