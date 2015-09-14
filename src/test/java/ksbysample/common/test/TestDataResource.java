package ksbysample.common.test;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Component
public class TestDataResource extends ExternalResource {

    private final String TESTDATA_DIR = "src/test/resources/testdata/base";
    private final String BACKUP_FILE_NAME = "ksbylending_backup";
    private final List<String> BACKUP_TABLES = Arrays.asList(
            "user_info"
            , "user_role"
            , "library_forsearch"
    );

    @Autowired
    private DataSource dataSource;

    private File backupFile;
    
    @Override
    protected void before() throws Exception {
        IDatabaseConnection conn = null;
        try {
            conn = new DatabaseConnection(dataSource.getConnection());

            // バックアップを取得する
            QueryDataSet partialDataSet = new QueryDataSet(conn);
            for (String backupTable : BACKUP_TABLES) {
                partialDataSet.addTable(backupTable);
            }
            ReplacementDataSet replacementDatasetBackup = new ReplacementDataSet(partialDataSet);
            replacementDatasetBackup.addReplacementObject("", "[null]");
            backupFile = File.createTempFile(BACKUP_FILE_NAME, "xml");
            try (FileOutputStream fos = new FileOutputStream(backupFile)) {
                FlatXmlDataSet.write(replacementDatasetBackup, fos);
            }

            // テストデータに入れ替える
            IDataSet dataSet = new CsvDataSet(new File(TESTDATA_DIR));
            ReplacementDataSet replacementDataset = new ReplacementDataSet(dataSet);
            replacementDataset.addReplacementObject("[null]", null);
            DatabaseOperation.CLEAN_INSERT.execute(conn, replacementDataset);
        } finally {
            if (conn != null) conn.close();
        }
    }

    @Override
    protected void after() {
        try {
            IDatabaseConnection conn = null;
            try {
                conn = new DatabaseConnection(dataSource.getConnection());

                // バックアップからリストアする
                if (backupFile != null) {
                    IDataSet dataSet = new FlatXmlDataSetBuilder().build(backupFile);
                    ReplacementDataSet replacementDatasetRestore = new ReplacementDataSet(dataSet);
                    replacementDatasetRestore.addReplacementObject("[null]", null);
                    DatabaseOperation.CLEAN_INSERT.execute(conn, replacementDatasetRestore);
                }
            } finally {
                if (backupFile != null) {
                    Files.delete(backupFile.toPath());
                    backupFile = null;
                }
                try {
                    if (conn != null) conn.close();
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
