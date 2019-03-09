package ksbysample.webapp.lending.web.lendingapproval;

import com.google.common.io.Files;
import ksbysample.common.test.extension.db.AssertOptions;
import ksbysample.common.test.extension.db.TableDataAssert;
import ksbysample.common.test.extension.db.TestData;
import ksbysample.common.test.extension.db.TestDataExtension;
import ksbysample.common.test.extension.mail.MailServerExtension;
import ksbysample.common.test.extension.mockmvc.SecurityMockMvcExtension;
import ksbysample.common.test.helper.TestHelper;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.FileCopyUtils;
import org.yaml.snakeyaml.Yaml;

import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.File;
import java.nio.charset.StandardCharsets;

import static ksbysample.common.test.matcher.ErrorsResultMatchers.errors;
import static ksbysample.common.test.matcher.HtmlResultMatchers.html;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LendingapprovalControllerTest {

    @Nested
    @SpringBootTest
    class 貸出承認画面の初期表示のテスト_エラー処理_DBなし {

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Autowired
        private MessagesPropertiesHelper mph;

        @Test
        void ログインしていなければ貸出承認画面は表示できない() throws Exception {
            mvc.noauth.perform(get("/lendingapproval?lendingAppId=105"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"));
        }

        @Test
        void 承認権限を持たないユーザは貸出承認画面を表示できない() throws Exception {
            mvc.authItoAoi.perform(get("/lendingapproval?lendingAppId=105"))
                    .andExpect(status().isForbidden());
        }

        @Test
        void lendingAppIdパラメータがなければエラーになる() throws Exception {
            MvcResult result = mvc.authSuzukiHanako.perform(get("/lendingapproval"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content).contains(mph.getMessage("LendingapprovalParamForm.lendingAppId.emptyerr", null));
        }

        @Test
        void lendingAppIdパラメータで指定された値が数値でなければエラーになる() throws Exception {
            MvcResult result = mvc.authSuzukiHanako.perform(get("/lendingapproval?lendingAppId=xxx"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content).contains(mph.getMessage("LendingapprovalParamForm.lendingAppId.emptyerr", null));
        }

    }

    @Nested
    @SpringBootTest
    class 貸出承認画面の初期表示のテスト_エラー処理_DBあり {

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Autowired
        private MessagesPropertiesHelper mph;

        @Test
        void lendingAppIdパラメータで指定されたデータが登録されていなければエラーになる() throws Exception {
            mvc.authSuzukiHanako.perform(get("/lendingapproval?lendingAppId=106"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapproval/lendingapproval"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(1))
                    .andExpect(errors().hasGlobalError("lendingapprovalForm", "LendingapprovalForm.lendingApp.nodataerr"));
        }

    }

    @Nested
    @SpringBootTest
    class 貸出承認画面の初期表示のテスト_正常処理 {

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Test
        @TestData("web/lendingapproval/testdata/001")
        void lendingAppIdパラメータで指定されたデータが登録されており承認権限を持つユーザならば貸出承認画面が表示される() throws Exception {
            mvc.authSuzukiHanako.perform(get("/lendingapproval?lendingAppId=105"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapproval/lendingapproval"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(html("#lendingapprovalForm > div > div > table > tbody > tr").count(3))
                    .andExpect(html("#lendingapprovalForm > div > div > table > tbody > tr:eq(0) > td:eq(1)")
                            .text("978-4-7741-5377-3"))
                    .andExpect(html("#lendingapprovalForm > div > div > table > tbody > tr:eq(0) > td:eq(2)")
                            .text("JUnit実践入門"))
                    .andExpect(html("#lendingapprovalForm > div > div > table > tbody > tr:eq(0) > td:eq(3)")
                            .text("開発で使用する為"))
                    .andExpect(html("#lendingapprovalForm > div > div > table > tbody > tr:eq(0) > td:eq(5) > input[type=text]")
                            .exists());
        }

    }

    @Nested
    @SpringBootTest
    class 貸出承認画面の入力チェックエラーのテスト {

        // テストデータ
        private LendingapprovalForm lendingapprovalForm_003
                = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_003.yaml"));
        private LendingapprovalForm lendingapprovalForm_006
                = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_006.yaml"));

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Autowired
        private MessagesPropertiesHelper mph;

        // FormValidator の入力チェックを呼び出せているかチェックできればよいので、１パターンだけテストする
        @Test
        void 一部の書籍は承認却下未選択で一部の書籍は却下理由未入力の場合は入力チェックエラー() throws Exception {
            mvc.authTanakaTaro.perform(TestHelper.postForm("/lendingapproval/complete", this.lendingapprovalForm_003).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapproval/lendingapproval"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(2))
                    .andExpect(errors().hasGlobalError("lendingapprovalForm", "LendingapprovalForm.applyingBookFormList.approvalResult.notAllCheckedErr"))
                    .andExpect(errors().hasFieldError("lendingapprovalForm", "applyingBookFormList[2].approvalReason"
                            , "LendingapprovalForm.applyingBookFormList.approvalReason.empty"))
                    .andExpect(html(".alert.alert-danger > p")
                            .text(mph.getMessage("LendingapprovalForm.applyingBookFormList.approvalResult.notAllCheckedErr", null)));
        }

        @Test
        void LendingapprovalForm_BeanValidationのテスト() throws Exception {
            mvc.authTanakaTaro.perform(TestHelper.postForm("/lendingapproval/complete", this.lendingapprovalForm_006).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapproval/lendingapproval"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(2))
                    .andExpect(errors().hasFieldError("lendingapprovalForm", "applyingBookFormList[0].approvalResult", "ValuesEnum"))
                    .andExpect(errors().hasFieldError("lendingapprovalForm", "applyingBookFormList[1].approvalReason", "Size"));
        }

    }

    @Nested
    @SpringBootTest
    class 貸出承認画面の正常処理時のテスト {

        // テストデータ
        private LendingapprovalForm lendingapprovalForm_004
                = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_004.yaml"));
        private LendingapprovalForm lendingapprovalForm_005
                = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_005.yaml"));

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @Autowired
        private DataSource dataSource;

        @RegisterExtension
        @Autowired
        public MailServerExtension mailServerExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Value("ksbysample/webapp/lending/web/lendingapproval/assertdata/001/message.txt")
        ClassPathResource messageTxt001Resource;

        @Value("ksbysample/webapp/lending/web/lendingapproval/assertdata/002/message.txt")
        ClassPathResource messageTxt002Resource;

        @Test
        @TestData("web/lendingapproval/testdata/001")
        void 確定ボタンをクリックした場合_承認() throws Exception {
            // when ( Spock Framework のブロックの区分けが分かりやすかったので、同じ部分にコメントで付けてみました )
            mvc.authSuzukiHanako.perform(TestHelper.postForm("/lendingapproval/complete", this.lendingapprovalForm_004).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapproval/lendingapproval"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(html(".alert.alert-success > p").text("確定しました"))
                    .andExpect(html("#lendingapprovalForm > div > div > table > tbody > tr:eq(0) > td:eq(5) > input[type=text]")
                            .notExists());

            // then ( Spock Framework のブロックの区分けが分かりやすかったので、同じ部分にコメントで付けてみました )
            // DB
            IDataSet dataSet = new CsvDataSet(new File("src/test/resources/ksbysample/webapp/lending/web/lendingapproval/assertdata/001"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEquals("lending_app", new String[]{"lending_app_id"});
            tableDataAssert.assertEquals("lending_book", new String[]{"lending_app_id", "isbn,book_name", "lending_state", "lending_app_flg", "lending_app_reason", "approval_reason"});
            // メール
            assertThat(mailServerExtension.getMessagesCount()).isEqualTo(1);
            MimeMessage mimeMessage = mailServerExtension.getFirstMessage();
            assertAll(
                    () -> assertThat(mimeMessage.getRecipients(javax.mail.Message.RecipientType.TO))
                            .extracting(Object::toString)
                            .containsOnly("tanaka.taro@sample.com"),
                    () -> assertThat(mimeMessage.getContent())
                            .isEqualTo(FileCopyUtils.copyToString(Files.newReader(
                                    messageTxt001Resource.getFile(), StandardCharsets.UTF_8)))
            );
        }

        @Test
        @TestData("web/lendingapproval/testdata/001")
        void 確定ボタンをクリックした場合_却下と却下理由() throws Exception {
            // when ( Spock Framework のブロックの区分けが分かりやすかったので、同じ部分にコメントで付けてみました )
            mvc.authSuzukiHanako.perform(TestHelper.postForm("/lendingapproval/complete", this.lendingapprovalForm_005).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapproval/lendingapproval"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(html("#lendingapprovalForm > div > div > table > tbody > tr:eq(0) > td:eq(5) > input[type=text]")
                            .notExists())
                    .andExpect(html("#lendingapprovalForm > div > div > table > tbody > tr:eq(0) > td:eq(5) > span")
                            .text("購入済です"));

            // then ( Spock Framework のブロックの区分けが分かりやすかったので、同じ部分にコメントで付けてみました )
            // DB
            IDataSet dataSet = new CsvDataSet(new File("src/test/resources/ksbysample/webapp/lending/web/lendingapproval/assertdata/002"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEquals("lending_app", new String[]{"lending_app_id"});
            tableDataAssert.assertEquals("lending_book", new String[]{"approval_result", "approval_reason", "version"}
                    , AssertOptions.INCLUDE_COLUMN);
            // メール
            assertThat(mailServerExtension.getMessagesCount()).isEqualTo(1);
            MimeMessage mimeMessage = mailServerExtension.getFirstMessage();
            assertAll(
                    () -> assertThat(mimeMessage.getRecipients(javax.mail.Message.RecipientType.TO))
                            .extracting(Object::toString)
                            .containsOnly("tanaka.taro@sample.com"),
                    () -> assertThat(mimeMessage.getContent())
                            .isEqualTo(FileCopyUtils.copyToString(Files.newReader(
                                    messageTxt002Resource.getFile(), StandardCharsets.UTF_8)))
            );
        }

    }

}
