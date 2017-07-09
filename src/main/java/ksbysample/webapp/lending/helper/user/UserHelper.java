package ksbysample.webapp.lending.helper.user;

import com.google.common.collect.Iterables;
import ksbysample.webapp.lending.dao.UserInfoDao;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ???
 */
@Component
public class UserHelper {

    private final UserInfoDao userInfoDao;

    /**
     * @param userInfoDao ???
     */
    public UserHelper(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    /**
     * @return ???
     */
    public String[] getApprovalMailAddrList() {
        List<String> approvalMailAddrList = userInfoDao.selectApproverMailAddrList();
        return Iterables.toArray(approvalMailAddrList, String.class);
    }

}
