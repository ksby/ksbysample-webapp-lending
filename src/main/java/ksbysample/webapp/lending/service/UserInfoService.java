package ksbysample.webapp.lending.service;

import ksbysample.webapp.lending.dao.UserInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;
    
    public void incCntBadcredentials(String mailAddress) {
        userInfoDao.incCntBadcredentialsByMailAddress(mailAddress);
    }

    public void initCntBadcredentials(String mailAddress) {
        userInfoDao.initCntBadcredentialsByMailAddress(mailAddress);
    }
    
}
