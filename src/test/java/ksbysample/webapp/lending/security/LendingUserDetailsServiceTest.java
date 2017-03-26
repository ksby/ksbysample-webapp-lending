package ksbysample.webapp.lending.security;

import ksbysample.common.test.rule.db.TestDataResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LendingUserDetailsServiceTest {

    private final String MAILADDR_TANAKA_TARO = "tanaka.taro@sample.com";
    private final String MAILADDR_TEST_TARO = "test.taro@sample.com";

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MessageSource messageSource;

    @Test
    public void 存在するユーザならばUserDetailsが返る() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(MAILADDR_TANAKA_TARO);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(MAILADDR_TANAKA_TARO);
    }

    @Test
    public void 存在しないユーザならばUsernameNotFoundExceptionが発生する() {
        assertThatThrownBy(() -> {
            UserDetails userDetails = userDetailsService.loadUserByUsername(MAILADDR_TEST_TARO);
        }).isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(messageSource.getMessage("UserInfoUserDetailsService.usernameNotFound"
                        , null, LocaleContextHolder.getLocale()));
    }

}
