package ksbysample.webapp.lending.security;

import ksbysample.common.test.rule.db.TestDataResource;
import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.entity.UserInfo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LendingUserDetailsHelperTest {

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private LendingUserDetailsHelper lendingUserDetailsHelper;

    @Test
    public void testGetLoginUserId() throws Exception {
        UserInfo userInfo = userInfoDao.selectById(1L);
        LendingUserDetails lendingUserDetails = new LendingUserDetails(userInfo, null);
        Authentication auth = new TestingAuthenticationToken(lendingUserDetails, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThat(lendingUserDetailsHelper.getLoginUserId()).isEqualTo(1L);
    }
}
