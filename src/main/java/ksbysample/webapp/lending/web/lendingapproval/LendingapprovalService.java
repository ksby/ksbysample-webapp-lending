package ksbysample.webapp.lending.web.lendingapproval;

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
import static ksbysample.webapp.lending.values.LendingAppStatusValues.PENDING;
import static ksbysample.webapp.lending.values.LendingBookLendingAppFlgValues.APPLY;

@Service
public class LendingapprovalService {

    @Autowired
    private LendingAppDao lendingAppDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private LendingBookDao lendingBookDao;

    public void setDispData(Long lendingAppId, LendingapprovalForm lendingapprovalForm) {
        LendingApp lendingApp = lendingAppDao.selectByIdAndStatus(lendingAppId
                , Arrays.asList(PENDING.getValue(), APPLOVED.getValue()));
        String username = "";
        if (lendingApp != null) {
            UserInfo userInfo = userInfoDao.selectById(lendingApp.getLendingUserId());
            username = userInfo.getUsername();
        }
        List<LendingBook> lendingBookList
                = lendingBookDao.selectByLendingAppIdAndLendingAppFlg(lendingAppId, APPLY.getValue());

        lendingapprovalForm.setLendingApp(lendingApp);
        lendingapprovalForm.setUsername(username);
        lendingapprovalForm.setApplyingBookFormList(lendingBookList);
    }

}
