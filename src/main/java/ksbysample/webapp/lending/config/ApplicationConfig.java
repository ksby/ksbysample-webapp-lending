package ksbysample.webapp.lending.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import javax.sql.DataSource;

@Configuration
public class ApplicationConfig {

    private final ConnectionFactory connectionFactory;

    /**
     * @param connectionFactory ???
     */
    public ApplicationConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * @return ???
     */
    @Bean
    public Queue inquiringStatusOfBookQueue() {
        return new Queue(Constant.QUEUE_NAME_INQUIRING_STATUSOFBOOK, false);
    }

    /**
     * @return ???
     */
    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        return converter;
    }

    /**
     * @return ???
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(this.connectionFactory);
        rabbitTemplate.setMessageConverter(this.messageConverter());
        return rabbitTemplate;
    }

    /**
     * @return ???
     */
    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    /**
     * @return ???
     */
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    /**
     * @return Tomcat JDBC Connection Pool の DataSource オブジェクト
     */
    @Bean
    @ConfigurationProperties("spring.datasource.tomcat")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(org.apache.tomcat.jdbc.pool.DataSource.class)
                .build();
    }

    /**
     * 外部のWebAPIとXMLフォーマットで通信するために使用する MessageConverter
     * build.gralde に compile("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:...") を記述して
     * jackson-dataformat-xml が使用できるように設定しないと Bean は生成されない
     *
     * @return MappingJackson2XmlHttpMessageConverter オブジェクト
     */
    @Bean
    @ConditionalOnClass(com.fasterxml.jackson.dataformat.xml.XmlMapper.class)
    public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter() {
        // findAndRegisterModules メソッドを呼び出して jackson-dataformat-xml が機能するようにする
        return new MappingJackson2XmlHttpMessageConverter(new XmlMapper().findAndRegisterModules());
    }

}
