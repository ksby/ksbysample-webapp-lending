package ksbysample.webapp.lending.web.booklist;

import ksbysample.common.test.extension.db.TableDataAssert;
import ksbysample.common.test.extension.db.TestDataExtension;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.security.LendingUserDetailsHelper;
import ksbysample.webapp.lending.service.file.BooklistCsvFileServiceTest;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class BooklistServiceTest {

    private static final String MAILADDR_TANAKA_TARO = "tanaka.taro@sample.com";

    @RegisterExtension
    @Autowired
    public TestDataExtension testDataExtension;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BooklistService booklistService;

    @MockBean
    private LendingUserDetailsHelper lendingUserDetailsHelper;

    @Test
    void testTemporarySaveBookListCsvFile() throws Exception {
        given(lendingUserDetailsHelper.getLoginUserId()).willReturn(1L);

        UploadBooklistForm uploadBooklistForm = new UploadBooklistForm();
        // テスト用のユーティリティクラスを作るべきですが、今回は他のテストクラスのメソッドをそのまま使います
        BooklistCsvFileServiceTest booklistCsvFileServiceTest = new BooklistCsvFileServiceTest();
        uploadBooklistForm.setFileupload(booklistCsvFileServiceTest.createNoErrorCsvFile());

        Long lendingAppId = booklistService.temporarySaveBookListCsvFile(uploadBooklistForm);
        assertThat(lendingAppId).isNotEqualTo(Long.valueOf(0L));
        IDataSet dataSet = new CsvDataSet(
                new File("src/test/resources/ksbysample/webapp/lending/web/booklist/assertdata/001"));
        TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
        tableDataAssert.assertEquals("lending_app"
                , new String[]{"lending_app_id", "approval_user_id", "version"});
        tableDataAssert.assertEquals("lending_book"
                , new String[]{"lending_book_id", "lending_app_id", "lending_state", "lending_app_flg"
                        , "lending_app_reason", "approval_result", "approval_reason", "version"});

        List<LendingBook> lendingBookList = booklistService.getLendingBookList(lendingAppId);
        assertThat(lendingBookList).hasSize(5);
    }

    @Test
    void testSendMessageToInquiringStatusOfBookQueue() {
        // 現在の実装では InquiringStatusOfBookQueueServiceTest が通ればOKなので、こちらは実装しない
    }

}
