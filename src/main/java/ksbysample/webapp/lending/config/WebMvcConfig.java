package ksbysample.webapp.lending.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;

/**
 * ???
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * Thymeleaf 3 のパフォーマンスを向上させるために SpEL コンパイラを有効にする
     *
     * @param templateEngine {@link SpringTemplateEngine} オブジェクト
     */
    @Autowired
    public void configureThymeleafSpringTemplateEngine(SpringTemplateEngine templateEngine) {
        templateEngine.setEnableSpringELCompiler(true);
    }

}
