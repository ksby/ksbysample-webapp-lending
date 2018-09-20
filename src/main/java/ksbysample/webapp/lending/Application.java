package ksbysample.webapp.lending;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * ???
 */
@ImportResource("classpath:applicationContext-${spring.profiles.active}.xml")
@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan("ksbysample")
@EnableRedisHttpSession
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Application {

    private static final Set<String> SPRING_PROFILES = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList("product", "develop", "unittest")));

    /**
     * Spring Boot メインメソッド
     *
     * @param args ???
     */
    public static void main(String[] args) {
        String springProfilesActive = System.getProperty("spring.profiles.active");
        if (!SPRING_PROFILES.contains(springProfilesActive)) {
            throw new UnsupportedOperationException(
                    MessageFormat.format("JVMの起動時引数 -Dspring.profiles.active で "
                                    + "develop か unittest か product を指定して下さい ( -Dspring.profiles.active={0} )。"
                            , springProfilesActive));
        }

        SpringApplication.run(Application.class, args);
    }

}
