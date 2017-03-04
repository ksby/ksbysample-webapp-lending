package ksbysample.webapp.lending.service.queue;

import ksbysample.webapp.lending.config.Constant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

@Service
public class InquiringStatusOfBookQueueService {

    private final MessageConverter converter;

    private final RabbitTemplate rabbitTemplate;

    /**
     * @param converter      ???
     * @param rabbitTemplate ???
     */
    public InquiringStatusOfBookQueueService(MessageConverter converter
            , RabbitTemplate rabbitTemplate) {
        this.converter = converter;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * @param lendingAppId ???
     */
    public void sendMessage(Long lendingAppId) {
        InquiringStatusOfBookQueueMessage message = new InquiringStatusOfBookQueueMessage();
        message.setLendingAppId(lendingAppId);
        rabbitTemplate.convertAndSend(Constant.QUEUE_NAME_INQUIRING_STATUSOFBOOK, message);
    }

    /**
     * @param message ???
     * @return ???
     */
    public InquiringStatusOfBookQueueMessage convertMessageToObject(Message message) {
        return (InquiringStatusOfBookQueueMessage) converter.fromMessage(message);
    }

}
