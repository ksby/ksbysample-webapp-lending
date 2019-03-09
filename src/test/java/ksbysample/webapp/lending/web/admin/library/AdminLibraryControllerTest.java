package ksbysample.webapp.lending.web.admin.library;

import ksbysample.common.test.extension.db.TableDataAssert;
import ksbysample.common.test.extension.db.TestData;
import ksbysample.common.test.extension.db.TestDataExtension;
import ksbysample.common.test.extension.mockmvc.SecurityMockMvcExtension;
import ksbysample.common.test.helper.TestHelper;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.Yaml;

import javax.sql.DataSource;
import java.io.File;

import static ksbysample.common.test.matcher.HtmlResultMatchers.html;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminLibraryControllerTest {

    @Nested
    @SpringBootTest
    class 検索対象図書館登録画面の初期表示のテスト {

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Test
        void 管理権限を持つユーザは検索対象図書館登録画面を表示できる_図書館未選択時() throws Exception {
            mvc.authTanakaTaro.perform(get("/admin/library"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(html("p.navbar-text.noselected-library").text("※図書館が選択されていません"));
        }

        @Test
        @TestData("web/admin/library/testdata/001")
        void 管理権限を持つユーザは検索対象図書館登録画面を表示できる_図書館選択時() throws Exception {
            mvc.authTanakaTaro.perform(get("/admin/library"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(html("p.navbar-text.selected-library").text("選択中：図書館サンプル"));
        }

        @Test
        public void 管理権限のないユーザは検索対象図書館登録画面を表示できない() throws Exception {
            mvc.authSuzukiHanako.perform(get("/admin/library"))
                    .andExpect(status().isForbidden());
        }

    }

    @Nested
    @SpringBootTest
    class 検索ボタンクリック時のテスト {

        // テストデータ
        private SetSelectedLibraryForm setSelectedLibraryForm_001
                = (SetSelectedLibraryForm) new Yaml().load(
                getClass().getResourceAsStream("SetSelectedLibraryForm_001.yaml"));

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @Autowired
        private DataSource dataSource;

        @RegisterExtension
        @Autowired
        public SecurityMockMvcExtension mvc;

        @Test
        @TestData("web/admin/library/testdata/001")
        void 管理権限を持つユーザが検索ボタンをクリックすると図書館を登録できる() throws Exception {
            mvc.authTanakaTaro.perform(TestHelper.postForm("/admin/library/addSearchLibrary"
                    , this.setSelectedLibraryForm_001).with(csrf()))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/admin/library"))
                    .andExpect(model().hasNoErrors());

            IDataSet dataSet = new CsvDataSet(
                    new File("src/test/resources/ksbysample/webapp/lending/web/admin/library/assertdata/001"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEquals("library_forsearch", null);
        }

    }

}
