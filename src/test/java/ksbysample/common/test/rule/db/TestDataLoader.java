package ksbysample.common.test.rule.db;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;

@Component
public class TestDataLoader {

    private static final String NULL_STRING = "[null]";
    
    @Autowired
    private DataSource dataSource;

    public void load(String csvDir) {
        IDatabaseConnection conn = null;
        try {
            conn = new DatabaseConnection(dataSource.getConnection());

            IDataSet dataSet = new CsvDataSet(new File(csvDir));
            ReplacementDataSet replacementDataset = new ReplacementDataSet(dataSet);
            replacementDataset.addReplacementObject(NULL_STRING, null);
            DatabaseOperation.CLEAN_INSERT.execute(conn, replacementDataset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) {}
        }
    }

}
