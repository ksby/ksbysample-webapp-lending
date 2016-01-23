package ksbysample.webapp.lending.web.lendingapp;

import com.google.common.base.Charsets;
import ksbysample.common.test.rule.db.TableDataAssert;
import ksbysample.common.test.helper.TestHelper;
import ksbysample.common.test.rule.mail.MailServerResource;
import ksbysample.common.test.rule.mockmvc.SecurityMockMvcResource;
import ksbysample.common.test.rule.db.TestData;
import ksbysample.common.test.rule.db.TestDataResource;
import ksbysample.webapp.lending.Application;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.yaml.snakeyaml.Yaml;

import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.File;
import java.util.List;

import static ksbysample.common.test.matcher.ErrorsResultMatchers.errors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(Enclosed.class)
public class LendingappControllerTest {

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 貸出申請画面の初期表示のテスト_エラー処理 {

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Autowired
        private MessagesPropertiesHelper messagesPropertiesHelper;

        @Test
        public void ログインしていなければ貸出申請画面は表示できない() throws Exception {
            mvc.noauth.perform(get("/lendingapp?lendingAppId=105"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"));
        }

        @Test
        public void lendingAppIdパラメータがなければエラーになる() throws Exception {
            MvcResult result = mvc.authTanakaTaro.perform(get("/lendingapp"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content).contains(messagesPropertiesHelper.getMessage("LendingappForm.lendingAppId.emptyerr", null));
        }

        @Test
        public void lendingAppIdパラメータで指定された値が数値でなければエラーになる() throws Exception {
            MvcResult result = mvc.authTanakaTaro.perform(get("/lendingapp?lendingAppId=a"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content).contains(messagesPropertiesHelper.getMessage("LendingappForm.lendingAppId.emptyerr", null));
        }

        @Test
        public void lendingAppIdパラメータで指定されたデータが登録されていなければエラーになる() throws Exception {
            MvcResult result = mvc.authTanakaTaro.perform(get("/lendingapp?lendingAppId=1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content).contains(messagesPropertiesHelper.getMessage("LendingappForm.lendingApp.nodataerr", null));
        }

    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 貸出申請画面の初期表示のテスト_正常処理 {

        @Rule
        @Autowired
        public TestDataResource testDataResource;
        
        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Test
        @TestData("src/test/resources/ksbysample/webapp/lending/web/lendingapp/testdata/001")
        public void lendingAppIdパラメータで指定されたデータが登録されていれば貸出申請画面が表示される() throws Exception {
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

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 貸出申請画面の入力チェックエラーのテスト {

        // テストデータ
        private LendingappForm lendingappForm_002
                = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_002.yaml"));
        private LendingappForm lendingappForm_003
                = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_003.yaml"));
        private LendingappForm lendingappForm_005
                = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_005.yaml"));

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Test
        public void 申請するが１つも選択されていない場合は入力チェックエラー() throws Exception {
            mvc.authTanakaTaro.perform(TestHelper.postForm("/lendingapp/apply", this.lendingappForm_002).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapp/lendingapp"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(4))
                    .andExpect(errors().hasGlobalError("lendingappForm", "LendingappForm.lendingBookDtoList.notExistApply"))
                    .andExpect(errors().hasFieldError("lendingappForm", "lendingBookDtoList[0].lendingAppFlg", ""))
                    .andExpect(errors().hasFieldError("lendingappForm", "lendingBookDtoList[1].lendingAppFlg", ""))
                    .andExpect(errors().hasFieldError("lendingappForm", "lendingBookDtoList[2].lendingAppFlg", ""));
        }

        @Test
        public void 申請するを選択して申請理由を入力していない場合は入力チェックエラー() throws Exception {
            mvc.authTanakaTaro.perform(TestHelper.postForm("/lendingapp/apply", this.lendingappForm_003).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapp/lendingapp"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(3))
                    .andExpect(errors().hasGlobalError("lendingappForm", "LendingappForm.lendingBookDtoList.emptyReason"))
                    .andExpect(errors().hasFieldError("lendingappForm", "lendingBookDtoList[0].lendingAppReason", ""))
                    .andExpect(errors().hasFieldError("lendingappForm", "lendingBookDtoList[2].lendingAppReason", ""));
        }

        @Test
        public void 最大文字数オーバー_パターンエラーの場合は入力チェックエラー() throws Exception {
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

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 貸出申請画面の正常処理時のテスト {

        // テストデータ
        private LendingappForm lendingappForm_006
                = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_006.yaml"));
        private LendingappForm lendingappForm_007
                = (LendingappForm) new Yaml().load(getClass().getResourceAsStream("LendingappForm_007.yaml"));

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
        @TestData("src/test/resources/ksbysample/webapp/lending/web/lendingapp/testdata/001")
        public void 申請ボタンをクリックした場合() throws Exception {
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
            assertThat(mailServerResource.getMessagesCount()).isEqualTo(2);
            List<MimeMessage> mimeMessageList = mailServerResource.getMessages();
            for (MimeMessage mimeMessage : mimeMessageList) {
                assertThat(mimeMessage.getRecipients(javax.mail.Message.RecipientType.TO))
                        .extracting(Object::toString)
                        .containsOnly("tanaka.taro@sample.com", "suzuki.hanako@test.co.jp");
                assertThat(mimeMessage.getContent())
                        .isEqualTo(com.google.common.io.Files.toString(
                                new File("src/test/resources/ksbysample/webapp/lending/web/lendingapp/assertdata/001/message.txt")
                                , Charsets.UTF_8));
            }
        }

        @Test
        @TestData("src/test/resources/ksbysample/webapp/lending/web/lendingapp/testdata/001")
        public void 一時保存ボタンをクリックした場合() throws Exception {
            mvc.authTanakaTaro.perform(TestHelper.postForm("/lendingapp/temporarySave", this.lendingappForm_007).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("lendingapp/lendingapp"))
                    .andExpect(model().hasNoErrors());

            // then ( Spock Framework のブロックの区分けが分かりやすかったので、同じ部分にコメントで付けてみました )
            // DB
            IDataSet dataSet = new CsvDataSet(new File("src/test/resources/ksbysample/webapp/lending/web/lendingapp/assertdata/002"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEquals("lending_app", new String[]{"lending_app_id", "approval_user_id"});
            tableDataAssert.assertEquals("lending_book", new String[]{"lending_book_id", "lending_app_id", "lending_state", "lending_app_flg", "lending_app_reason", "approval_result", "approval_reason"});
        }

    }

}