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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static ksbysample.webapp.lending.values.lendingapp.LendingAppStatusValues.TENPORARY_SAVE;

@Service
public class BooklistService {

    @Autowired
    private BooklistCsvFileService booklistCsvFileService;

    @Autowired
    private LendingAppDao lendingAppDao;

    @Autowired
    private LendingBookDao lendingBookDao;

    @Autowired
    private InquiringStatusOfBookQueueService inquiringStatusOfBookQueueService;

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
        lendingApp.setLendingUserId(LendingUserDetailsHelper.getLoginUserId());
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

    public List<LendingBook> getLendingBookList(Long lendingAppId) {
        List<LendingBook> lendingBookList = lendingBookDao.selectByLendingAppId(lendingAppId);
        return lendingBookList;
    }

    public void sendMessageToInquiringStatusOfBookQueue(RegisterBooklistForm registerBooklistForm) {
        inquiringStatusOfBookQueueService.sendMessage(registerBooklistForm.getLendingAppId());
    }

}
