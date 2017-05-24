package ksbysample.webapp.lending.web.lendingapproval;

import com.google.common.base.Charsets;
import ksbysample.common.test.helper.TestHelper;
import ksbysample.common.test.rule.db.AssertOptions;
import ksbysample.common.test.rule.db.TableDataAssert;
import ksbysample.common.test.rule.db.TestData;
import ksbysample.common.test.rule.db.TestDataResource;
import ksbysample.common.test.rule.mail.MailServerResource;
import ksbysample.common.test.rule.mockmvc.SecurityMockMvcResource;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.yaml.snakeyaml.Yaml;

import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.File;

import static ksbysample.common.test.matcher.ErrorsResultMatchers.errors;
import static ksbysample.common.test.matcher.HtmlResultMatchers.html;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(Enclosed.class)
public class LendingapprovalControllerTest {

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class 貸出承認画面の初期表示のテスト_エラー処理_DBなし {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Autowired
        private MessagesPropertiesHelper mph;

        @Test
        public void ログインしていなければ貸出承認画面は表示できない() throws Exception {
            mvc.noauth.perform(get("/lendingapproval?lendingAppId=105"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"));
        }

        @Test
        public void 承認権限を持たないユーザは貸出承認画面を表示できない() throws Exception {
            mvc.authItoAoi.perform(get("/lendingapproval?lendingAppId=105"))
                    .andExpect(status().isForbidden());
        }

        @Test
        public void lendingAppIdパラメータがなければエラーになる() throws Exception {
            MvcResult result = mvc.authSuzukiHanako.perform(get("/lendingapproval"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content).contains(mph.getMessage("LendingapprovalParamForm.lendingAppId.emptyerr", null));
        }

        @Test
        public void lendingAppIdパラメータで指定された値が数値でなければエラーになる() throws Exception {
            MvcResult result = mvc.authSuzukiHanako.perform(get("/lendingapproval?lendingAppId=xxx"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content).contains(mph.getMessage("LendingapprovalParamForm.lendingAppId.emptyerr", null));
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class 貸出承認画面の初期表示のテスト_エラー処理_DBあり {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Autowired
        private MessagesPropertiesHelper mph;

        @Test
        public void lendingAppIdパラメータで指定されたデータが登録されていなければエラーになる() throws Exception {
            mvc.authSuzukiHanako.perform(get("/lendingapproval?lendingAppId=106"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapproval/lendingapproval"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(1))
                    .andExpect(errors().hasGlobalError("lendingapprovalForm", "LendingapprovalForm.lendingApp.nodataerr"));
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class 貸出承認画面の初期表示のテスト_正常処理 {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Test
        @TestData("web/lendingapproval/testdata/001")
        public void lendingAppIdパラメータで指定されたデータが登録されており承認権限を持つユーザならば貸出承認画面が表示される() throws Exception {
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

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class 貸出承認画面の入力チェックエラーのテスト {

        // テストデータ
        private LendingapprovalForm lendingapprovalForm_003
                = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_003.yaml"));
        private LendingapprovalForm lendingapprovalForm_006
                = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_006.yaml"));
        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Autowired
        private MessagesPropertiesHelper mph;

        // FormValidator の入力チェックを呼び出せているかチェックできればよいので、１パターンだけテストする
        @Test
        public void 一部の書籍は承認却下未選択で一部の書籍は却下理由未入力の場合は入力チェックエラー() throws Exception {
            mvc.authTanakaTaro.perform(TestHelper.postForm("/lendingapproval/complete", this.lendingapprovalForm_003).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapproval/lendingapproval"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(2))
                    .andExpect(errors().hasGlobalError("lendingapprovalForm", "LendingapprovalForm.applyingBookFormList.approvalResult.notAllCheckedErr"))
                    .andExpect(errors().hasFieldError("lendingapprovalForm", "applyingBookFormList[2].approvalReason", ""))
                    .andExpect(html(".alert.alert-danger > p")
                            .text(mph.getMessage("LendingapprovalForm.applyingBookFormList.approvalResult.notAllCheckedErr", null)));
        }

        @Test
        public void LendingapprovalForm_BeanValidationのテスト() throws Exception {
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

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class 貸出承認画面の正常処理時のテスト {

        // テストデータ
        private LendingapprovalForm lendingapprovalForm_004
                = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_004.yaml"));
        private LendingapprovalForm lendingapprovalForm_005
                = (LendingapprovalForm) new Yaml().load(getClass().getResourceAsStream("LendingapprovalForm_005.yaml"));

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Autowired
        private DataSource dataSource;

        @Rule
        @Autowired
        public MailServerResource mailServerResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Test
        @TestData("web/lendingapproval/testdata/001")
        public void 確定ボタンをクリックした場合_承認() throws Exception {
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
            assertThat(mailServerResource.getMessagesCount()).isEqualTo(1);
            MimeMessage mimeMessage = mailServerResource.getFirstMessage();
            assertThat(mimeMessage.getRecipients(javax.mail.Message.RecipientType.TO))
                    .extracting(Object::toString)
                    .containsOnly("tanaka.taro@sample.com");
            assertThat(mimeMessage.getContent())
                    .isEqualTo(com.google.common.io.Files.toString(
                            new File("src/test/resources/ksbysample/webapp/lending/web/lendingapproval/assertdata/001/message.txt")
                            , Charsets.UTF_8));
        }

        @Test
        @TestData("web/lendingapproval/testdata/001")
        public void 確定ボタンをクリックした場合_却下と却下理由() throws Exception {
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
            assertThat(mailServerResource.getMessagesCount()).isEqualTo(1);
            MimeMessage mimeMessage = mailServerResource.getFirstMessage();
            assertThat(mimeMessage.getRecipients(javax.mail.Message.RecipientType.TO))
                    .extracting(Object::toString)
                    .containsOnly("tanaka.taro@sample.com");
            assertThat(mimeMessage.getContent())
                    .isEqualTo(com.google.common.io.Files.toString(
                            new File("src/test/resources/ksbysample/webapp/lending/web/lendingapproval/assertdata/002/message.txt")
                            , Charsets.UTF_8));
        }

    }

}
