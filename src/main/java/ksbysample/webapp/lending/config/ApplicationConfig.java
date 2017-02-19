package ksbysample.webapp.lending.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@Configuration
public class ApplicationConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public Queue inquiringStatusOfBookQueue() {
        return new Queue(Constant.QUEUE_NAME_INQUIRING_STATUSOFBOOK, false);
    }

    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        return converter;
    }

    /**
     *
     * @return ???
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(this.connectionFactory);
        rabbitTemplate.setMessageConverter(this.messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

}
