package ksbysample.webapp.lending;

import ksbysample.common.test.extension.db.*;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.io.File;

public class TestDataResourceTest {

    @Nested
    @SpringBootTest
    @BaseTestData("testdata/base")
    @BaseTestSql(order = 1, sql = "insert into library_forsearch values ('Kanagawa_Sample', null)")
    @BaseTestSql(order = 2, sql = "update library_forsearch set formal = '図書館サンプル' where systemid = 'Kanagawa_Sample'")
    class テストクラス {

        @RegisterExtension
        @Autowired
        @BaseTestSql(sql = "update user_role set role = 'ROLE_APPROVER' where role_id in (6, 9)")
        public TestDataExtension testDataExtension;

        @Autowired
        private DataSource dataSource;

        @Test
        @BaseTestSql(sql = "delete from user_info where user_id in (4, 5, 6, 7, 8)")
        @BaseTestSql(sql = "update user_info set username = 'tanaka jiro' where user_id = 1")
        @TestData("web/confirmresult/testdata/001")
        @TestSql(sql = "update lending_app set status = '3' where lending_app_id = 105")
        void テストメソッド() throws Exception {
            IDataSet dataSet = new CsvDataSet(new File("src/test/resources/testdata/assertdata"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEqualsByQuery("select status from lending_app where lending_app_id = 105"
                    , "lending_app", new String[]{"status"}, AssertOptions.INCLUDE_COLUMN);
            tableDataAssert.assertEqualsByQuery("select * from lending_book order by lending_book_id", "lending_book");
            tableDataAssert.assertEquals("library_forsearch");
            tableDataAssert.assertEquals("user_info");
            tableDataAssert.assertEquals("user_role");
        }

    }

    @Nested
    @SpringBootTest
    class テストクラス_TestData_Annotation {

        @RegisterExtension
        @Autowired
        public TestDataExtension testDataExtension;

        @Autowired
        private DataSource dataSource;

        @Test
        @TestData("web/lendingapp/testdata/001")
        void テストメソッド_lendingapp() throws Exception {
            IDataSet dataSet = new CsvDataSet(
                    new File("src/test/resources/ksbysample/webapp/lending/web/lendingapp/testdata/001"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEquals("lending_app", new String[]{}, AssertOptions.EXCLUDE_COLUM);
            tableDataAssert.assertEquals("lending_book", new String[]{}, AssertOptions.EXCLUDE_COLUM);
        }

        @Test
        @TestData("web/confirmresult/testdata/001")
        void テストメソッド_confirmresult() throws Exception {
            IDataSet dataSet = new CsvDataSet(
                    new File("src/test/resources/ksbysample/webapp/lending/web/confirmresult/testdata/001"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEquals("lending_app", new String[]{}, AssertOptions.EXCLUDE_COLUM);
            tableDataAssert.assertEquals("lending_book", new String[]{}, AssertOptions.EXCLUDE_COLUM);
        }

        @Test
        @TestData(order = 1, value = "web/lendingapp/testdata/001")
        @TestData(order = 2, value = "web/confirmresult/testdata/001")
        void テストメソッド_lendingapp_confirmresult() throws Exception {
            IDataSet dataSet = new CsvDataSet(
                    new File("src/test/resources/ksbysample/webapp/lending/web/confirmresult/testdata/001"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEquals("lending_app", new String[]{}, AssertOptions.EXCLUDE_COLUM);
            tableDataAssert.assertEquals("lending_book", new String[]{}, AssertOptions.EXCLUDE_COLUM);
        }

        @Test
        @TestData(order = 2, value = "web/lendingapp/testdata/001")
        @TestData(order = 1, value = "web/confirmresult/testdata/001")
        void テストメソッド_confirmresult_lendingapp() throws Exception {
            IDataSet dataSet = new CsvDataSet(
                    new File("src/test/resources/ksbysample/webapp/lending/web/lendingapp/testdata/001"));
            TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
            tableDataAssert.assertEquals("lending_app", new String[]{}, AssertOptions.EXCLUDE_COLUM);
            tableDataAssert.assertEquals("lending_book", new String[]{}, AssertOptions.EXCLUDE_COLUM);
        }

    }

}
