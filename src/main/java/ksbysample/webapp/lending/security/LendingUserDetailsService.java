package ksbysample.webapp.lending.security;

import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.dao.UserRoleDao;
import ksbysample.webapp.lending.entity.UserInfo;
import ksbysample.webapp.lending.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class LendingUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoDao.selectByMailAddress(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException(
                    messageSource.getMessage("UserInfoUserDetailsService.usernameNotFound"
                            , new Object[]{}, LocaleContextHolder.getLocale()));
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        List<UserRole> userRoleList = userRoleDao.selectByUserId(userInfo.getUserId());
        if (userRoleList != null) {
            authorities.addAll(
                    userRoleList.stream().map(userRole -> new SimpleGrantedAuthority(userRole.getRole()))
                            .collect(Collectors.toList()));
        }

        return new LendingUserDetails(userInfo, authorities);
    }

}
