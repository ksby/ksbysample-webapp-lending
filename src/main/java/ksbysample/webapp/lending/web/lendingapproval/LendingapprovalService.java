package ksbysample.webapp.lending.web.lendingapproval;

import ksbysample.webapp.lending.dao.LendingAppDao;
import ksbysample.webapp.lending.dao.LendingBookDao;
import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.entity.UserInfo;
import ksbysample.webapp.lending.helper.mail.EmailHelper;
import ksbysample.webapp.lending.helper.mail.Mail003Helper;
import ksbysample.webapp.lending.security.LendingUserDetailsHelper;
import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ksbysample.webapp.lending.values.lendingapp.LendingAppStatusValues.APPLOVED;
import static ksbysample.webapp.lending.values.lendingapp.LendingAppStatusValues.PENDING;
import static ksbysample.webapp.lending.values.lendingbook.LendingBookLendingAppFlgValues.APPLY;

/**
 * ???
 */
@Service
public class LendingapprovalService {

    private final LendingAppDao lendingAppDao;

    private final UserInfoDao userInfoDao;

    private final LendingBookDao lendingBookDao;

    private final Mail003Helper mail003Helper;

    private final EmailHelper emailHelper;

    private final LendingUserDetailsHelper lendingUserDetailsHelper;

    /**
     * @param lendingAppDao            ???
     * @param userInfoDao              ???
     * @param lendingBookDao           ???
     * @param mail003Helper            ???
     * @param emailHelper              ???
     * @param lendingUserDetailsHelper ???
     */
    public LendingapprovalService(LendingAppDao lendingAppDao
            , UserInfoDao userInfoDao
            , LendingBookDao lendingBookDao
            , Mail003Helper mail003Helper
            , EmailHelper emailHelper
            , LendingUserDetailsHelper lendingUserDetailsHelper) {
        this.lendingAppDao = lendingAppDao;
        this.userInfoDao = userInfoDao;
        this.lendingBookDao = lendingBookDao;
        this.mail003Helper = mail003Helper;
        this.emailHelper = emailHelper;
        this.lendingUserDetailsHelper = lendingUserDetailsHelper;
    }

    /**
     * @param lendingAppId        ???
     * @param lendingapprovalForm ???
     */
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
        lendingapprovalForm.setApplyingBookFormListFromLendingBookList(lendingBookList);
    }

    /**
     * @param lendingapprovalForm ???
     * @throws MessagingException
     */
    @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops", "UnusedVariable"})
    public void complete(LendingapprovalForm lendingapprovalForm) throws MessagingException {
        // 更新対象のデータを取得する(ロックする)
        Long lendingAppId = lendingapprovalForm.getLendingApp().getLendingAppId();
        LendingApp lendingApp = lendingAppDao.selectById(lendingAppId, SelectOptions.get().forUpdate());
        lendingBookDao.selectByLendingAppId(lendingAppId, SelectOptions.get().forUpdate());

        // lending_app.status を 4(承認済) にする
        lendingApp.setStatus(APPLOVED.getValue());
        lendingApp.setApprovalUserId(lendingUserDetailsHelper.getLoginUserId());
        lendingAppDao.update(lendingApp);

        // lending_book の approval_result, approval_reason を更新する
        List<LendingBook> lendingBookList = new ArrayList<>();
        for (ApplyingBookForm applyingBookForm : lendingapprovalForm.getApplyingBookFormList()) {
            LendingBook lendingBook = new LendingBook();
            BeanUtils.copyProperties(applyingBookForm, lendingBook);
            lendingBookDao.updateApprovalResultAndReason(lendingBook);
            lendingBookList.add(lendingBook);
        }

        // 申請者にメールを送信する
        UserInfo userInfo = userInfoDao.selectById(lendingApp.getLendingUserId());
        MimeMessage mimeMessage = mail003Helper.createMessage(userInfo.getMailAddress(), lendingAppId, lendingBookList);
        emailHelper.sendMail(mimeMessage);
    }

}
