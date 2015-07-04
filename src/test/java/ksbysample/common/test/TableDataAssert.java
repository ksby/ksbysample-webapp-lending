package ksbysample.common.test;

import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.filter.DefaultColumnFilter;

import javax.sql.DataSource;
import java.sql.SQLException;

public class TableDataAssert {

    private final IDataSet dataSet;

    private final DataSource dataSource;

    public TableDataAssert(IDataSet dataSet, DataSource dataSource) {
        ReplacementDataSet replacementDataset = new ReplacementDataSet(dataSet);
        replacementDataset.addReplacementObject("[null]", null);
        this.dataSet = replacementDataset;
        this.dataSource = dataSource;
    }

    public void assertEquals(String tableName, String[] columnNames) throws DatabaseUnitException, SQLException {
        ITable expected = expectedTable(tableName, columnNames);
        ITable actual = actualTable(tableName, columnNames);
        Assertion.assertEquals(expected, actual);
    }

    private ITable expectedTable(String tableName, String[] columnNames) throws DataSetException {
        ITable table = dataSet.getTable(tableName);
        if (columnNames != null) {
            table = DefaultColumnFilter.excludedColumnsTable(table, columnNames);
        }
        return table;
    }

    private ITable actualTable(String tableName, String[] columnNames) throws DatabaseUnitException, SQLException {
        IDatabaseConnection conn = new DatabaseConnection(this.dataSource.getConnection());
        ITable table = conn.createDataSet().getTable(tableName);
        if (columnNames != null) {
            table = DefaultColumnFilter.excludedColumnsTable(table, columnNames);
        }
        return table;
    }

}
