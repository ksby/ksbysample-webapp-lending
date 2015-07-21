package ksbysample.webapp.lending.security;

import ksbysample.webapp.lending.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

public class UserInfoUserDetails implements UserDetails {

    private UserInfo userInfo;
    private final Set<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public UserInfoUserDetails(UserInfo userInfo
            , Set<? extends GrantedAuthority> authorities) {
        LocalDateTime now = LocalDateTime.now();
        this.userInfo = userInfo;
        this.authorities = authorities;
        this.accountNonExpired = !userInfo.getExpiredAccount().isBefore(now);
        this.accountNonLocked = (userInfo.getCntBadcredentials() < 5);
        this.credentialsNonExpired = !userInfo.getExpiredPassword().isBefore(now);
        this.enabled = (userInfo.getEnabled() == 1);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return userInfo.getMailAddress();
    }

    public String getName() {
        return userInfo.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
