package ksbysample.webapp.lending.web.confirmresult;

import com.google.common.io.Files;
import ksbysample.common.test.extension.db.TestData;
import ksbysample.common.test.extension.db.TestDataExtension;
import ksbysample.common.test.extension.mockmvc.SecurityMockMvcExtension;
import ksbysample.common.test.helper.TestHelper;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static ksbysample.common.test.matcher.HtmlResultMatchers.html;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ConfirmresultControllerTest {

    @Nested
    @SpringBootTest
    class 貸出申請結果確認画面の初期表示のテスト_エラー処理 {

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Autowired
        private MessagesPropertiesHelper mph;

        @Test
        void ログインしていなければ貸出申請結果確認画面は表示できない() throws Exception {
            mvc.noauth.perform(get("/confirmresult?lendingAppId=105"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"));
        }

        @Test
        void lendingAppIdパラメータがなければエラーになる() throws Exception {
            mvc.authTanakaTaro.perform(get("/confirmresult"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andExpect(content().string(
                            containsString(mph.getMessage("ConfirmresultParamForm.lendingAppId.emptyerr", null))));
        }

        @Test
        void lendingAppIdパラメータで指定された値が数値でなければエラーになる() throws Exception {
            mvc.authTanakaTaro.perform(get("/confirmresult?lendingAppId=a"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andExpect(content().string(
                            containsString(mph.getMessage("ConfirmresultParamForm.lendingAppId.emptyerr", null))));
        }

        @Test
        @TestData("web/confirmresult/testdata/001")
        void lendingAppIdパラメータで指定されたデータが登録されていなければエラーになる() throws Exception {
            mvc.authTanakaTaro.perform(get("/confirmresult?lendingAppId=1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("confirmresult/confirmresult"))
                    .andExpect(html("#confirmresultForm > div > p")
                            .text(mph.getMessage("ConfirmresultForm.lendingApp.nodataerr", null)));
        }

        @Test
        @TestData("web/confirmresult/testdata/001")
        void 申請者でなければ貸出申請結果確認画面を表示できない() throws Exception {
            mvc.authSuzukiHanako.perform(get("/confirmresult?lendingAppId=105"))
                    .andExpect(status().isForbidden())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andExpect(content().string(
                            containsString(mph.getMessage("Confirmresult.lendingUserId.notequalerr", null))));
        }

    }

    @Nested
    @SpringBootTest
    class 貸出申請結果確認画面の初期表示のテスト_正常処理 {

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Test
        @TestData("web/confirmresult/testdata/001")
        void lendingAppIdパラメータで指定されたデータが登録されており申請者ならば貸出申請結果確認画面が表示される()
                throws Exception {
            mvc.authTanakaTaro.perform(get("/confirmresult?lendingAppId=105"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("confirmresult/confirmresult"))
                    .andExpect(html("#confirmresultForm > div > div > div:eq(0) > table > tbody > tr:eq(1) > td")
                            .text("承認済"))
                    .andExpect(html("#confirmresultForm > div > div > div:eq(0) > table > tbody > tr:eq(2) > td")
                            .text("tanaka taro"))
                    .andExpect(html("#confirmresultForm > div > div > div:eq(0) > table > tbody > tr:eq(3) > td")
                            .text("suzuki hanako"))
                    .andExpect(html("#confirmresultForm > div > div > table > tbody > tr")
                            .count(3))
                    .andExpect(html("#confirmresultForm > div > div > table > tbody > tr:eq(0) > td:eq(1)")
                            .text("978-4-7741-5377-3"))
                    .andExpect(html("#confirmresultForm > div > div > table > tbody > tr:eq(0) > td:eq(2)")
                            .text("JUnit実践入門"))
                    .andExpect(html("#confirmresultForm > div > div > table > tbody > tr:eq(0) > td:eq(3)")
                            .text("開発で使用する為"))
                    .andExpect(html("#confirmresultForm > div > div > table > tbody > tr:eq(0) > td:eq(4)")
                            .text("却下"))
                    .andExpect(html("#confirmresultForm > div > div > table > tbody > tr:eq(0) > td:eq(5)")
                            .text("購入済です"))
                    .andExpect(html("#confirmresultForm > div > div > table > tbody > tr:eq(1) > td:eq(4)")
                            .text("承認"));
        }

    }

    @Nested
    @SpringBootTest
    class 貸出申請結果確認画面の正常処理時のテスト {

        // テストデータ
        private ConfirmresultForm confirmresultForm_001
                = (ConfirmresultForm) new Yaml().load(getClass().getResourceAsStream("ConfirmresultForm_001.yaml"));

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Value("ksbysample/webapp/lending/web/confirmresult/assertdata/001/booklist-105.utf-8.csv")
        ClassPathResource booklist105Utf8CsvResource;

        @Value("ksbysample/webapp/lending/web/confirmresult/assertdata/002/booklist-105.ms932.csv")
        ClassPathResource booklist105Ms932CsvResource;

        @Test
        @TestData("web/confirmresult/testdata/001")
        void CSVダウンロード_HttpServletResponse_をクリックした場合() throws Exception {
            MvcResult result = mvc.authTanakaTaro.perform(
                    TestHelper.postForm("/confirmresult/filedownloadByResponse"
                            , this.confirmresultForm_001).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Disposition", "attachment; filename=\"booklist-105.csv\""))
                    .andExpect(content().encoding("UTF-8"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content)
                    .isEqualTo(FileCopyUtils.copyToString(Files.newReader(
                            booklist105Utf8CsvResource.getFile(), StandardCharsets.UTF_8)));
        }

        @Test
        @TestData("web/confirmresult/testdata/001")
        void CSVダウンロード_AbstractView_をクリックした場合() throws Exception {
            MvcResult result = mvc.authTanakaTaro.perform(
                    TestHelper.postForm("/confirmresult/filedownloadByView"
                            , this.confirmresultForm_001).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(header().string(
                            "Content-Disposition", "attachment; filename=\"booklist-105.csv\""))
                    .andExpect(content().encoding("MS932"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content)
                    .isEqualTo(FileCopyUtils.copyToString(Files.newReader(
                            booklist105Ms932CsvResource.getFile(), Charset.forName("MS932"))));
        }

    }

}
