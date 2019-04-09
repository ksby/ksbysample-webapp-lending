package ksbysample.webapp.lending.security;

import ksbysample.common.test.extension.db.TestDataExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class LendingUserDetailsServiceTest {

    private static final String MAILADDR_TANAKA_TARO = "tanaka.taro@sample.com";
    private static final String MAILADDR_TEST_TARO = "test.taro@sample.com";

    @RegisterExtension
    @Autowired
    public TestDataExtension testDataExtension;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MessageSource messageSource;

    @Test
    void 存在するユーザならばUserDetailsが返る() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(MAILADDR_TANAKA_TARO);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(MAILADDR_TANAKA_TARO);
    }

    @Test
    void 存在しないユーザならばUsernameNotFoundExceptionが発生する() {
        assertThatThrownBy(() -> {
            UserDetails userDetails = userDetailsService.loadUserByUsername(MAILADDR_TEST_TARO);
        }).isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(messageSource.getMessage("UserInfoUserDetailsService.usernameNotFound"
                        , null, LocaleContextHolder.getLocale()));
    }

}
