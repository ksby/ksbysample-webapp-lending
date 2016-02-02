package ksbysample.common.test.rule.db;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
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
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
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
    private static final String NULL_STRING = "[null]";

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
                conn = createDatabaseConnection(dataSource);

                // バックアップを取得する
                backupDb(conn);

                // TESTDATA_BASE_DIR で指定されたディレクトリ内のテストデータをロードする
                testDataLoader.load(TESTDATA_BASE_DIR);

                // テストメソッドに @TestData アノテーションが付加されている場合には、
                // アノテーションで指定されたテストデータをロードする
                loadTestData(description);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) {}
        }
    }

    @Override
    protected void finished(Description description) {
        IDatabaseConnection conn = null;
        try {
            // @NouseTestDataResource アノテーションがテストメソッドに付加されていない場合には処理を実行する
            if (!hasNoUseTestDataResourceAnnotation(description)) {
                conn = createDatabaseConnection(dataSource);

                // バックアップからリストアする
                restoreDb(conn);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) {}

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

    private IDatabaseConnection createDatabaseConnection(DataSource dataSource) throws SQLException, DatabaseUnitException {
        IDatabaseConnection conn = new DatabaseConnection(dataSource.getConnection());
        DatabaseConfig databaseConfig = conn.getConfig();
        databaseConfig.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        return conn;
    }

    private boolean hasNoUseTestDataResourceAnnotation(Description description) {
        Collection<Annotation> annotationList = description.getAnnotations();
        boolean result = annotationList.stream()
                .anyMatch(annotation -> annotation instanceof NoUseTestDataResource);
        return result;
    }

    private void backupDb(IDatabaseConnection conn)
            throws DataSetException, IOException {
        QueryDataSet partialDataSet = new QueryDataSet(conn);

        // TESTDATA_BASE_DIR で指定されたディレクトリ内の table-ordering.txt に記述されたテーブル名一覧を取得し、
        // バックアップテーブルとしてセットする
        List<String> backupTableList = Files.readAllLines(Paths.get(TESTDATA_BASE_DIR, "table-ordering.txt"));
        for (String backupTable :  backupTableList) {
            partialDataSet.addTable(backupTable);
        }

        ReplacementDataSet replacementDatasetBackup = new ReplacementDataSet(partialDataSet);
        replacementDatasetBackup.addReplacementObject(null, NULL_STRING);
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
            replacementDatasetRestore.addReplacementObject(NULL_STRING, null);
            DatabaseOperation.CLEAN_INSERT.execute(conn, replacementDatasetRestore);
        }
    }

    private void loadTestData(Description description) {
        description.getAnnotations().stream()
                .filter(annotation -> annotation instanceof TestData)
                .forEach(annotation -> {
                    TestData testData = (TestData)annotation;
                    testDataLoader.load(testData.value());
                });
    }

}
