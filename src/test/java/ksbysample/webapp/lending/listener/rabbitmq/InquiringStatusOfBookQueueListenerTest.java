package ksbysample.webapp.lending.listener.rabbitmq;

import com.google.common.base.Charsets;
import ksbysample.common.test.rule.db.TableDataAssert;
import ksbysample.common.test.rule.db.TestData;
import ksbysample.common.test.rule.db.TestDataResource;
import ksbysample.common.test.rule.mail.MailServerResource;
import ksbysample.webapp.lending.dao.LibraryForsearchDao;
import ksbysample.webapp.lending.entity.LibraryForsearch;
import ksbysample.webapp.lending.service.calilapi.CalilApiService;
import ksbysample.webapp.lending.service.calilapi.response.Book;
import ksbysample.webapp.lending.service.calilapi.response.Libkey;
import ksbysample.webapp.lending.service.calilapi.response.SystemData;
import ksbysample.webapp.lending.service.queue.InquiringStatusOfBookQueueMessage;
import mockit.Mock;
import mockit.MockUp;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static mockit.Deencapsulation.getField;
import static mockit.Deencapsulation.setField;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InquiringStatusOfBookQueueListenerTest {

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Rule
    @Autowired
    public MailServerResource mailServer;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private InquiringStatusOfBookQueueListener listener;

    @Test
    @TestData("listener/rabbitmq/testdata/001")
    public void testReceiveMessage() throws Exception {
        // モックに入れ替える前のフィールドの実体を退避する
        LibraryForsearchDao libraryForsearchDaoOrg = getField(listener, "libraryForsearchDao");
        CalilApiService calilApiServiceOrg = getField(listener, "calilApiService");
        try {
            /**
             * モック定義部
             */
            // InquiringStatusOfBookQueueListener.libraryForsearchDao をモックに入れ替える
            LibraryForsearchDao libraryForsearchDao = new MockUp<LibraryForsearchDao>() {
                @Mock
                LibraryForsearch selectSelectedLibrary() {
                    LibraryForsearch libraryForsearch = new LibraryForsearch();
                    libraryForsearch.setSystemid("System_Id");
                    libraryForsearch.setFormal("図書館名");
                    return libraryForsearch;
                }
            }.getMockInstance();
            setField(listener, "libraryForsearchDao", libraryForsearchDao);

            // InquiringStatusOfBookQueueListener.calilApiService をモックに入れ替える
            CalilApiService calilApiService = new MockUp<CalilApiService>() {
                @Mock
                public List<Book> check(String systemid, List<String> isbnList) {
                    List<Book> bookList = new ArrayList<>();
                    bookList.add(new Book("978-4-7741-6366-6", null, new SystemData(null, null, null, Arrays.asList(new Libkey(null, "貸出可")))));
                    bookList.add(new Book("978-4-7741-5377-3", null, new SystemData(null, null, null, Arrays.asList(new Libkey(null, "蔵書あり")))));
                    bookList.add(new Book("978-4-7973-8014-9", null, new SystemData(null, null, null, Arrays.asList(new Libkey(null, "貸出中")))));
                    bookList.add(new Book("978-4-7973-4778-4", null, new SystemData(null, null, null, Arrays.asList(new Libkey(null, "準備中")))));
                    bookList.add(new Book("978-4-87311-704-1", null, new SystemData(null, null, null, Arrays.asList(new Libkey(null, "蔵書なし")))));
                    return bookList;
                }
            }.getMockInstance();
            setField(listener, "calilApiService", calilApiService);

            /**
             * テスト本体
             */
            InquiringStatusOfBookQueueMessage queueMessage = new InquiringStatusOfBookQueueMessage();
            queueMessage.setLendingAppId(1L);
            Message message = messageConverter.toMessage(queueMessage, new MessageProperties());
            listener.receiveMessage(message);

            /**
             * 検証
             */
            // テーブルの以下のカラムのデータを検証する
            //  ・lending_app.status
            //  ・lending_book.lending_state
            IDataSet dataSet = new CsvDataSet(new File("src/test/resources/ksbysample/webapp/lending/listener/rabbitmq/assertdata/001"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEquals("lending_app", new String[]{"lending_app_id", "lending_user_id", "approval_user_id", "version"});
            tableDataAssert.assertEquals("lending_book", new String[]{"lending_book_id", "lending_app_id", "isbn", "book_name", "lending_app_flg", "lending_app_reason", "approval_result", "approval_reason", "version"});

            // 送信されたメールを検証する
            assertThat(mailServer.getMessagesCount()).isEqualTo(1);
            MimeMessage mimeMessage = mailServer.getFirstMessage();
            assertThat(mimeMessage.getRecipients(javax.mail.Message.RecipientType.TO))
                    .extracting(Object::toString)
                    .containsOnly("tanaka.taro@sample.com");
            assertThat(mimeMessage.getContent())
                    .isEqualTo(com.google.common.io.Files.toString(
                            new File("src/test/resources/ksbysample/webapp/lending/helper/mail/assertdata/001/message.txt")
                            , Charsets.UTF_8));
        } finally {
            // モックに差し替えたフィールドを退避しておいた元の実体に戻す
            setField(listener, "libraryForsearchDao", libraryForsearchDaoOrg);
            setField(listener, "calilApiService", calilApiServiceOrg);
        }
    }

}
