package ksbysample.webapp.lending.web.confirmresult;

import ksbysample.webapp.lending.dao.LendingAppDao;
import ksbysample.webapp.lending.dao.LendingBookDao;
import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static ksbysample.webapp.lending.values.LendingAppStatusValues.APPLOVED;
import static ksbysample.webapp.lending.values.LendingBookLendingAppFlgValues.APPLY;

@Service
public class ConfirmresultService {

    @Autowired
    private LendingAppDao lendingAppDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private LendingBookDao lendingBookDao;

    public void setDispData(Long lendingAppId, ConfirmresultForm confirmresultForm) {
        LendingApp lendingApp = lendingAppDao.selectByIdAndStatus(lendingAppId, Arrays.asList(APPLOVED.getValue()));
        String lendingUserName = "";
        String approvalUserName = "";
        if (lendingApp != null) {
            UserInfo lendingUserInfo = userInfoDao.selectById(lendingApp.getLendingUserId());
            lendingUserName = lendingUserInfo.getUsername();
            UserInfo approvalUserInfo = userInfoDao.selectById(lendingApp.getApprovalUserId());
            approvalUserName = approvalUserInfo.getUsername();
        }
        List<LendingBook> lendingBookList
                = lendingBookDao.selectByLendingAppIdAndLendingAppFlg(lendingAppId, APPLY.getValue());

        confirmresultForm.setLendingApp(lendingApp);
        confirmresultForm.setLendingUserName(lendingUserName);
        confirmresultForm.setApprovalUserName(approvalUserName);
        confirmresultForm.setApprovedBookFormListFromLendingBookList(lendingBookList);
    }

}
