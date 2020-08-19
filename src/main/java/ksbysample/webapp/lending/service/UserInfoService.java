package ksbysample.webapp.lending.service;

import ksbysample.webapp.lending.dao.UserInfoDao;
import org.springframework.stereotype.Service;

/**
 * ???
 */
@Service
public class UserInfoService {

    private final UserInfoDao userInfoDao;

    /**
     * ???
     *
     * @param userInfoDao ???
     */
    public UserInfoService(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    /**
     * ???
     *
     * @param mailAddress ???
     */
    public void incCntBadcredentials(String mailAddress) {
        userInfoDao.incCntBadcredentialsByMailAddress(mailAddress);
    }

    /**
     * ???
     *
     * @param mailAddress ???
     */
    public void initCntBadcredentials(String mailAddress) {
        userInfoDao.initCntBadcredentialsByMailAddress(mailAddress);
    }

}
