package ksbysample.webapp.lending.security;

import ksbysample.webapp.lending.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * ???
 */
public class LendingUserDetails implements UserDetails {

    private static final long serialVersionUID = 4775912062739295150L;

    private LendingUser lendingUser;
    private final Set<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    /**
     * @param userInfo    ???
     * @param authorities ???
     */
    public LendingUserDetails(UserInfo userInfo
            , Set<? extends GrantedAuthority> authorities) {
        LocalDateTime now = LocalDateTime.now();
        lendingUser = new LendingUser(userInfo);
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
        return lendingUser.getPassword();
    }

    /**
     * @return ???
     */
    public Long getUserId() {
        return lendingUser.getUserId();
    }

    /**
     * @return ???
     */
    @Override
    public String getUsername() {
        return lendingUser.getMailAddress();
    }

    /**
     * @return ???
     */
    public String getName() {
        return lendingUser.getUsername();
    }

    /**
     * @return ???
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * @return ???
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * @return ???
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /**
     * @return ???
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
