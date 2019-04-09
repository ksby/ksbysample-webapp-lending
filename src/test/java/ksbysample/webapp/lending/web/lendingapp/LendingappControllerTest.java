package ksbysample.webapp.lending.web.lendingapp;

import com.google.common.io.Files;
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
import java.util.List;

import static ksbysample.common.test.matcher.ErrorsResultMatchers.errors;
import static ksbysample.common.test.matcher.HtmlResultMatchers.html;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LendingappControllerTest {

    @Nested
    @SpringBootTest
    class 貸出申請画面の初期表示のテスト_エラー処理 {

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Autowired
        private MessagesPropertiesHelper mph;

        @Test
        void ログインしていなければ貸出申請画面は表示できない() throws Exception {
            mvc.noauth.perform(get("/lendingapp?lendingAppId=105"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"));
        }

        @Test
        void lendingAppIdパラメータがなければエラーになる() throws Exception {
            MvcResult result = mvc.authTanakaTaro.perform(get("/lendingapp"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content).contains(mph.getMessage("LendingappForm.lendingAppId.emptyerr", null));
        }

        @Test
        void lendingAppIdパラメータで指定された値が数値でなければエラーになる() throws Exception {
            MvcResult result = mvc.authTanakaTaro.perform(get("/lendingapp?lendingAppId=a"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content).contains(mph.getMessage("LendingappForm.lendingAppId.emptyerr", null));
        }

        @Test
        void lendingAppIdパラメータで指定されたデータが登録されていなければエラーになる() throws Exception {
            MvcResult result = mvc.authTanakaTaro.perform(get("/lendingapp?lendingAppId=1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content).contains(mph.getMessage("LendingappForm.lendingApp.nodataerr", null));
        }

    }

    @Nested
    @SpringBootTest
    class 貸出申請画面の初期表示のテスト_正常処理 {

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Test
        @TestData("web/lendingapp/testdata/001")
        void lendingAppIdパラメータで指定されたデータが登録されていれば貸出申請画面が表示される() throws Exception {
            MvcResult result = mvc.authTanakaTaro.perform(get("/lendingapp?lendingAppId=105"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapp/lendingapp"))
                    .andExpect(model().hasNoErrors())
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content)
                    .contains("978-4-7741-6366-6").contains("GitHub実践入門")
                    .contains("978-4-7973-8014-9").contains("Java最強リファレンス")
                    .contains("978-4-87311-704-1").contains("Javaによる関数型プログラミング")
                    .contains("978-4-7741-5377-3").contains("JUnit実践入門")
                    .contains("978-4-7973-4778-4").contains("アジャイルソフトウェア開発の奥義");
        }

    }

    @Nested
    @SpringBootTest
    class 貸出申請画面の入力チェックエラーのテスト {

        // テストデータ
        private LendingappForm lendingappForm_002
                = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_002.yaml"));
        private LendingappForm lendingappForm_003
                = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_003.yaml"));
        private LendingappForm lendingappForm_005
                = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_005.yaml"));

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Autowired
        private MessagesPropertiesHelper mph;

        @Test
        void 申請するが１つも選択されていない場合は入力チェックエラー() throws Exception {
            mvc.authTanakaTaro.perform(TestHelper.postForm("/lendingapp/apply", this.lendingappForm_002).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapp/lendingapp"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(4))
                    .andExpect(errors().hasGlobalError("lendingappForm", "LendingappForm.lendingBookDtoList.notExistApply"))
                    .andExpect(errors().hasFieldError("lendingappForm", "lendingBookDtoList[0].lendingAppFlg", "LendingappForm.lendingBookDtoList.notExistApply"))
                    .andExpect(errors().hasFieldError("lendingappForm", "lendingBookDtoList[1].lendingAppFlg", "LendingappForm.lendingBookDtoList.notExistApply"))
                    .andExpect(errors().hasFieldError("lendingappForm", "lendingBookDtoList[2].lendingAppFlg", "LendingappForm.lendingBookDtoList.notExistApply"))
                    .andExpect(html(".alert.alert-danger > p")
                            .text(mph.getMessage("LendingappForm.lendingBookDtoList.notExistApply", null)));
        }

        @Test
        void 申請するを選択して申請理由を入力していない場合は入力チェックエラー() throws Exception {
            mvc.authTanakaTaro.perform(TestHelper.postForm("/lendingapp/apply", this.lendingappForm_003).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapp/lendingapp"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(3))
                    .andExpect(errors().hasGlobalError("lendingappForm", "LendingappForm.lendingBookDtoList.emptyReason"))
                    .andExpect(errors().hasFieldError("lendingappForm", "lendingBookDtoList[0].lendingAppReason", "LendingappForm.lendingBookDtoList.emptyReason"))
                    .andExpect(errors().hasFieldError("lendingappForm", "lendingBookDtoList[2].lendingAppReason", "LendingappForm.lendingBookDtoList.emptyReason"));
        }

        @Test
        void 最大文字数オーバー_パターンエラーの場合は入力チェックエラー() throws Exception {
            mvc.authTanakaTaro.perform(TestHelper.postForm("/lendingapp/apply", this.lendingappForm_005).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapp/lendingapp"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(2))
                    .andExpect(errors().hasFieldError("lendingappForm", "lendingBookDtoList[0].lendingAppFlg", "Pattern"))
                    .andExpect(errors().hasFieldError("lendingappForm", "lendingBookDtoList[1].lendingAppReason", "Size"));
        }

    }

    @Nested
    @SpringBootTest
    class 貸出申請画面の正常処理時のテスト {

        // テストデータ
        private LendingappForm lendingappForm_006
                = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_006.yaml"));
        private LendingappForm lendingappForm_007
                = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_007.yaml"));

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

        @Value("ksbysample/webapp/lending/web/lendingapp/assertdata/001/message.txt")
        ClassPathResource messageTxtResource;

        @Test
        @TestData("web/lendingapp/testdata/001")
        void 申請ボタンをクリックした場合() throws Exception {
            // when ( Spock Framework のブロックの区分けが分かりやすかったので、同じ部分にコメントで付けてみました )
            mvc.authTanakaTaro.perform(TestHelper.postForm("/lendingapp/apply", this.lendingappForm_006).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapp/lendingapp"))
                    .andExpect(model().hasNoErrors());

            // then ( Spock Framework のブロックの区分けが分かりやすかったので、同じ部分にコメントで付けてみました )
            // DB
            IDataSet dataSet = new CsvDataSet(new File("src/test/resources/ksbysample/webapp/lending/web/lendingapp/assertdata/001"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEquals("lending_app", new String[]{"lending_app_id", "approval_user_id"});
            tableDataAssert.assertEquals("lending_book", new String[]{"lending_book_id", "lending_app_id", "lending_state", "lending_app_flg", "lending_app_reason", "approval_result", "approval_reason"});
            // メール ( To にメールアドレスを２つ指定しているためメールが２通送信される )
            assertThat(mailServerExtension.getMessagesCount()).isEqualTo(2);
            List<MimeMessage> mimeMessageList = mailServerExtension.getMessages();
            for (MimeMessage mimeMessage : mimeMessageList) {
                assertThat(mimeMessage.getRecipients(javax.mail.Message.RecipientType.TO))
                        .extracting(Object::toString)
                        .containsOnly("tanaka.taro@sample.com", "suzuki.hanako@test.co.jp");
                assertThat(mimeMessage.getContent())
                        .isEqualTo(FileCopyUtils.copyToString(Files.newReader(
                                messageTxtResource.getFile(), StandardCharsets.UTF_8)));
            }
        }

        @Test
        @TestData("web/lendingapp/testdata/001")
        void 一時保存ボタンをクリックした場合() throws Exception {
            mvc.authTanakaTaro.perform(TestHelper.postForm("/lendingapp/temporarySave", this.lendingappForm_007).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapp/lendingapp"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(html(".alert.alert-success > p").text("一時保存しました"));

            // then ( Spock Framework のブロックの区分けが分かりやすかったので、同じ部分にコメントで付けてみました )
            // DB
            IDataSet dataSet = new CsvDataSet(new File("src/test/resources/ksbysample/webapp/lending/web/lendingapp/assertdata/002"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEquals("lending_app", new String[]{"lending_app_id", "approval_user_id"});
            tableDataAssert.assertEquals("lending_book", new String[]{"lending_book_id", "lending_app_id", "lending_state", "lending_app_flg", "lending_app_reason", "approval_result", "approval_reason"});
        }

    }

}
