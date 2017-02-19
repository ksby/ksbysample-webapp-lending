package ksbysample.webapp.lending.service.queue;

import ksbysample.webapp.lending.config.Constant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiringStatusOfBookQueueService {

    @Autowired
    private MessageConverter converter;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @param lendingAppId ???
     */
    public void sendMessage(Long lendingAppId) {
        InquiringStatusOfBookQueueMessage message = new InquiringStatusOfBookQueueMessage();
        message.setLendingAppId(lendingAppId);
        rabbitTemplate.convertAndSend(Constant.QUEUE_NAME_INQUIRING_STATUSOFBOOK, message);
    }

    public InquiringStatusOfBookQueueMessage convertMessageToObject(Message message) {
        return (InquiringStatusOfBookQueueMessage) converter.fromMessage(message);
    }

}
