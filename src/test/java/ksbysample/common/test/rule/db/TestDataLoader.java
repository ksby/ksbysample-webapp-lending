package ksbysample.common.test.rule.db;

import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;

@Component
public class TestDataLoader {

    @Autowired
    private DataSource dataSource;

    public void load(String csvDir) {
        IDatabaseConnection conn = null;
        try (Connection connection = dataSource.getConnection()) {
            conn = DbUnitUtils.createDatabaseConnection(connection);
            load(conn, csvDir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    public void load(IDatabaseConnection conn, String csvDir) {
        try {
            IDataSet dataSet = new CsvDataSet(new File(csvDir));
            ReplacementDataSet replacementDataset = new ReplacementDataSet(dataSet);
            replacementDataset.addReplacementObject(DbUnitUtils.NULL_STRING, null);
            DatabaseOperation.CLEAN_INSERT.execute(conn, replacementDataset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
