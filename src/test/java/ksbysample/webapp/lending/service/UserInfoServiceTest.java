package ksbysample.webapp.lending.service;

import ksbysample.common.test.extension.db.TestDataExtension;
import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserInfoServiceTest {

    private static final String MAILADDR_TANAKA_TARO = "tanaka.taro@sample.com";

    @RegisterExtension
    @Autowired
    public TestDataExtension testDataExtension;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserInfoService userInfoService;

    @Test
    void testIncCntBadcredentials() {
        UserInfo userInfo = userInfoDao.selectByMailAddress(MAILADDR_TANAKA_TARO);
        assertThat(userInfo.getCntBadcredentials()).isZero();

        userInfoService.incCntBadcredentials(MAILADDR_TANAKA_TARO);

        UserInfo userInfo2 = userInfoDao.selectByMailAddress(MAILADDR_TANAKA_TARO);
        assertThat(userInfo2.getCntBadcredentials()).isEqualTo((short) (userInfo.getCntBadcredentials() + 1));
    }

    @Test
    void testInitCntBadcredentials() {
        userInfoService.incCntBadcredentials(MAILADDR_TANAKA_TARO);
        UserInfo userInfo = userInfoDao.selectByMailAddress(MAILADDR_TANAKA_TARO);
        assertThat(userInfo.getCntBadcredentials()).isNotZero();

        userInfoService.initCntBadcredentials(MAILADDR_TANAKA_TARO);
        userInfo = userInfoDao.selectByMailAddress(MAILADDR_TANAKA_TARO);
        assertThat(userInfo.getCntBadcredentials()).isZero();
    }

}
