package ksbysample.webapp.lending.web.booklist;

import ksbysample.webapp.lending.dao.LendingAppDao;
import ksbysample.webapp.lending.dao.LendingBookDao;
import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.security.LendingUserDetailsHelper;
import ksbysample.webapp.lending.service.file.BooklistCsvFileService;
import ksbysample.webapp.lending.service.file.BooklistCsvRecord;
import ksbysample.webapp.lending.service.queue.InquiringStatusOfBookQueueService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static ksbysample.webapp.lending.values.lendingapp.LendingAppStatusValues.TENPORARY_SAVE;

@Service
public class BooklistService {

    private final BooklistCsvFileService booklistCsvFileService;

    private final LendingAppDao lendingAppDao;

    private final LendingBookDao lendingBookDao;

    private final InquiringStatusOfBookQueueService inquiringStatusOfBookQueueService;

    private final LendingUserDetailsHelper lendingUserDetailsHelper;

    /**
     * @param booklistCsvFileService            ???
     * @param lendingAppDao                     ???
     * @param lendingBookDao                    ???
     * @param inquiringStatusOfBookQueueService ???
     * @param lendingUserDetailsHelper          ???
     */
    public BooklistService(BooklistCsvFileService booklistCsvFileService
            , LendingAppDao lendingAppDao
            , LendingBookDao lendingBookDao
            , InquiringStatusOfBookQueueService inquiringStatusOfBookQueueService
            , LendingUserDetailsHelper lendingUserDetailsHelper) {
        this.booklistCsvFileService = booklistCsvFileService;
        this.lendingAppDao = lendingAppDao;
        this.lendingBookDao = lendingBookDao;
        this.inquiringStatusOfBookQueueService = inquiringStatusOfBookQueueService;
        this.lendingUserDetailsHelper = lendingUserDetailsHelper;
    }

    /**
     * @param uploadBooklistForm ???
     * @return ???
     */
    public Long temporarySaveBookListCsvFile(UploadBooklistForm uploadBooklistForm) {
        // アップロードされたCSVファイルのデータを List に変換する
        List<BooklistCsvRecord> booklistCsvRecordList
                = booklistCsvFileService.convertFileToList(uploadBooklistForm.getFileupload());

        // lending_app テーブルにデータを保存する
        LendingApp lendingApp = new LendingApp();
        lendingApp.setStatus(TENPORARY_SAVE.getValue());
        lendingApp.setLendingUserId(lendingUserDetailsHelper.getLoginUserId());
        lendingAppDao.insert(lendingApp);

        // lending_book テーブルにデータを保存する
        LendingBook lendingBook;
        for (BooklistCsvRecord booklistCsvRecord : booklistCsvRecordList) {
            lendingBook = new LendingBook();
            BeanUtils.copyProperties(booklistCsvRecord, lendingBook);
            lendingBook.setLendingAppId(lendingApp.getLendingAppId());
            lendingBookDao.insert(lendingBook);
        }

        return lendingApp.getLendingAppId();
    }

    /**
     * @param lendingAppId ???
     * @return ???
     */
    public List<LendingBook> getLendingBookList(Long lendingAppId) {
        List<LendingBook> lendingBookList = lendingBookDao.selectByLendingAppId(lendingAppId);
        return lendingBookList;
    }

    /**
     * @param registerBooklistForm ???
     */
    public void sendMessageToInquiringStatusOfBookQueue(RegisterBooklistForm registerBooklistForm) {
        inquiringStatusOfBookQueueService.sendMessage(registerBooklistForm.getLendingAppId());
    }

}
