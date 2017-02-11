package ksbysample.webapp.lending.security;

import ksbysample.common.test.rule.db.TestDataResource;
import ksbysample.webapp.lending.Application;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class LendingUserDetailsHelperTest {

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Autowired
    private UserInfoDao userInfoDao;

    @Test
    public void testGetLoginUserId() throws Exception {
        UserInfo userInfo = userInfoDao.selectById(1L);
        LendingUserDetails lendingUserDetails = new LendingUserDetails(userInfo, null);
        Authentication auth = new TestingAuthenticationToken(lendingUserDetails, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThat(LendingUserDetailsHelper.getLoginUserId()).isEqualTo(1L);
    }
}
