package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.webapp.lending.dao.LendingAppDao;
import ksbysample.webapp.lending.dao.LendingBookDao;
import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.mail.EmailHelper;
import ksbysample.webapp.lending.helper.mail.Mail002Helper;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import ksbysample.webapp.lending.helper.user.UserHelper;
import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

import static ksbysample.webapp.lending.values.lendingapp.LendingAppStatusValues.PENDING;
import static ksbysample.webapp.lending.values.lendingbook.LendingBookLendingAppFlgValues.APPLY;

/**
 * ???
 */
@Service
public class LendingappService {

    private final LendingAppDao lendingAppDao;

    private final LendingBookDao lendingBookDao;

    private final MessagesPropertiesHelper mph;

    private final EmailHelper emailHelper;

    private final Mail002Helper mail002Helper;

    private final UserHelper userHelper;

    /**
     * @param lendingAppDao  ???
     * @param lendingBookDao ???
     * @param mph            ???
     * @param emailHelper    ???
     * @param mail002Helper  ???
     * @param userHelper     ???
     */
    public LendingappService(LendingAppDao lendingAppDao
            , LendingBookDao lendingBookDao
            , MessagesPropertiesHelper mph
            , EmailHelper emailHelper
            , Mail002Helper mail002Helper
            , UserHelper userHelper) {
        this.lendingAppDao = lendingAppDao;
        this.lendingBookDao = lendingBookDao;
        this.mph = mph;
        this.emailHelper = emailHelper;
        this.mail002Helper = mail002Helper;
        this.userHelper = userHelper;
    }

    /**
     * @param lendingAppId   ???
     * @param lendingappForm ???
     */
    public void setDispData(Long lendingAppId, LendingappForm lendingappForm) {
        LendingApp lendingApp = lendingAppDao.selectById(lendingAppId);
        if (lendingApp == null) {
            throw new WebApplicationRuntimeException(
                    mph.getMessage("LendingappForm.lendingApp.nodataerr", null));
        }
        List<LendingBook> lendingBookList = lendingBookDao.selectByLendingAppId(lendingAppId);

        lendingappForm.setLendingApp(lendingApp);
        lendingappForm.setLendingBookList(lendingBookList);
    }

    /**
     * @param lendingappForm ???
     * @throws MessagingException
     */
    public void apply(LendingappForm lendingappForm) throws MessagingException {
        // 更新対象のデータを取得する(ロックする)
        LendingApp lendingApp = lendingAppDao.selectById(lendingappForm.getLendingApp().getLendingAppId()
                , SelectOptions.get().forUpdate());
        List<LendingBook> lendingBookList = lendingBookDao.selectByLendingAppId(
                lendingappForm.getLendingApp().getLendingAppId(), SelectOptions.get().forUpdate());

        // lending_app.status を 3(申請中) にする
        lendingApp.setStatus(PENDING.getValue());
        lendingAppDao.update(lendingApp);

        // lending_book.lending_app_flg を 1(する) に、lending_app_reason に画面に入力された申請理由をセットする
        lendingappForm.getLendingBookDtoList().stream()
                .filter(lendingBookDto ->
                        StringUtils.equals(lendingBookDto.getLendingAppFlg(), APPLY.getValue()))
                .forEach(lendingBookDto -> {
                    LendingBook lendingBook = new LendingBook();
                    BeanUtils.copyProperties(lendingBookDto, lendingBook);
                    lendingBookDao.updateLendingAppFlgAndReason(lendingBook);
                });

        // 承認者にメールを送信する
        String[] approverMailAddrList = userHelper.getApprovalMailAddrList();
        MimeMessage mimeMessage = mail002Helper.createMessage(approverMailAddrList, lendingApp.getLendingAppId());
        emailHelper.sendMail(mimeMessage);
    }

    /**
     * @param lendingappForm ???
     */
    public void temporarySave(LendingappForm lendingappForm) {
        // 更新対象のデータを取得する(ロックする)
        LendingApp lendingApp = lendingAppDao.selectById(lendingappForm.getLendingApp().getLendingAppId()
                , SelectOptions.get().forUpdate());
        List<LendingBook> lendingBookList = lendingBookDao.selectByLendingAppId(
                lendingappForm.getLendingApp().getLendingAppId(), SelectOptions.get().forUpdate());

        // lending_book.lending_app_flg, lending_app_reason に画面に入力された内容をセットする
        lendingappForm.getLendingBookDtoList().stream()
                .forEach(lendingBookDto -> {
                    LendingBook lendingBook = new LendingBook();
                    BeanUtils.copyProperties(lendingBookDto, lendingBook);
                    lendingBookDao.updateLendingAppFlgAndReason(lendingBook);
                });
    }

}
