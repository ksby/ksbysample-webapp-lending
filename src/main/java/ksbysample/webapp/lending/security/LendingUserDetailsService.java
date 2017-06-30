package ksbysample.webapp.lending.security;

import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.dao.UserRoleDao;
import ksbysample.webapp.lending.entity.UserInfo;
import ksbysample.webapp.lending.entity.UserRole;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ???
 */
@Component
public class LendingUserDetailsService implements UserDetailsService {

    private final UserInfoDao userInfoDao;

    private final UserRoleDao userRoleDao;

    private final MessageSource messageSource;

    /**
     * @param userInfoDao   ???
     * @param userRoleDao   ???
     * @param messageSource ???
     */
    public LendingUserDetailsService(UserInfoDao userInfoDao
            , UserRoleDao userRoleDao
            , MessageSource messageSource) {
        this.userInfoDao = userInfoDao;
        this.userRoleDao = userRoleDao;
        this.messageSource = messageSource;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoDao.selectByMailAddress(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException(
                    messageSource.getMessage("UserInfoUserDetailsService.usernameNotFound"
                            , null, LocaleContextHolder.getLocale()));
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        List<UserRole> userRoleList = userRoleDao.selectByUserId(userInfo.getUserId());
        if (userRoleList != null) {
            authorities.addAll(
                    userRoleList.stream()
                            .map(userRole -> new SimpleGrantedAuthority(userRole.getRole()))
                            .collect(Collectors.toList()));
        }

        return new LendingUserDetails(userInfo, authorities);
    }

}
