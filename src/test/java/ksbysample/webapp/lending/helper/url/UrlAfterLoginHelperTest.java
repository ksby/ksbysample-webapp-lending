package ksbysample.webapp.lending.helper.url;

import ksbysample.webapp.lending.Application;
import ksbysample.webapp.lending.config.Constant;
import ksbysample.webapp.lending.config.WebSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
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