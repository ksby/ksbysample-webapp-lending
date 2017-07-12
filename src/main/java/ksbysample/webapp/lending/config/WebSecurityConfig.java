package ksbysample.webapp.lending.config;

import ksbysample.webapp.lending.security.RoleAwareAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * ???
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String DEFAULT_SUCCESS_URL = "/booklist";
    public static final String REMEMBERME_KEY = "ksbysample-webapp-lending";

    private final UserDetailsService userDetailsService;

    /**
     * @param userDetailsService ???
     */
    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 認証の対象外にしたいURLがある場合には、以下のような記述を追加します
                // 複数URLがある場合はantMatchersメソッドにカンマ区切りで対象URLを複数列挙します
                // .antMatchers("/country/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/html/**").permitAll()
                .antMatchers("/encode").permitAll()
                .antMatchers("/urllogin").permitAll()
                .antMatchers("/webapi/**").permitAll()
                .antMatchers("/springMvcMemo/**").permitAll()
                .antMatchers("/sessionsample/**").permitAll()
                .antMatchers("/textareamemo/**").permitAll()
                .antMatchers("/sample/**").permitAll()
                .anyRequest().authenticated();
        http.formLogin()
                .loginPage("/")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl(WebSecurityConfig.DEFAULT_SUCCESS_URL)
                .usernameParameter("id")
                .passwordParameter("password")
                .successHandler(new RoleAwareAuthenticationSuccessHandler())
                .failureHandler(new ForwardAuthenticationFailureHandler("/"))
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .deleteCookies("SESSION")
                .deleteCookies("remember-me")
                .invalidateHttpSession(true)
                .permitAll()
                .and()
                .rememberMe()
                .key(REMEMBERME_KEY)
                .tokenValiditySeconds(60 * 60 * 24 * 30);
    }

    /**
     * @return ???
     */
    @Bean
    public AuthenticationProvider daoAuhthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * @param auth ???
     * @throws Exception
     */
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuhthenticationProvider())
                .userDetailsService(userDetailsService);
    }

}
