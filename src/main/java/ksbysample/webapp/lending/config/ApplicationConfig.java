package ksbysample.webapp.lending.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import javax.sql.DataSource;

import static java.util.Collections.singletonMap;

/**
 * ???
 */
@Configuration
public class ApplicationConfig {

    private final ConnectionFactory connectionFactory;

    private final MessageSource messageSource;

    private final MBeanExporter mbeanExporter;

    /**
     * ???
     *
     * @param connectionFactory {@link ConnectionFactory} bean
     * @param messageSource     {@link MessageSource} bean
     * @param mbeanExporter     {@link MBeanExporter} bean
     */
    public ApplicationConfig(ConnectionFactory connectionFactory
            , MessageSource messageSource
            , @Autowired(required = false) MBeanExporter mbeanExporter) {
        this.connectionFactory = connectionFactory;
        this.messageSource = messageSource;
        this.mbeanExporter = mbeanExporter;
    }

    /**
     * ???
     *
     * @return ???
     */
    @Bean
    public Queue inquiringStatusOfBookQueue() {
        return new Queue(Constant.QUEUE_NAME_INQUIRING_STATUSOFBOOK, false);
    }

    /**
     * ???
     *
     * @return ???
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * ???
     *
     * @return ???
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(this.connectionFactory);
        rabbitTemplate.setMessageConverter(this.messageConverter());
        return rabbitTemplate;
    }

    /**
     * ???
     *
     * @return ???
     */
    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    /**
     * javax.validation や Hibernate Validator のメッセージを ValidationMessages.properties ではなく
     * messages.properties に記述できるようにするために定義している
     *
     * @return new {@link LocalValidatorFactoryBean}
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(this.messageSource);
        return localValidatorFactoryBean;
    }

    /**
     * ???
     *
     * @return HikariCP の DataSource オブジェクト
     */
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() {
        if (mbeanExporter != null) {
            mbeanExporter.addExcludedBean("dataSource");
        }
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * 外部のWebAPIとXMLフォーマットで通信するために使用する MessageConverter
     * build.gralde に compile("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:...") を記述して
     * jackson-dataformat-xml が使用できるように設定しないと Bean は生成されない
     *
     * @return MappingJackson2XmlHttpMessageConverter オブジェクト
     */
    @SuppressWarnings({"PMD.UnnecessaryFullyQualifiedName"})
    @Bean
    @ConditionalOnClass(com.fasterxml.jackson.dataformat.xml.XmlMapper.class)
    public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter() {
        // findAndRegisterModules メソッドを呼び出して jackson-dataformat-xml が機能するようにする
        return new MappingJackson2XmlHttpMessageConverter(new XmlMapper().findAndRegisterModules());
    }

    /**
     * 以下の条件でリトライするための RetryTemplate
     * Exception 及びその継承クラスの例外が throw された時にリトライする
     * 最大５回リトライする
     * リトライ間隔は５秒固定
     *
     * @return new {@link RetryTemplate}
     */
    @Bean
    public RetryTemplate simpleRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(
                new SimpleRetryPolicy(5, singletonMap(Exception.class, true)));
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(5000);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        return retryTemplate;
    }

}
