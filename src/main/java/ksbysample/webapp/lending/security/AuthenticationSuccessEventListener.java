package ksbysample.webapp.lending.security;

import ksbysample.webapp.lending.service.UserInfoService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * ???
 */
@Component
public class AuthenticationSuccessEventListener
        implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserInfoService userInfoService;

    /**
     * @param userInfoService ???
     */
    public AuthenticationSuccessEventListener(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // ログインしたユーザのユーザ名(username)の user_info.cnt_badcredentials を 0 にする
        userInfoService.initCntBadcredentials(event.getAuthentication().getName());
    }

}
