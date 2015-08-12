package ksbysample.webapp.lending;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.io.Serializable;
import java.text.MessageFormat;

@ImportResource("classpath:applicationContext-${spring.profiles.active}.xml")
@SpringBootApplication
@ComponentScan("ksbysample")
@EnableRedisHttpSession
public class Application {

    public static void main(String[] args) {
        String springProfilesActive = System.getProperty("spring.profiles.active");
        if (!StringUtils.equals(springProfilesActive, "product")
                && !StringUtils.equals(springProfilesActive, "develop")
                && !StringUtils.equals(springProfilesActive, "unittest")) {
            throw new UnsupportedOperationException(MessageFormat.format("JVMの起動時引数 -Dspring.profiles.active で develop か unittest か product を指定して下さい ( -Dspring.profiles.active={0} )。", springProfilesActive));
        }

        SpringApplication.run(Application.class, args);
    }

}
