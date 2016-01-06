package ksbysample.webapp.lending.web.admin.library;

import ksbysample.common.test.TableDataAssert;
import ksbysample.common.test.TestData;
import ksbysample.common.test.TestDataResource;
import ksbysample.webapp.lending.Application;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.yaml.snakeyaml.Yaml;

import javax.sql.DataSource;
import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AdminLibraryServiceTest {

    // テストデータ
    private SetSelectedLibraryForm setSelectedLibraryForm_001
            = (SetSelectedLibraryForm) new Yaml().load(getClass().getResourceAsStream("SetSelectedLibraryForm_001.yaml"));

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminLibraryService adminLibraryService;
    
    @Test
    @TestData("src/test/resources/ksbysample/webapp/lending/web/admin/library/testdata/001")
    public void testDeleteAndInsertLibraryForSearch() throws Exception {
        adminLibraryService.deleteAndInsertLibraryForSearch(setSelectedLibraryForm_001);

        IDataSet dataSet = new CsvDataSet(new File("src/test/resources/ksbysample/webapp/lending/web/admin/library/assertdata/001"));
        TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
        tableDataAssert.assertEquals("library_forsearch", null);
    }

}