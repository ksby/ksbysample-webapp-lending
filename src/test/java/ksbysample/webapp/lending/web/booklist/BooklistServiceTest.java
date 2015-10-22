package ksbysample.webapp.lending.web.booklist;

import ksbysample.common.test.TableDataAssert;
import ksbysample.common.test.TestDataResource;
import ksbysample.webapp.lending.Application;
import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.security.LendingUserDetailsHelper;
import ksbysample.webapp.lending.service.file.BooklistCsvFileServiceTest;
import mockit.NonStrictExpectations;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class BooklistServiceTest {

    private final String MAILADDR_TANAKA_TARO = "tanaka.taro@sample.com";

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private BooklistService booklistService;
    
    @Test
    public void testTemporarySaveBookListCsvFile() throws Exception {
        new NonStrictExpectations(LendingUserDetailsHelper.class) {{
            LendingUserDetailsHelper.getLoginUserId(); result = new Long(1);
        }};

        UploadBooklistForm uploadBooklistForm = new UploadBooklistForm();
        // テスト用のユーティリティクラスを作るべきですが、今回は他のテストクラスのメソッドをそのまま使います
        BooklistCsvFileServiceTest booklistCsvFileServiceTest = new BooklistCsvFileServiceTest();
        uploadBooklistForm.setFileupload(booklistCsvFileServiceTest.createNoErrorCsvFile());
        
        Long lendingAppId = booklistService.temporarySaveBookListCsvFile(uploadBooklistForm);
        assertThat(lendingAppId).isNotEqualTo(new Long(0));
        IDataSet dataSet = new CsvDataSet(new File("src/test/resources/ksbysample/webapp/lending/web/booklist/assertdata/001"));
        TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
        tableDataAssert.assertEquals("lending_app", new String[]{"lending_app_id", "approval_user_id"});
        tableDataAssert.assertEquals("lending_book", new String[]{"lending_book_id", "lending_app_id", "lending_state", "lending_app_flg", "lending_app_reason", "approval_result", "approval_reason"});

        List<LendingBook> lendingBookList = booklistService.getLendingBookList(lendingAppId);
        assertThat(lendingBookList).hasSize(5);
    }
    
    @Test
    public void testSendMessageToInquiringStatusOfBookQueue() throws Exception {
        // 現在の実装では InquiringStatusOfBookQueueServiceTest が通ればOKなので、こちらは実装しない
    }

}