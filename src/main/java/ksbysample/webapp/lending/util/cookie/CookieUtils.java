package ksbysample.webapp.lending.util.cookie;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

public class CookieUtils {

    public static <T extends CookieGenerator> void addCookie(Class<T> clazz, HttpServletResponse response, String cookieValue) {
        try {
            T cookieGenerator = clazz.newInstance();
            cookieGenerator.addCookie(response, cookieValue);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends CookieGenerator> void removeCookie(Class<T> clazz, HttpServletResponse response) {
        try {
            T cookieGenerator = clazz.newInstance();
            cookieGenerator.removeCookie(response);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCookieValue(String cookieName, HttpServletRequest request) {
        Optional<String> result = Optional.empty();
        if (request != null) {
            Cookie[] cookies = request.getCookies();
            result = Arrays.asList(cookies).stream()
                    .filter(cookie -> StringUtils.equals(cookie.getName(), cookieName))
                    .map(cookie -> cookie.getValue())
                    .findFirst();
        }
        return result.orElse(null);
    }

}
