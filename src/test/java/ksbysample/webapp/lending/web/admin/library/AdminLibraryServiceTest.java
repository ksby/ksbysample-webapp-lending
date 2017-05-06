package ksbysample.webapp.lending.web.admin.library;

import ksbysample.common.test.rule.db.TableDataAssert;
import ksbysample.common.test.rule.db.TestData;
import ksbysample.common.test.rule.db.TestDataResource;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.yaml.snakeyaml.Yaml;

import javax.sql.DataSource;
import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminLibraryServiceTest {

    // テストデータ
    private SetSelectedLibraryForm setSelectedLibraryForm_001
            = (SetSelectedLibraryForm) new Yaml().load(
            getClass().getResourceAsStream("SetSelectedLibraryForm_001.yaml"));

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminLibraryService adminLibraryService;

    @Test
    @TestData("web/admin/library/testdata/001")
    public void testDeleteAndInsertLibraryForSearch() throws Exception {
        adminLibraryService.deleteAndInsertLibraryForSearch(setSelectedLibraryForm_001);

        IDataSet dataSet = new CsvDataSet(
                new File("src/test/resources/ksbysample/webapp/lending/web/admin/library/assertdata/001"));
        TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
        tableDataAssert.assertEquals("library_forsearch", null);
    }

}