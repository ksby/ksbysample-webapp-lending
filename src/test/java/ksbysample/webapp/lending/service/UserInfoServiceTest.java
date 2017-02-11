package ksbysample.webapp.lending.service;

import ksbysample.common.test.rule.db.TestDataResource;
import ksbysample.webapp.lending.Application;
import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.entity.UserInfo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UserInfoServiceTest {

    private final String MAILADDR_TANAKA_TARO = "tanaka.taro@sample.com";

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserInfoService userInfoService;

    @Test
    public void testIncCntBadcredentials() throws Exception {
        UserInfo userInfo = userInfoDao.selectByMailAddress(MAILADDR_TANAKA_TARO);
        assertThat(userInfo.getCntBadcredentials()).isZero();

        userInfoService.incCntBadcredentials(MAILADDR_TANAKA_TARO);

        UserInfo userInfo2 = userInfoDao.selectByMailAddress(MAILADDR_TANAKA_TARO);
        assertThat(userInfo2.getCntBadcredentials()).isEqualTo((short)(userInfo.getCntBadcredentials() + 1));
    }

    @Test
    public void testInitCntBadcredentials() throws Exception {
        userInfoService.incCntBadcredentials(MAILADDR_TANAKA_TARO);
        UserInfo userInfo = userInfoDao.selectByMailAddress(MAILADDR_TANAKA_TARO);
        assertThat(userInfo.getCntBadcredentials()).isNotZero();

        userInfoService.initCntBadcredentials(MAILADDR_TANAKA_TARO);
        userInfo = userInfoDao.selectByMailAddress(MAILADDR_TANAKA_TARO);
        assertThat(userInfo.getCntBadcredentials()).isZero();
    }

}