package ksbysample.webapp.lending.web.confirmresult;

import ksbysample.webapp.lending.dao.LendingAppDao;
import ksbysample.webapp.lending.dao.LendingBookDao;
import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.entity.UserInfo;
import ksbysample.webapp.lending.helper.download.booklistcsv.BookListCsvData;
import ksbysample.webapp.lending.helper.download.booklistcsv.BookListCsvDataConverter;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static ksbysample.webapp.lending.values.lendingapp.LendingAppStatusValues.APPLOVED;
import static ksbysample.webapp.lending.values.lendingbook.LendingBookLendingAppFlgValues.APPLY;

/**
 * ???
 */
@Service
public class ConfirmresultService {

    private final LendingAppDao lendingAppDao;

    private final UserInfoDao userInfoDao;

    private final LendingBookDao lendingBookDao;

    private final BookListCsvDataConverter bookListCsvDataConverter;

    /**
     * ???
     *
     * @param lendingAppDao            ???
     * @param userInfoDao              ???
     * @param lendingBookDao           ???
     * @param bookListCsvDataConverter ???
     */
    public ConfirmresultService(LendingAppDao lendingAppDao
            , UserInfoDao userInfoDao
            , LendingBookDao lendingBookDao
            , BookListCsvDataConverter bookListCsvDataConverter) {
        this.lendingAppDao = lendingAppDao;
        this.userInfoDao = userInfoDao;
        this.lendingBookDao = lendingBookDao;
        this.bookListCsvDataConverter = bookListCsvDataConverter;
    }

    /**
     * ???
     *
     * @param lendingAppId      ???
     * @param confirmresultForm ???
     */
    public void setDispData(Long lendingAppId, ConfirmresultForm confirmresultForm) {
        LendingApp lendingApp = lendingAppDao.selectByIdAndStatus(lendingAppId, Arrays.asList(APPLOVED.getValue()));
        UserInfo lendingUserInfo = null;
        UserInfo approvalUserInfo = null;
        if (lendingApp != null) {
            lendingUserInfo = userInfoDao.selectById(lendingApp.getLendingUserId());
            approvalUserInfo = userInfoDao.selectById(lendingApp.getApprovalUserId());
        }
        List<LendingBook> lendingBookList
                = lendingBookDao.selectByLendingAppIdAndLendingAppFlg(lendingAppId, APPLY.getValue());

        confirmresultForm.setLendingApp(lendingApp);
        if (lendingUserInfo != null) {
            confirmresultForm.setLendingUserId(lendingUserInfo.getUserId());
            confirmresultForm.setLendingUserName(lendingUserInfo.getUsername());
        }
        if (approvalUserInfo != null) {
            confirmresultForm.setApprovalUserName(approvalUserInfo.getUsername());
        }
        confirmresultForm.setApprovedBookFormListFromLendingBookList(lendingBookList);
    }

    /**
     * ???
     *
     * @param lendingAppId ???
     * @return ???
     */
    public List<BookListCsvData> getDownloadData(Long lendingAppId) {
        List<LendingBook> lendingBookList
                = lendingBookDao.selectByLendingAppIdAndLendingAppFlg(lendingAppId, APPLY.getValue());
        return bookListCsvDataConverter.convertFrom(lendingBookList);
    }

}
