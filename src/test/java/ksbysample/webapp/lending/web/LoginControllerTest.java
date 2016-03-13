package ksbysample.webapp.lending.web;

import ksbysample.common.test.rule.mockmvc.SecurityMockMvcResource;
import ksbysample.common.test.helper.SimpleRequestBuilder;
import ksbysample.common.test.rule.db.TestDataResource;
import ksbysample.webapp.lending.Application;
import ksbysample.webapp.lending.config.Constant;
import ksbysample.webapp.lending.config.WebSecurityConfig;
import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.entity.UserInfo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.isA;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(Enclosed.class)
public class LoginControllerTest {

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class ログイン画面の初期表示のテスト {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Test
        public void ログイン画面を表示する() throws Exception {
            // ログイン画面が表示されることを確認する
            mvc.noauth.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("login"))
                    .andExpect(model().hasNoErrors());
        }

    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class ログイン成功のテスト {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Test
        public void 有効なユーザ名とパスワードを入力すればログインに成功する() throws Exception {
            mvc.noauth.perform(formLogin()
                            .user("id", mvc.MAILADDR_TANAKA_TARO)
                            .password("password", "taro")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl(Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN))
                    .andExpect(authenticated().withUsername(mvc.MAILADDR_TANAKA_TARO));
        }

    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class ログインエラーのテスト {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Autowired
        private UserInfoDao userInfoDao;

        @Test
        public void 存在しないユーザ名とパスワードを入力すればログインはエラーになる() throws Exception {
            mvc.noauth.perform(formLogin()
                            .user("id", "user.notexists@sample.com")
                            .password("password", "notexists")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated())
                    .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", isA(BadCredentialsException.class)));
        }

        @Test
        public void 存在するユーザ名でもパスワードが正しくなければログインはエラーになる() throws Exception {
            mvc.noauth.perform(formLogin()
                            .user("id", mvc.MAILADDR_TANAKA_TARO)
                            .password("password", "tanaka")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated())
                    .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", isA(BadCredentialsException.class)));
        }

        @Test
        public void enabledが0のユーザならばログインはエラーになる() throws Exception {
            mvc.noauth.perform(formLogin()
                            .user("id", mvc.MAILADDR_KIMURA_MASAO)
                            .password("password", "masao")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated())
                    .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", isA(DisabledException.class)));
        }

        @Test
        public void アカウントの有効期限が切れているユーザならばログインはエラーになる() throws Exception {
            mvc.noauth.perform(formLogin()
                            .user("id", mvc.MAILADDR_ENDO_YOKO)
                            .password("password", "yoko")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated())
                    .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", isA(AccountExpiredException.class)));
        }

        @Test
        public void パスワードの有効期限が切れているユーザならばログインはエラーになる() throws Exception {
            mvc.noauth.perform(formLogin()
                            .user("id", mvc.MAILADDR_SATO_MASAHIKO)
                            .password("password", "masahiko")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated())
                    .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", isA(CredentialsExpiredException.class)));
        }

        @Test
        public void ログインを5回失敗すればアカウントはロックされる() throws Exception {
            // 1回目
            mvc.noauth.perform(formLogin()
                            .user("id", mvc.MAILADDR_TANAKA_TARO)
                            .password("password", "taro1")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated())
                    .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", isA(BadCredentialsException.class)));
            UserInfo userInfo = userInfoDao.selectByMailAddress(mvc.MAILADDR_TANAKA_TARO);
            assertThat(userInfo.getCntBadcredentials()).isEqualTo((short) 1);

            // 2回目
            mvc.noauth.perform(formLogin()
                            .user("id", mvc.MAILADDR_TANAKA_TARO)
                            .password("password", "taro2")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated())
                    .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", isA(BadCredentialsException.class)));
            userInfo = userInfoDao.selectByMailAddress(mvc.MAILADDR_TANAKA_TARO);
            assertThat(userInfo.getCntBadcredentials()).isEqualTo((short) 2);

            // 3回目
            mvc.noauth.perform(formLogin()
                            .user("id", mvc.MAILADDR_TANAKA_TARO)
                            .password("password", "taro3")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated())
                    .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", isA(BadCredentialsException.class)));
            userInfo = userInfoDao.selectByMailAddress(mvc.MAILADDR_TANAKA_TARO);
            assertThat(userInfo.getCntBadcredentials()).isEqualTo((short) 3);

            // 4回目
            mvc.noauth.perform(formLogin()
                            .user("id", mvc.MAILADDR_TANAKA_TARO)
                            .password("password", "taro4")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated())
                    .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", isA(BadCredentialsException.class)));
            userInfo = userInfoDao.selectByMailAddress(mvc.MAILADDR_TANAKA_TARO);
            assertThat(userInfo.getCntBadcredentials()).isEqualTo((short) 4);

            // 5回目 ( ここまでは BadCredentialsException.class )
            mvc.noauth.perform(formLogin()
                            .user("id", mvc.MAILADDR_TANAKA_TARO)
                            .password("password", "taro5")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated())
                    .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", isA(BadCredentialsException.class)));
            userInfo = userInfoDao.selectByMailAddress(mvc.MAILADDR_TANAKA_TARO);
            assertThat(userInfo.getCntBadcredentials()).isEqualTo((short) 5);

            // 6回目 ( アカウントがロックされているので LockedException.class に変わる )
            mvc.noauth.perform(formLogin()
                            .user("id", mvc.MAILADDR_TANAKA_TARO)
                            .password("password", "taro6")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated())
                    .andExpect(request().sessionAttribute("SPRING_SECURITY_LAST_EXCEPTION", isA(LockedException.class)));
            userInfo = userInfoDao.selectByMailAddress(mvc.MAILADDR_TANAKA_TARO);
            assertThat(userInfo.getCntBadcredentials()).isEqualTo((short) 5);
        }

    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 次回から自動的にログインするのテスト {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Test
        public void 次回から自動的にログインするをチェックすれば次はログインしていなくてもログイン後の画面にアクセスできる()
                throws Exception {
            // ログイン前にはログイン後の画面にアクセスできない
            mvc.noauth.perform(get(Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"))
                    .andExpect(unauthenticated());

            // 「次回から自動的にログインする」をチェックしてログインし、remember-me Cookie を生成する
            MockServletContext servletContext = new MockServletContext();
            org.springframework.mock.web.MockHttpServletRequest request
                    = formLogin()
                    .user("id", mvc.MAILADDR_TANAKA_TARO)
                    .password("password", "taro")
                    .buildRequest(servletContext);
            request.addParameter("remember-me", "true");
            SimpleRequestBuilder simpleRequestBuilder = new SimpleRequestBuilder(request);
            MvcResult result = mvc.noauth.perform(simpleRequestBuilder)
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl(Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN))
                    .andExpect(authenticated().withUsername(mvc.MAILADDR_TANAKA_TARO))
                    .andReturn();
            Cookie[] cookie = result.getResponse().getCookies();

            // remember-me Cookie を引き継いでログイン後の画面にアクセスするとアクセスできる
            mvc.noauth.perform(get(Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN).cookie(cookie))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(authenticated().withUsername(mvc.MAILADDR_TANAKA_TARO));

            // ログイン画面にアクセスしても有効な remember-me Cookie があればログイン後の画面にリダイレクトする
            mvc.noauth.perform(get("/").cookie(cookie))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl(Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN))
                    .andExpect(authenticated().withUsername(mvc.MAILADDR_TANAKA_TARO));
        }

    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class ログアウトのテスト {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Test
        public void 有効なユーザ名とパスワードを入力すればログインに成功する() throws Exception {
            // ログイン前にはログイン後の画面にアクセスできない
            mvc.noauth.perform(get(Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"))
                    .andExpect(unauthenticated());

            // ログインする
            MvcResult result = mvc.noauth.perform(formLogin()
                            .user("id", mvc.MAILADDR_TANAKA_TARO)
                            .password("password", "taro")
            )
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl(Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN))
                    .andExpect(authenticated().withUsername(mvc.MAILADDR_TANAKA_TARO))
                    .andReturn();
            HttpSession session = result.getRequest().getSession();
            assertThat(session).isNotNull();

            // ログインしたのでログイン後の画面にアクセスできる
            mvc.noauth.perform(get(Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN).session((MockHttpSession) session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(authenticated().withUsername(mvc.MAILADDR_TANAKA_TARO));

            // ログアウトする
            mvc.noauth.perform(get("/logout").session((MockHttpSession) session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(unauthenticated());

            // ログアウトしたのでログイン後の画面にアクセスできない
            mvc.noauth.perform(get(Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN).session((MockHttpSession) session))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"))
                    .andExpect(unauthenticated());
        }

    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class urlloginのテスト {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @RunWith(SpringJUnit4ClassRunner.class)
        @SpringApplicationConfiguration(classes = Application.class)
        @WebAppConfiguration
        public static class encodeのテスト {

            @Rule
            @Autowired
            public TestDataResource testDataResource;

            @Rule
            @Autowired
            public SecurityMockMvcResource mvc;

            @Test
            public void encodeで生成したパスワードの暗号化文字列が正しいことを確認する() throws Exception {
                MvcResult result = mvc.noauth.perform(get("/encode?password=ptest"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("text/plain;charset=UTF-8"))
                        .andReturn();
                String crypt = result.getResponse().getContentAsString();
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                assertThat(passwordEncoder.matches("ptest", crypt)).isTrue();
            }

        }

        @Test
        public void 存在するメールアドレスを指定すればログインに成功する() throws Exception {
            // ログイン前にはログイン後の画面にアクセスできない
            mvc.noauth.perform(get(WebSecurityConfig.DEFAULT_SUCCESS_URL))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"))
                    .andExpect(unauthenticated());

            // 存在するメールアドレスを指定して /urllogin にアクセスすればログインできる
            MvcResult result = mvc.noauth.perform(get("/urllogin?user=" + mvc.MAILADDR_SUZUKI_HANAKO))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl(WebSecurityConfig.DEFAULT_SUCCESS_URL))
                    .andExpect(authenticated().withUsername(mvc.MAILADDR_SUZUKI_HANAKO))
                    .andReturn();
            HttpSession session = result.getRequest().getSession();
            assertThat(session).isNotNull();

            // ログイン後の画面にアクセスしてもエラーにならない
            mvc.noauth.perform(get(WebSecurityConfig.DEFAULT_SUCCESS_URL).session((MockHttpSession) session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("booklist/booklist"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(authenticated().withUsername(mvc.MAILADDR_SUZUKI_HANAKO));
        }

    }

}
