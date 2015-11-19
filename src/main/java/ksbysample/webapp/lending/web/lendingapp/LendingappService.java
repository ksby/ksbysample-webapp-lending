package ksbysample.webapp.lending.web.lendingapp;

import ksbysample.webapp.lending.dao.LendingAppDao;
import ksbysample.webapp.lending.dao.LendingBookDao;
import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.exception.WebApplicationRuntimeException;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
