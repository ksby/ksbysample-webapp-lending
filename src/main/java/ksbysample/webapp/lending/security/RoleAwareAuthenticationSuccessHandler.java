package ksbysample.webapp.lending.security;

import ksbysample.webapp.lending.helper.url.UrlAfterLoginHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoleAwareAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws ServletException, IOException {
        // ログイン画面以外のURLを指定してアクセスされていた場合には、処理を SavedRequestAwareAuthenticationSuccessHandler へ委譲する
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        String targetUrl = UrlAfterLoginHelper.getUrlAfterLogin(authentication, request);
        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}
