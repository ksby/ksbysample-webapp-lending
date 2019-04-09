package ksbysample.webapp.lending.security;

import ksbysample.common.test.extension.db.TestDataExtension;
import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LendingUserDetailsHelperTest {

    @RegisterExtension
    @Autowired
    public TestDataExtension testDataExtension;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private LendingUserDetailsHelper lendingUserDetailsHelper;

    @Test
    void testGetLoginUserId() {
        UserInfo userInfo = userInfoDao.selectById(1L);
        LendingUserDetails lendingUserDetails = new LendingUserDetails(userInfo, null);
        Authentication auth = new TestingAuthenticationToken(lendingUserDetails, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThat(lendingUserDetailsHelper.getLoginUserId()).isEqualTo(1L);
    }
}
