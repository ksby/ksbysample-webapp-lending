package ksbysample.webapp.lending.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * ???
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

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
