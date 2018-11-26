package ksbysample.webapp.lending.config;

import ksbysample.webapp.lending.security.RoleAwareAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * ???
 */
@Configuration
public class WebSecurityConfig {

    public static final String DEFAULT_SUCCESS_URL = "/booklist";
    public static final String REMEMBERME_KEY = "ksbysample-webapp-lending";

    private final UserDetailsService userDetailsService;

    /**
     * @param userDetailsService ???
     */
    public WebSecurityConfig(@Qualifier("lendingUserDetailsService") UserDetailsService userDetailsService) {
        super();
        this.userDetailsService = userDetailsService;
    }

    /**
     * Spring Actuator の Endpoint 用 Spring Security 設定クラス
     * Spring Actuator の Endpoint だけ Basic 認証を設定する
     */
    @Configuration
    @Order(1)
    public static class ActuatorWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    // Spring Actuator の Endpoint のみ Basic認証を設定する
                    .requestMatcher(EndpointRequest.toAnyEndpoint())
                    .authorizeRequests()
                    .anyRequest().hasRole("ENDPOINT_ADMIN")
                    .and()
                    .httpBasic()
                    // Spring Actuator の Endpoint の Basic認証の時は認証するだけでサーバ側にセッションを作成しない
                    // これにより Spring Session のセッション情報保存先である Redis 上にデータが作成されなくなる
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

    }

    /**
     * Spring Security 設定クラス (Spring Actuator の Endpoint を除く)
     */
    @Configuration
    public static class FormLoginWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    // 認証の対象外にしたいURLがある場合には、以下のような記述を追加します
                    // 複数URLがある場合はantMatchersメソッドにカンマ区切りで対象URLを複数列挙します
                    // .antMatchers("/country/**").permitAll()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .antMatchers("/fonts/**").permitAll()
                    .antMatchers("/html/**").permitAll()
                    .antMatchers("/encode").permitAll()
                    .antMatchers("/urllogin").permitAll()
                    .antMatchers("/webapi/**").permitAll()
                    .antMatchers("/springMvcMemo/**").permitAll()
                    .antMatchers("/sessionsample/**").permitAll()
                    .antMatchers("/textareamemo/**").permitAll()
                    .antMatchers("/sample/**").permitAll()
                    .anyRequest().hasAnyRole("USER", "ADMIN", "APPROVER");
            http.formLogin()
                    .loginPage("/")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl(DEFAULT_SUCCESS_URL)
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
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        // AuthenticationManagerBuilder#userDetailsService の後に auth.inMemoryAuthentication() を呼び出すと
        // AuthenticationManagerBuilder の defaultUserDetailsService に
        // org.springframework.security.provisioning.InMemoryUserDetailsManager がセットされて
        // Remember Me 認証で InMemoryUserDetailsManager が使用されて DB のユーザが参照されなくなるので、
        // Remember Me 認証で使用する UserDetailsService を一番最後に呼び出す
        // ※今回の場合には auth.userDetailsService(userDetailsService) が一番最後に呼び出されるようにする
        auth.inMemoryAuthentication()
                .withUser("actuator")
                .password("{noop}xxxxxxxx")
                .roles("ENDPOINT_ADMIN");
        auth.authenticationProvider(daoAuhthenticationProvider())
                .userDetailsService(userDetailsService);
    }

}
