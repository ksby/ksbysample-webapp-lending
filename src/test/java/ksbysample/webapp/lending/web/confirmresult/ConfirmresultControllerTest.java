package ksbysample.webapp.lending.web.confirmresult;

import com.google.common.base.Charsets;
import ksbysample.common.test.helper.TestHelper;
import ksbysample.common.test.rule.db.TestData;
import ksbysample.common.test.rule.db.TestDataResource;
import ksbysample.common.test.rule.mockmvc.SecurityMockMvcResource;
import ksbysample.webapp.lending.helper.message.MessagesPropertiesHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.nio.charset.Charset;

import static ksbysample.common.test.matcher.HtmlResultMatchers.html;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(Enclosed.class)
public class ConfirmresultControllerTest {

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class 貸出申請結果確認画面の初期表示のテスト_エラー処理 {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Autowired
        private MessagesPropertiesHelper mph;

        @Test
        public void ログインしていなければ貸出申請結果確認画面は表示できない() throws Exception {
            mvc.noauth.perform(get("/confirmresult?lendingAppId=105"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/"));
        }

        @Test
        public void lendingAppIdパラメータがなければエラーになる() throws Exception {
            mvc.authTanakaTaro.perform(get("/confirmresult"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andExpect(content().string(
                            containsString(mph.getMessage("ConfirmresultParamForm.lendingAppId.emptyerr", null))));
        }

        @Test
        public void lendingAppIdパラメータで指定された値が数値でなければエラーになる() throws Exception {
            mvc.authTanakaTaro.perform(get("/confirmresult?lendingAppId=a"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andExpect(content().string(
                            containsString(mph.getMessage("ConfirmresultParamForm.lendingAppId.emptyerr", null))));
        }

        @Test
        @TestData("web/confirmresult/testdata/001")
        public void lendingAppIdパラメータで指定されたデータが登録されていなければエラーになる() throws Exception {
            mvc.authTanakaTaro.perform(get("/confirmresult?lendingAppId=1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("confirmresult/confirmresult"))
                    .andExpect(html("#confirmresultForm > div > p")
                            .text(mph.getMessage("ConfirmresultForm.lendingApp.nodataerr", null)));
        }

        @Test
        @TestData("web/confirmresult/testdata/001")
        public void 申請者でなければ貸出申請結果確認画面を表示できない() throws Exception {
            mvc.authSuzukiHanako.perform(get("/confirmresult?lendingAppId=105"))
                    .andExpect(status().isForbidden())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("error"))
                    .andExpect(content().string(
                            containsString(mph.getMessage("Confirmresult.lendingUserId.notequalerr", null))));
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class 貸出申請結果確認画面の初期表示のテスト_正常処理 {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Test
        @TestData("web/confirmresult/testdata/001")
        public void lendingAppIdパラメータで指定されたデータが登録されており申請者ならば貸出申請結果確認画面が表示される()
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

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class 貸出申請結果確認画面の正常処理時のテスト {

        // テストデータ
        private ConfirmresultForm confirmresultForm_001
                = (ConfirmresultForm) new Yaml().load(getClass().getResourceAsStream("ConfirmresultForm_001.yaml"));

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public SecurityMockMvcResource mvc;

        @Test
        @TestData("web/confirmresult/testdata/001")
        public void CSVダウンロード_HttpServletResponse_をクリックした場合() throws Exception {
            MvcResult result = mvc.authTanakaTaro.perform(
                    TestHelper.postForm("/confirmresult/filedownloadByResponse"
                            , this.confirmresultForm_001).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Disposition", "attachment; filename=\"booklist-105.csv\""))
                    .andExpect(content().encoding("UTF-8"))
                    .andReturn();
            String content = result.getResponse().getContentAsString();
            assertThat(content)
                    .isEqualTo(com.google.common.io.Files.toString(
                            new File("src/test/resources/ksbysample/webapp/lending/web/confirmresult"
                                    + "/assertdata/001/booklist-105.utf-8.csv")
                            , Charsets.UTF_8));
        }

        @Test
        @TestData("web/confirmresult/testdata/001")
        public void CSVダウンロード_AbstractView_をクリックした場合() throws Exception {
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
                    .isEqualTo(com.google.common.io.Files.toString(
                            new File("src/test/resources/ksbysample/webapp/lending/web/confirmresult/assertdata/002/booklist-105.ms932.csv")
                            , Charset.forName("MS932")));
        }

    }

}
