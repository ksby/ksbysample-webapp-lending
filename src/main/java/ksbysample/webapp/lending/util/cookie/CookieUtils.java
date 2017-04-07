package ksbysample.webapp.lending.util.cookie;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

public class CookieUtils {

    /**
     * @param clazz       ???
     * @param response    ???
     * @param cookieValue ???
     * @param <T>         ???
     */
    public static <T extends CookieGenerator> void addCookie(Class<T> clazz, HttpServletResponse response
            , String cookieValue) {
        try {
            T cookieGenerator = clazz.getConstructor().newInstance();
            cookieGenerator.addCookie(response, cookieValue);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param clazz    ???
     * @param response ???
     * @param <T>      ???
     */
    public static <T extends CookieGenerator> void removeCookie(Class<T> clazz, HttpServletResponse response) {
        try {
            T cookieGenerator = clazz.getConstructor().newInstance();
            cookieGenerator.removeCookie(response);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param cookieName ???
     * @param request    ???
     * @return ???
     */
    public static Optional<String> getCookieValue(String cookieName, HttpServletRequest request) {
        return Optional.ofNullable(request)
                .flatMap(req -> Optional.ofNullable(req.getCookies()))
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> StringUtils.equals(cookie.getName(), cookieName))
                        .map(cookie -> cookie.getValue())
                        .findFirst());
    }

}
