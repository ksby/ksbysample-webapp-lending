package ksbysample.webapp.lending.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 認証の対象外にしたいURLがある場合には、以下のような記述を追加します
                // 複数URLがある場合はantMatchersメソッドにカンマ区切りで対象URLを複数列挙します
                // .antMatchers("/country/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/html/**").permitAll()
                .antMatchers("/encode").permitAll()
                .anyRequest().authenticated();
        http.formLogin()
                .loginPage("/")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/loginsuccess")
                .failureUrl("/")
                .usernameParameter("id")
                .passwordParameter("password")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll();
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

}
