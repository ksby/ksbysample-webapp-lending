package ksbysample.webapp.lending.helper.url;

import ksbysample.webapp.lending.config.Constant;
import ksbysample.webapp.lending.config.WebSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlAfterLoginHelperTest {

    @Test
    public void testGetUrlAfterLogin_管理権限がある場合() throws Exception {
        Authentication authentication = new TestingAuthenticationToken("test", "test", "ROLE_ADMIN", "ROLE_USER");
        String url = UrlAfterLoginHelper.getUrlAfterLogin(authentication);
        assertThat(url).isEqualTo(Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN);
    }

    @Test
    public void testGetUrlAfterLogin_ユーザ権限しかない場合() throws Exception {
        Authentication authentication = new TestingAuthenticationToken("test", "test", "ROLE_USER");
        String url = UrlAfterLoginHelper.getUrlAfterLogin(authentication);
        assertThat(url).isEqualTo(WebSecurityConfig.DEFAULT_SUCCESS_URL);
    }

}