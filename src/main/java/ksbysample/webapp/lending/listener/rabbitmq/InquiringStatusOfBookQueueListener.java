package ksbysample.webapp.lending.listener.rabbitmq;

import ksbysample.webapp.lending.config.Constant;
import ksbysample.webapp.lending.dao.LendingAppDao;
import ksbysample.webapp.lending.dao.LendingBookDao;
import ksbysample.webapp.lending.dao.LibraryForsearchDao;
import ksbysample.webapp.lending.dao.UserInfoDao;
import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.entity.LibraryForsearch;
import ksbysample.webapp.lending.entity.UserInfo;
import ksbysample.webapp.lending.helper.mail.Mail001Helper;
import ksbysample.webapp.lending.helper.mail.EmailHelper;
import ksbysample.webapp.lending.service.calilapi.CalilApiService;
import ksbysample.webapp.lending.service.calilapi.response.Book;
import ksbysample.webapp.lending.service.queue.InquiringStatusOfBookQueueMessage;
import ksbysample.webapp.lending.service.queue.InquiringStatusOfBookQueueService;
import ksbysample.webapp.lending.values.LendingAppStatusValues;
import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.jdbc.SelectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InquiringStatusOfBookQueueListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private InquiringStatusOfBookQueueService inquiringStatusOfBookQueueService;

    @Autowired
    private CalilApiService calilApiService;

    @Autowired
    private EmailHelper emailHelper;

    @Autowired
    private Mail001Helper mail001Helper;
    
    @Autowired
    private LibraryForsearchDao libraryForsearchDao;

    @Autowired
    private LendingAppDao lendingAppDao;

    @Autowired
    private LendingBookDao lendingBookDao;
    
    @Autowired
    private UserInfoDao userInfoDao;
    
    @RabbitListener(queues = {Constant.QUEUE_NAME_INQUIRING_STATUSOFBOOK})
    public void receiveMessage(Message message) throws MessagingException {
        // 受信したメッセージを InquiringStatusOfBookQueueMessage クラスのインスタンスに変換する
        InquiringStatusOfBookQueueMessage convertedMessage
                = inquiringStatusOfBookQueueService.convertMessageToObject(message);

        // 選択中の図書館を取得する
        LibraryForsearch libraryForsearch = libraryForsearchDao.selectSelectedLibrary();

        // 更新対象の lending_app テーブルのデータを取得する
        LendingApp lendingApp = lendingAppDao.selectById(convertedMessage.getLendingAppId(), SelectOptions.get().forUpdate());
        if (lendingApp == null) {
            logger.error("lending_app テーブルに対象のデータがありませんでした ( lending_app_id = {} )。", convertedMessage.getLendingAppId());
            return;
        }
        
        // lending_book テーブルから調査対象の ISBN 一覧を取得する
        List<LendingBook> lendingBookList
                = lendingBookDao.selectByLendingAppId(convertedMessage.getLendingAppId(), SelectOptions.get().forUpdate());
        if (lendingBookList == null) {
            logger.error("lending_book テーブルに対象のデータがありませんでした ( lending_app_id = {} )。", convertedMessage.getLendingAppId());
            return;
        }
        List<String> isbnList = lendingBookList.stream()
                .map(LendingBook::getIsbn)
                .collect(Collectors.toList());
        
        // カーリルの蔵書検索 WebAPI を呼び出して貸出状況を取得する
        List<Book> bookList = calilApiService.check(libraryForsearch.getSystemid(), isbnList);

        // lending_book テーブルに取得した貸出状況を反映し、lending_app テーブルの status を 2(未申請) に更新する
        copyLendingStateFromBookListToEntityList(bookList, lendingBookList);
        updateLendingData(lendingBookList, lendingApp);

        // データを登録したユーザへメールを送信する
        UserInfo userInfo = userInfoDao.selectById(lendingApp.getLendingUserId());
        MimeMessage mimeMessage = mail001Helper.createMessage(userInfo.getMailAddress(), convertedMessage.getLendingAppId());
        emailHelper.sendMail(mimeMessage);
    }

    private void copyLendingStateFromBookListToEntityList(List<Book> bookList, List<LendingBook> lendingBookList) {
        for (LendingBook lendingBook : lendingBookList) {
            for (Book book : bookList) {
                if (StringUtils.equals(lendingBook.getIsbn(), book.getIsbn())) {
                    lendingBook.setLendingState(book.getFirstLibkeyValue());
                    break;
                }
            }
        }
    }

    private void updateLendingData(List<LendingBook> lendingBookList, LendingApp lendingApp) {
        // lending_book テーブルに取得した貸出状況を反映する
        for (LendingBook lendingBook : lendingBookList) {
            lendingBookDao.updateLendingState(lendingBook);
        }

        // lending_app テーブルの status を 2(未申請) に更新する
        lendingApp.setStatus(LendingAppStatusValues.UNAPPLIED.getValue());
        lendingAppDao.update(lendingApp);
    }
    
}
