package ksbysample.common.test.rule.db;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
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
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
public class TestDataResource extends TestWatcher {

    private static final String TESTDATA_BASE_DIR = "src/test/resources/testdata/base";
    private static final String BACKUP_FILE_NAME = "ksbylending_backup";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TestDataLoader testDataLoader;

    private File backupFile;

    @Override
    protected void starting(Description description) {
        IDatabaseConnection conn = null;
        try {
            // @NouseTestDataResource アノテーションがテストメソッドに付加されていない場合には処理を実行する
            if (!hasNoUseTestDataResourceAnnotation(description)) {
                conn = DbUnitUtils.createDatabaseConnection(dataSource);

                // バックアップ＆ロード＆リストア対象のテストデータのパスを取得する
                String testDataBaseDir = getBaseTestDir(description);

                // バックアップを取得する
                backupDb(conn, testDataBaseDir);

                // テストデータをロードする
                testDataLoader.load(testDataBaseDir);

                // テストメソッドに @TestData アノテーションが付加されている場合には、
                // アノテーションで指定されたテストデータをロードする
                loadTestData(description);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected void finished(Description description) {
        IDatabaseConnection conn = null;
        try {
            // @NouseTestDataResource アノテーションがテストメソッドに付加されていない場合には処理を実行する
            if (!hasNoUseTestDataResourceAnnotation(description)) {
                conn = DbUnitUtils.createDatabaseConnection(dataSource);

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

    private boolean hasNoUseTestDataResourceAnnotation(Description description) {
        Collection<Annotation> annotationList = description.getAnnotations();
        boolean result = annotationList.stream()
                .anyMatch(annotation -> annotation instanceof NoUseTestDataResource);
        return result;
    }

    private String getBaseTestDir(Description description) {
        // @BaseTestData アノテーションで指定されている場合にはそれを使用し、指定されていない場合には
        // TESTDATA_BASE_DIR 定数で指定されているものと使用する

        // テストメソッドに @BaseTestData アノテーションが付加されているかチェックする
        BaseTestData baseTestData = description.getAnnotation(BaseTestData.class);
        if (baseTestData != null) {
            return baseTestData.value();
        }

        // TestDataResource クラスのフィールドに @BaseTestData アノテーションが付加されているかチェックする
        Field[] fields = description.getTestClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(TestDataResource.class)) {
                baseTestData = field.getAnnotation(BaseTestData.class);
                if (baseTestData != null) {
                    return baseTestData.value();
                }
            }
        }

        // テストクラスに @BaseTestData アノテーションが付加されているかチェックする
        Class<?> testClass = description.getTestClass();
        baseTestData = AnnotationUtils.findAnnotation(testClass, BaseTestData.class);
        if (baseTestData != null) {
            return baseTestData.value();
        }

        return TESTDATA_BASE_DIR;
    }

    private void backupDb(IDatabaseConnection conn, String testDataBaseDir)
            throws DataSetException, IOException {
        QueryDataSet partialDataSet = new QueryDataSet(conn);

        // TESTDATA_BASE_DIR で指定されたディレクトリ内の table-ordering.txt に記述されたテーブル名一覧を取得し、
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

    private void loadTestData(Description description) {
        description.getAnnotations().stream()
                .filter(annotation -> annotation instanceof TestData)
                .forEach(annotation -> {
                    TestData testData = (TestData) annotation;
                    testDataLoader.load(testData.value());
                });
    }

}
