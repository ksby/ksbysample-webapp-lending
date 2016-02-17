package ksbysample.webapp.lending;

import ksbysample.common.test.rule.db.*;
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

import javax.sql.DataSource;
import java.io.File;

@RunWith(Enclosed.class)
public class TestDataResourceTest {

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    @BaseTestData("src/test/resources/testdata/base")
    @BaseTestSql(order = 1, sql = "insert into library_forsearch values ('Kanagawa_Sample', null)")
    @BaseTestSql(order = 2, sql = "update library_forsearch set formal = '図書館サンプル' where systemid = 'Kanagawa_Sample'")
    public static class テストクラス {

        @Rule
        @Autowired
        @BaseTestSql(sql = "update user_role set role = 'ROLE_APPROVER' where role_id in (6, 9)")
        public TestDataResource testDataResource;

        @Autowired
        private DataSource dataSource;

        @Test
        @BaseTestSql(sql = "delete from user_info where user_id in (4, 5, 6, 7, 8)")
        @BaseTestSql(sql = "update user_info set username = 'tanaka jiro' where user_id = 1")
        @TestData("src/test/resources/ksbysample/webapp/lending/web/confirmresult/testdata/001")
        @TestSql(sql = "update lending_app set status = '3' where lending_app_id = 105")
        public void テストメソッド() throws Exception {
            IDataSet dataSet = new CsvDataSet(new File("src/test/resources/testdata/assertdata"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEquals("lending_app", new String[]{}, AssertOptions.EXCLUDE_COLUM);
            tableDataAssert.assertEquals("lending_book", new String[]{}, AssertOptions.EXCLUDE_COLUM);
            tableDataAssert.assertEquals("library_forsearch", new String[]{}, AssertOptions.EXCLUDE_COLUM);
            tableDataAssert.assertEquals("user_info", new String[]{}, AssertOptions.EXCLUDE_COLUM);
            tableDataAssert.assertEquals("user_role", new String[]{}, AssertOptions.EXCLUDE_COLUM);
        }

    }

}
