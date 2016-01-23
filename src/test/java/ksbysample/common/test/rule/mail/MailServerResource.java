package ksbysample.common.test.rule.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.rules.ExternalResource;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

@Component
public class MailServerResource extends ExternalResource {

    private GreenMail greenMail = new GreenMail(new ServerSetup(25, "localhost", ServerSetup.PROTOCOL_SMTP));

    @Override
    protected void before() {
        greenMail.start();
    }

    @Override
    protected void after() {
        greenMail.stop();
    }

    public int getMessagesCount() {
        return greenMail.getReceivedMessages().length;
    }

    public List<MimeMessage> getMessages() {
        return Arrays.asList(greenMail.getReceivedMessages());
    }

    public MimeMessage getFirstMessage() {
        MimeMessage message = null;
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        if (receivedMessages.length > 0) {
            message = receivedMessages[0];
        }
        return message;
    }

}
