package ksbysample.webapp.lending.util.cookie

import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.web.util.CookieGenerator
import spock.lang.Specification

import javax.servlet.http.Cookie

class CookieUtilsTest extends Specification {

    static class CookieTest extends CookieGenerator {
        public static final String COOKIE_NAME = "TestCookie"
        public CookieTest() {
            setCookieName(COOKIE_NAME)
        }
    }

    static class CookieSample extends CookieGenerator {
        public static final String COOKIE_NAME = "SampleCookie"
        public CookieSample() {
            setCookieName(COOKIE_NAME)
        }
    }

    def "AddCookie_and_RemoveCookie_Cookieが1個の場合"() {
        setup:
        def response = new MockHttpServletResponse()

        expect:
        CookieUtils.addCookie(CookieTest.class, response, "テスト")
        Cookie[] cookies = response.getCookies()
        cookies.size() == 1
        cookies[0].name == CookieTest.COOKIE_NAME
        cookies[0].value == "テスト"
        CookieUtils.removeCookie(CookieTest.class, response)
        Cookie[] cookies2 = response.getCookies()
        cookies2[0].maxAge == -1
    }

    def "AddCookie_and_RemoveCookie_Cookieが2個の場合"() {
        setup:
        def response = new MockHttpServletResponse()

        expect:
        CookieUtils.addCookie(CookieTest.class, response, "テスト")
        CookieUtils.addCookie(CookieSample.class, response, "サンプル")
        Cookie[] cookies = response.getCookies()
        cookies.size() == 2
        for (cookie in cookies) {
            if (cookie.name == CookieTest.COOKIE_NAME) {
                cookie.value == "テスト"
            }
            else if (cookie.name == CookieSample.COOKIE_NAME) {
                cookie.value == "サンプル"
            }
        }
        CookieUtils.removeCookie(CookieSample.class, response)
        Cookie[] cookies2 = response.getCookies()
        for (cookie in cookies) {
            if (cookie.name == CookieSample.COOKIE_NAME) {
                cookie.maxAge == -1
            }
            else {
                cookie.maxAge != -1
            }
        }
    }

    def "GetCookieValueのテスト"() {
        setup:
        def request = new MockHttpServletRequest()

        expect:
        Cookie cookieTest = new Cookie(CookieTest.COOKIE_NAME, "テスト")
        Cookie cookieSample = new Cookie(CookieSample.COOKIE_NAME, "サンプル")
        request.setCookies(cookieTest, cookieSample)
        CookieUtils.getCookieValue(CookieTest.COOKIE_NAME, request).get() == "テスト"
        CookieUtils.getCookieValue(CookieSample.COOKIE_NAME, request).get() == "サンプル"
        CookieUtils.getCookieValue(CookieTest.COOKIE_NAME, null) == Optional.empty()
        CookieUtils.getCookieValue("NotExistsCookie", request) == Optional.empty()
    }

}
