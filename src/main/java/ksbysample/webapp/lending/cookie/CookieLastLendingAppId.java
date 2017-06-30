package ksbysample.webapp.lending.cookie;

import org.springframework.web.util.CookieGenerator;

/**
 * ???
 */
public class CookieLastLendingAppId extends CookieGenerator {

    public static final String COOKIE_NAME = "LastLendingAppId";

    private static final Integer COOKIE_MAX_AGE = 24 * 60 * 60 * 3; // 3日間

    /**
     *
     */
    public CookieLastLendingAppId() {
        setCookieName(COOKIE_NAME);
        setCookieMaxAge(COOKIE_MAX_AGE);
    }

}
