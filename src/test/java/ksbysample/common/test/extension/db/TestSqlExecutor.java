package ksbysample.common.test.extension.db;

import ksbysample.common.test.helper.TestContextWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static java.util.Comparator.comparing;

public class TestSqlExecutor<L extends Annotation, I extends Annotation> {

    private Class<L> testSqlListClass;

    private Class<I> testSqlClass;

    public TestSqlExecutor(Class<L> testSqlListClass, Class<I> testSqlClass) {
        this.testSqlListClass = testSqlListClass;
        this.testSqlClass = testSqlClass;
    }

    public void execute(Connection connection, TestContextWrapper testContextWrapper) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // テストクラスに付加されている @BaseTestSql, @TestSql アノテーションの SQL を実行する
            Class<?> testClass = testContextWrapper.getTestClass();
            executeTestSqlListOrTestSql(stmt
                    , testClass.getAnnotation(this.testSqlListClass)
                    , testClass.getAnnotation(this.testSqlClass));

            // TestDataResource クラスのフィールドに付加されている @BaseTestSql, @TestSql アノテーションの SQL を実行する
            Field[] fields = testContextWrapper.getTestClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType().equals(TestDataExtension.class)) {
                    executeTestSqlListOrTestSql(stmt
                            , field.getAnnotation(this.testSqlListClass)
                            , field.getAnnotation(this.testSqlClass));
                }
            }

            // テストメソッドに付加されている @BaseTestSql, @TestSql アノテーションの SQL を実行する
            executeTestSqlListOrTestSql(stmt
                    , testContextWrapper.getAnnotation(this.testSqlListClass)
                    , testContextWrapper.getAnnotation(this.testSqlClass));
        }
    }

    private void executeTestSqlListOrTestSql(Statement stmt, L testSqlList, I testSql) {
        executeTestSqlList(stmt, testSqlList);
        executeTestSql(stmt, testSql);
    }

    private void executeTestSqlList(Statement stmt, L testSqlList) {
        if (testSqlList != null) {
            Arrays.asList(value(testSqlList)).stream()
                    .sorted(comparing(testSql -> order(testSql)))
                    .forEach(testSql -> executeTestSql(stmt, testSql));
        }
    }

    private void executeTestSql(Statement stmt, I testSql) {
        if (testSql != null) {
            try {
                stmt.execute(sql(testSql));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private I[] value(L testSqlList) {
        try {
            Method method = testSqlList.annotationType().getMethod("value");
            return (I[]) method.invoke(testSqlList);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private long order(I testSql) {
        try {
            Method method = testSql.annotationType().getMethod("order");
            return (long) method.invoke(testSql);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String sql(I testSql) {
        try {
            Method method = testSql.annotationType().getMethod("sql");
            return (String) method.invoke(testSql);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
