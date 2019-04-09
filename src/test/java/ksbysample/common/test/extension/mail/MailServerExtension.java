package ksbysample.common.test.extension.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.rules.ExternalResource;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

@Component
public class MailServerExtension extends ExternalResource
        implements BeforeEachCallback, AfterEachCallback {

    private GreenMail greenMail = new GreenMail(new ServerSetup(25, "localhost", ServerSetup.PROTOCOL_SMTP));

    @Override
    protected void before() {
        greenMail.start();
    }

    @Override
    protected void after() {
        greenMail.stop();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        before();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        after();
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
