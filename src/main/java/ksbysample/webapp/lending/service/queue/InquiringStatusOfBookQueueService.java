package ksbysample.webapp.lending.service.queue;

import ksbysample.webapp.lending.config.Constant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiringStatusOfBookQueueService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(Long lendingAppId) {
        InquiringStatusOfBookQueueMessage message = new InquiringStatusOfBookQueueMessage();
        message.setLendingAppId(lendingAppId);
        rabbitTemplate.convertAndSend(Constant.QUEUE_NAME_INQUIRING_STATUSOFBOOK, message);
    }

}
