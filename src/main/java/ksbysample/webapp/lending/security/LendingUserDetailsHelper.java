package ksbysample.webapp.lending.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * ???
 */
@Component
public class LendingUserDetailsHelper {

    /**
     * 現在ログインしているユーザのユーザIDを取得する
     *
     * @return ユーザID(user_info.user_id)
     */
    public Long getLoginUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LendingUserDetails lendingUserDetails = (LendingUserDetails) auth.getPrincipal();
        return lendingUserDetails.getUserId();
    }

}
