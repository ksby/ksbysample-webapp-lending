package ksbysample.webapp.lending.helper.url;

import ksbysample.webapp.lending.config.Constant;
import ksbysample.webapp.lending.config.WebSecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class UrlAfterLoginHelper {

    public static String getUrlAfterLogin(Authentication authentication) {
        String targetUrl = WebSecurityConfig.DEFAULT_SUCCESS_URL;

        // 特定の権限を持っている場合には対応するURLへリダイレクトする
        GrantedAuthority roleAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
        if (authentication.getAuthorities().contains(roleAdmin)) {
            // 管理権限 ( ROLE_ADMIN ) を持っている場合には検索対象図書館登録画面へ遷移させる
            targetUrl = Constant.URL_AFTER_LOGIN_FOR_ROLE_ADMIN;
        }

        return targetUrl;
    }

}
