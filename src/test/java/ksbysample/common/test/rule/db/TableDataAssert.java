package ksbysample.common.test.rule.db;

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

    private static final String NULL_STRING = "[null]";

    private final IDataSet dataSet;

    private final DataSource dataSource;

    public TableDataAssert(IDataSet dataSet, DataSource dataSource) {
        ReplacementDataSet replacementDataset = new ReplacementDataSet(dataSet);
        replacementDataset.addReplacementObject(NULL_STRING, null);
        this.dataSet = replacementDataset;
        this.dataSource = dataSource;
    }

    public void assertEquals(String tableName, String[] columnNames)
            throws DatabaseUnitException, SQLException {
        assertEquals(tableName, columnNames, AssertOptions.EXCLUDE_COLUM);
    }

    public void assertEquals(String tableName, String[] columnNames, AssertOptions options)
            throws DatabaseUnitException, SQLException {
        ITable expected = expectedTable(tableName, columnNames, options);
        ITable actual = actualTable(tableName, columnNames, options);
        Assertion.assertEquals(expected, actual);
    }

    public void assertEqualsByExcludingColumn(String tableName, String[] columnNames)
            throws DatabaseUnitException, SQLException {
        assertEquals(tableName, columnNames, AssertOptions.EXCLUDE_COLUM);
    }

    public void assertEqualsByIncludingColumn(String tableName, String[] columnNames)
            throws DatabaseUnitException, SQLException {
        assertEquals(tableName, columnNames, AssertOptions.INCLUDE_COLUMN);
    }
    
    private ITable expectedTable(String tableName, String[] columnNames, AssertOptions options) throws DataSetException {
        ITable table = dataSet.getTable(tableName);
        if (columnNames != null) {
            table = columnFilter(table, columnNames, options);
        }
        return table;
    }

    private ITable actualTable(String tableName, String[] columnNames, AssertOptions options)
            throws DatabaseUnitException, SQLException {
        IDatabaseConnection conn = new DatabaseConnection(this.dataSource.getConnection());
        ITable table = conn.createDataSet().getTable(tableName);
        if (columnNames != null) {
            table = columnFilter(table, columnNames, options);
        }
        return table;
    }

    private ITable columnFilter(ITable table, String[] columnNames, AssertOptions options)
            throws DataSetException {
        if (options == AssertOptions.EXCLUDE_COLUM) {
            table = DefaultColumnFilter.excludedColumnsTable(table, columnNames);
        }
        else {
            table = DefaultColumnFilter.includedColumnsTable(table, columnNames);
        }
        return table;
    }
    
}
