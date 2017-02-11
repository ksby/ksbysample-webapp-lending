package ksbysample.webapp.lending.security;

import ksbysample.common.test.rule.db.TestDataResource;
import ksbysample.webapp.lending.Application;
import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.dao.UserRoleDao;
import ksbysample.webapp.lending.entity.UserInfo;
import ksbysample.webapp.lending.entity.UserRole;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class LendingUserDetailsTest {

    private final String MAILADDR_TANAKA_TARO = "tanaka.taro@sample.com";
    private final String MAILADDR_KATO_HIROSHI = "kato.hiroshi@sample.com";

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Autowired
    private UserInfoDao userInfoDao;

    @Test
    public void 利用不可能でロック中で全て有効期限切れのユーザでのテスト() {
        UserInfo userInfo = userInfoDao.selectByMailAddress(MAILADDR_KATO_HIROSHI);
        LendingUserDetails lendingUserDetails = new LendingUserDetails(userInfo, null);

        assertThat(lendingUserDetails.getUsername()).isEqualTo(userInfo.getMailAddress());
        assertThat(lendingUserDetails.getPassword()).isEqualTo(userInfo.getPassword());
        assertThat(lendingUserDetails.getName()).isEqualTo(userInfo.getUsername());
        assertThat(lendingUserDetails.getAuthorities()).isNull();
        assertThat(lendingUserDetails.isAccountNonExpired()).isFalse();
        assertThat(lendingUserDetails.isAccountNonLocked()).isFalse();
        assertThat(lendingUserDetails.isCredentialsNonExpired()).isFalse();
        assertThat(lendingUserDetails.isEnabled()).isFalse();
    }

    @Autowired
    private UserRoleDao userRoleDao;

    @Test
    public void 利用可能でロックされておらず有効期限切れもないユーザでのテスト() {
        UserInfo userInfo = userInfoDao.selectByMailAddress(MAILADDR_TANAKA_TARO);
        List<UserRole> userRoleList = userRoleDao.selectByUserId(userInfo.getUserId());
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(
                userRoleList.stream()
                        .map(userRole -> new SimpleGrantedAuthority(userRole.getRole()))
                        .collect(Collectors.toList()));

        LendingUserDetails lendingUserDetails = new LendingUserDetails(userInfo, authorities);

        assertThat(lendingUserDetails.getUsername()).isEqualTo(userInfo.getMailAddress());
        assertThat(lendingUserDetails.getPassword()).isEqualTo(userInfo.getPassword());
        assertThat(lendingUserDetails.getName()).isEqualTo(userInfo.getUsername());
        assertThat(lendingUserDetails.getAuthorities()).extracting("authority")
                .containsOnly("ROLE_USER", "ROLE_ADMIN", "ROLE_APPROVER");
        assertThat(lendingUserDetails.isAccountNonExpired()).isTrue();
        assertThat(lendingUserDetails.isAccountNonLocked()).isTrue();
        assertThat(lendingUserDetails.isCredentialsNonExpired()).isTrue();
        assertThat(lendingUserDetails.isEnabled()).isTrue();
    }

}
