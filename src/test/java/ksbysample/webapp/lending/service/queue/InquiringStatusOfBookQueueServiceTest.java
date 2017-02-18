package ksbysample.webapp.lending.service.queue;

import ksbysample.webapp.lending.Application;
import ksbysample.webapp.lending.config.Constant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class InquiringStatusOfBookQueueServiceTest {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private Queue queue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private InquiringStatusOfBookQueueService inquiringStatusOfBookQueueService;

    @Test
    public void testSendMessage() throws Exception {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.deleteQueue(Constant.QUEUE_NAME_INQUIRING_STATUSOFBOOK);
        rabbitAdmin.declareQueue(queue);

        Long lendingAppId = Long.valueOf(1L);
        inquiringStatusOfBookQueueService.sendMessage(lendingAppId);

        InquiringStatusOfBookQueueMessage message
                = (InquiringStatusOfBookQueueMessage) rabbitTemplate.receiveAndConvert(Constant.QUEUE_NAME_INQUIRING_STATUSOFBOOK);
        assertThat(message.getLendingAppId()).isEqualTo(lendingAppId);
    }
}
