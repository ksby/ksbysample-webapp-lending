package ksbysample.webapp.lending.security;

import ksbysample.webapp.lending.entity.UserInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ???
 */
@Data
public class LendingUser implements Serializable {

    private static final long serialVersionUID = 511849715573196163L;

    Long userId;

    String username;

    String password;

    String mailAddress;

    Short enabled;

    Short cntBadcredentials;

    LocalDateTime expiredAccount;

    LocalDateTime expiredPassword;

    /**
     * ???
     *
     * @param userInfo ???
     */
    public LendingUser(UserInfo userInfo) {
        BeanUtils.copyProperties(userInfo, this);
    }

}
