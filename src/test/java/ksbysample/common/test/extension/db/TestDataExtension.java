package ksbysample.common.test.extension.db;

import ksbysample.common.test.helper.DescriptionWrapper;
import ksbysample.common.test.helper.ExtensionContextWrapper;
import ksbysample.common.test.helper.TestContextWrapper;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.Comparator.comparing;

@Component
public class TestDataExtension extends TestWatcher
        implements BeforeEachCallback, AfterEachCallback {

    private static final String BASETESTDATA_ROOT_DIR = "src/test/resources/";
    private static final String TESTDATA_ROOT_DIR = "src/test/resources/ksbysample/webapp/lending/";
    private static final String BASETESTDATA_DIR = BASETESTDATA_ROOT_DIR + "testdata/base";
    private static final String BACKUP_FILE_NAME = "ksbylending_backup";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TestDataLoader testDataLoader;

    private File backupFile;

    @Override
    protected void starting(Description description) {
        before(new DescriptionWrapper(description));
    }

    @SuppressWarnings("Finally")
    @Override
    protected void finished(Description description) {
        after(new DescriptionWrapper(description));
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        before(new ExtensionContextWrapper(context));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        after(new ExtensionContextWrapper(context));
    }

    private void before(TestContextWrapper testContextWrapper) {
        // @NouseTestDataResource アノテーションがテストメソッドに付加されていない場合には処理を実行する
        if (!hasNoUseTestDataResourceAnnotation(testContextWrapper)) {
            IDatabaseConnection conn = null;
            try (Connection connection = dataSource.getConnection()) {
                conn = DbUnitUtils.createDatabaseConnection(connection);

                // バックアップ＆ロード＆リストア対象のテストデータのパスを取得する
                String testDataBaseDir = getBaseTestDir(testContextWrapper);

                // バックアップを取得する
                backupDb(conn, testDataBaseDir);

                // テストデータをロードする
                testDataLoader.load(conn, testDataBaseDir);

                // @BaseTestSql アノテーションで指定された SQL を実行する
                TestSqlExecutor<BaseTestSqlList, BaseTestSql> baseTestSqlExecutor
                        = new TestSqlExecutor<>(BaseTestSqlList.class, BaseTestSql.class);
                baseTestSqlExecutor.execute(connection, testContextWrapper);

                // テストメソッドに @TestData アノテーションが付加されている場合には、
                // アノテーションで指定されたテストデータをロードする
                loadTestData(conn, testContextWrapper);

                // @TestSql アノテーションで指定された SQL を実行する
                TestSqlExecutor<TestSqlList, TestSql> testSqlExecutor
                        = new TestSqlExecutor<>(TestSqlList.class, TestSql.class);
                testSqlExecutor.execute(connection, testContextWrapper);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (conn != null) conn.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void after(TestContextWrapper testContextWrapper) {
        IDatabaseConnection conn = null;
        try (Connection connection = dataSource.getConnection()) {
            // @NouseTestDataResource アノテーションがテストメソッドに付加されていない場合には処理を実行する
            if (!hasNoUseTestDataResourceAnnotation(testContextWrapper)) {
                conn = DbUnitUtils.createDatabaseConnection(connection);

                // バックアップからリストアする
                restoreDb(conn);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) {
            }

            if (backupFile != null) {
                try {
                    Files.delete(backupFile.toPath());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                backupFile = null;
            }
        }
    }

    private boolean hasNoUseTestDataResourceAnnotation(TestContextWrapper testContextWrapper) {
        Collection<Annotation> annotationList = testContextWrapper.getAnnotations();
        boolean result = annotationList.stream()
                .anyMatch(annotation -> annotation instanceof NoUseTestDataResource);
        return result;
    }

    private String getBaseTestDir(TestContextWrapper testContextWrapper) {
        // @BaseTestData アノテーションで指定されている場合にはそれを使用し、指定されていない場合には
        // BASETESTDATA_DIR 定数で指定されているものと使用する

        // テストメソッドに @BaseTestData アノテーションが付加されているかチェックする
        BaseTestData baseTestData = testContextWrapper.getAnnotation(BaseTestData.class);
        if (baseTestData != null) {
            return BASETESTDATA_ROOT_DIR + baseTestData.value();
        }

        // TestDataResource クラスのフィールドに @BaseTestData アノテーションが付加されているかチェックする
        Field[] fields = testContextWrapper.getTestClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(TestDataExtension.class)) {
                baseTestData = field.getAnnotation(BaseTestData.class);
                if (baseTestData != null) {
                    return BASETESTDATA_ROOT_DIR + baseTestData.value();
                }
            }
        }

        // テストクラスに @BaseTestData アノテーションが付加されているかチェックする
        Class<?> testClass = testContextWrapper.getTestClass();
        baseTestData = testClass.getAnnotation(BaseTestData.class);
        if (baseTestData != null) {
            return BASETESTDATA_ROOT_DIR + baseTestData.value();
        }

        return BASETESTDATA_DIR;
    }

    private void backupDb(IDatabaseConnection conn, String testDataBaseDir)
            throws DataSetException, IOException {
        QueryDataSet partialDataSet = new QueryDataSet(conn);

        // BASETESTDATA_DIR で指定されたディレクトリ内の table-ordering.txt に記述されたテーブル名一覧を取得し、
        // バックアップテーブルとしてセットする
        List<String> backupTableList = Files.readAllLines(Paths.get(testDataBaseDir, "table-ordering.txt"));
        for (String backupTable : backupTableList) {
            partialDataSet.addTable(backupTable);
        }

        ReplacementDataSet replacementDatasetBackup = new ReplacementDataSet(partialDataSet);
        replacementDatasetBackup.addReplacementObject(null, DbUnitUtils.NULL_STRING);
        this.backupFile = File.createTempFile(BACKUP_FILE_NAME, "xml");
        try (FileOutputStream fos = new FileOutputStream(this.backupFile)) {
            FlatXmlDataSet.write(replacementDatasetBackup, fos);
        }
    }

    private void restoreDb(IDatabaseConnection conn)
            throws MalformedURLException, DatabaseUnitException, SQLException {
        if (this.backupFile != null) {
            IDataSet dataSet = new FlatXmlDataSetBuilder().build(this.backupFile);
            ReplacementDataSet replacementDatasetRestore = new ReplacementDataSet(dataSet);
            replacementDatasetRestore.addReplacementObject(DbUnitUtils.NULL_STRING, null);
            DatabaseOperation.CLEAN_INSERT.execute(conn, replacementDatasetRestore);
        }
    }

    private void loadTestData(IDatabaseConnection conn, TestContextWrapper testContextWrapper) {
        testContextWrapper.getAnnotations().stream()
                .filter(annotation -> annotation instanceof TestDataList || annotation instanceof TestData)
                .forEach(annotation -> {
                    if (annotation instanceof TestDataList) {
                        TestDataList testDataList = (TestDataList) annotation;
                        Arrays.asList(testDataList.value()).stream()
                                .sorted(comparing(testData -> testData.order()))
                                .forEach(testData -> testDataLoader.load(conn, TESTDATA_ROOT_DIR + testData.value()));
                    } else {
                        TestData testData = (TestData) annotation;
                        testDataLoader.load(conn, TESTDATA_ROOT_DIR + testData.value());
                    }
                });
    }

}
