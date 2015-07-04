package ksbysample.common.test;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Collection;

@Component
public class TestDataLoaderResource extends TestWatcher {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void starting(Description description) {
        try {
            IDatabaseConnection conn = null;
            try {
                conn = new DatabaseConnection(dataSource.getConnection());

                Collection<Annotation> annotationList = description.getAnnotations();
                for (Annotation annotation : annotationList) {
                    if (annotation instanceof TestDataLoader) {
                        TestDataLoader testDataLoader = (TestDataLoader)annotation;
                        IDataSet dataSet = new CsvDataSet(new File(testDataLoader.value()));
                        ReplacementDataSet replacementDataset = new ReplacementDataSet(dataSet);
                        replacementDataset.addReplacementObject("[null]", null);
                        DatabaseOperation.CLEAN_INSERT.execute(conn, replacementDataset);
                    }
                }
            } finally {
                if (conn != null) conn.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
