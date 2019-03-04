package ksbysample.webapp.lending.helper.url;

import ksbysample.webapp.lending.config.Constant;
import ksbysample.webapp.lending.config.WebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UrlAfterLoginHelperTest {

    @Test
    void testGetUrlAfterLogin_管理権限がある場合() {
        Authentication authentication = new TestingAuthenticationToken("test", "test", "ROLE_ADMIN", "ROLE_USER");
        String url = UrlAfterLoginHelper.getUrlAfterLogin(authentication);
        assertThat(url).isEqualTo(Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN);
    }

    @Test
    void testGetUrlAfterLogin_ユーザ権限しかない場合() {
        Authentication authentication = new TestingAuthenticationToken("test", "test", "ROLE_USER");
        String url = UrlAfterLoginHelper.getUrlAfterLogin(authentication);
        assertThat(url).isEqualTo(WebSecurityConfig.DEFAULT_SUCCESS_URL);
    }

}