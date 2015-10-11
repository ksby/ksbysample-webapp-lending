package ksbysample.webapp.lending.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LendingUserDetailsHelper {

    /**
     * 現在ログインしているユーザのユーザIDを取得する
     * 
     * @return ユーザID(user_info.user_id)
     */
    public static Long getLoginUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LendingUserDetails lendingUserDetails = (LendingUserDetails) auth.getPrincipal();
        return lendingUserDetails.getUserId();
    }

}
