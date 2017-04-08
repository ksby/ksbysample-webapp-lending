package ksbysample.webapp.lending.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private final Validator validator;

    /**
     * @param validator {@link Validator} bean
     */
    public WebMvcConfig(Validator validator) {
        this.validator = validator;
    }

    /**
     * Hibernate Validator のメッセージを ValidationMessages.properties ではなく
     * messages.properties に記述するために Override して {@link Validator} bean を返している
     *
     * @return {@link Validator} bean
     */
    @Override
    public Validator getValidator() {
        return validator;
    }

}
