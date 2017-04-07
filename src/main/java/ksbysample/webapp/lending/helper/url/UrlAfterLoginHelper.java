package ksbysample.webapp.lending.helper.url;

import ksbysample.webapp.lending.config.Constant;
import ksbysample.webapp.lending.config.WebSecurityConfig;
import ksbysample.webapp.lending.cookie.CookieLastLendingAppId;
import ksbysample.webapp.lending.util.cookie.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;

public class UrlAfterLoginHelper {

    /**
     * @param authentication ???
     * @return ???
     */
    public static String getUrlAfterLogin(Authentication authentication) {
        return getUrlAfterLogin(authentication, null);
    }

    /**
     * @param authentication ???
     * @param request        ???
     * @return ???
     */
    public static String getUrlAfterLogin(Authentication authentication, HttpServletRequest request) {
        String targetUrl = WebSecurityConfig.DEFAULT_SUCCESS_URL;

        // 特定の権限を持っている場合には対応するURLへリダイレクトする
        GrantedAuthority roleAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
        if (authentication.getAuthorities().contains(roleAdmin)) {
            // 管理権限 ( ROLE_ADMIN ) を持っている場合には検索対象図書館登録画面へ遷移させる
            targetUrl = Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN;
        }

        // LastLendingAppId Cookie に貸出申請ID をセットされている場合には貸出申請画面へリダイレクトさせる
        targetUrl = CookieUtils.getCookieValue(CookieLastLendingAppId.COOKIE_NAME, request)
                .map(cookieLastLendingAppId ->
                        String.format("%s?lendingAppId=%s", Constant.URL_LENDINGAPP, cookieLastLendingAppId))
                .orElse(targetUrl);

        return targetUrl;
    }

}
