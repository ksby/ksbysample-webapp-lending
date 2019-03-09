package ksbysample.webapp.lending.web.admin.library;

import ksbysample.common.test.extension.db.TableDataAssert;
import ksbysample.common.test.extension.db.TestData;
import ksbysample.common.test.extension.db.TestDataExtension;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.Yaml;

import javax.sql.DataSource;
import java.io.File;

@SpringBootTest
public class AdminLibraryServiceTest {

    // テストデータ
    private SetSelectedLibraryForm setSelectedLibraryForm_001
            = (SetSelectedLibraryForm) new Yaml().load(
            getClass().getResourceAsStream("SetSelectedLibraryForm_001.yaml"));

    @RegisterExtension
    @Autowired
    public TestDataExtension testDataExtension;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminLibraryService adminLibraryService;

    @Test
    @TestData("web/admin/library/testdata/001")
    void testDeleteAndInsertLibraryForSearch() throws Exception {
        adminLibraryService.deleteAndInsertLibraryForSearch(setSelectedLibraryForm_001);

        IDataSet dataSet = new CsvDataSet(
                new File("src/test/resources/ksbysample/webapp/lending/web/admin/library/assertdata/001"));
        TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
        tableDataAssert.assertEquals("library_forsearch", null);
    }

}
