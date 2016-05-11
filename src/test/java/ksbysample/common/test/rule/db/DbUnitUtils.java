package ksbysample.common.test.rule.db;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class DbUnitUtils {

    public static final String NULL_STRING = "[null]";

    public static IDatabaseConnection createDatabaseConnection(Connection connection)
            throws SQLException, DatabaseUnitException {
        IDatabaseConnection conn = new DatabaseConnection(connection);
        DatabaseConfig databaseConfig = conn.getConfig();
        databaseConfig.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        return conn;
    }

}
