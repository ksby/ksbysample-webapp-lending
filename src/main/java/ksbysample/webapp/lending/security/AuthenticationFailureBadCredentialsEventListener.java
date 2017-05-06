package ksbysample.webapp.lending.security;

import ksbysample.webapp.lending.service.UserInfoService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureBadCredentialsEventListener
        implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final UserInfoService userInfoService;

    /**
     * @param userInfoService ???
     */
    public AuthenticationFailureBadCredentialsEventListener(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        // UsernameNotFoundException は何もしない
        if (event.getException().getClass().equals(UsernameNotFoundException.class)) {
            return;
        }

        // 入力されたID(メールアドレス)の user_info.cnt_badcredentials を +1 する
        userInfoService.incCntBadcredentials(event.getAuthentication().getName());
    }

}
