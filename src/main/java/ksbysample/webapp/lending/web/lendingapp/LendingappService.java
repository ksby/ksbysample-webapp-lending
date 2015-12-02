package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.webapp.lending.dao.LendingAppDao;
import ksbysample.webapp.lending.dao.LendingBookDao;
import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static ksbysample.webapp.lending.values.LendingAppStatusValues.PENDING;
import static ksbysample.webapp.lending.values.LendingBookLendingAppFlgValues.APPLY;

@Service
public class LendingappService {

    @Autowired
    private LendingAppDao lendingAppDao;

    @Autowired
    private LendingBookDao lendingBookDao;

    @Autowired
    private MessagesPropertiesHelper messagesPropertiesHelper;

    public LendingApp getLendingApp(Long lendingAppId) {
        LendingApp lendingApp = lendingAppDao.selectById(lendingAppId);
        if (lendingApp == null) {
            throw new WebApplicationRuntimeException(
                    messagesPropertiesHelper.getMessage("LendingappForm.lendingApp.nodataerr", null));
        }

        return lendingApp;
    }
    
    public List<LendingBook> getLendingBookList(Long lendingAppId) {
        List<LendingBook> lendingBookList = lendingBookDao.selectByLendingAppId(lendingAppId);
        return lendingBookList;
    }

    public void apply(LendingappForm lendingappForm) {
        // 更新対象のデータを取得する(ロックする)
        LendingApp lendingApp = lendingAppDao.selectById(lendingappForm.getLendingApp().getLendingAppId()
                , SelectOptions.get().forUpdate());
        List<LendingBook> lendingBookList = lendingBookDao.selectByLendingAppId(lendingappForm.getLendingApp().getLendingAppId()
                , SelectOptions.get().forUpdate());

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
    }

    public void temporarySave(LendingappForm lendingappForm) {
        // 更新対象のデータを取得する(ロックする)
        LendingApp lendingApp = lendingAppDao.selectById(lendingappForm.getLendingApp().getLendingAppId()
                , SelectOptions.get().forUpdate());
        List<LendingBook> lendingBookList = lendingBookDao.selectByLendingAppId(lendingappForm.getLendingApp().getLendingAppId()
                , SelectOptions.get().forUpdate());
    
        // lending_book.lending_app_flg, lending_app_reason に画面に入力された内容をセットする
        lendingappForm.getLendingBookDtoList().stream()
                .forEach(lendingBookDto -> {
                    LendingBook lendingBook = new LendingBook();
                    BeanUtils.copyProperties(lendingBookDto, lendingBook);
                    lendingBookDao.updateLendingAppFlgAndReason(lendingBook);
                });
    }
    
}
