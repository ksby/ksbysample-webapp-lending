package ksbysample.webapp.lending.security;

import ksbysample.webapp.lending.entity.UserInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LendingUser implements Serializable {

    String username;

    String password;

    String mailAddress;

    Short enabled;

    Short cntBadcredentials;

    LocalDateTime expiredAccount;

    LocalDateTime expiredPassword;

    public LendingUser(UserInfo userInfo) {
        BeanUtils.copyProperties(userInfo, this);
    }
    
}
