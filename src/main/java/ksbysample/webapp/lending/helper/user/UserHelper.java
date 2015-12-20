package ksbysample.webapp.lending.helper.user;

import com.google.common.collect.Iterables;
import ksbysample.webapp.lending.dao.UserInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserHelper {

    @Autowired
    private UserInfoDao userInfoDao;
    
    public String[] getApprovalMailAddrList() {
        List<String> approvalMailAddrList = userInfoDao.selectApproverMailAddrList();
        return Iterables.toArray(approvalMailAddrList, String.class);
    }

}
