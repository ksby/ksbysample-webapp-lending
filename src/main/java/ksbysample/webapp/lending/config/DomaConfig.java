package ksbysample.webapp.lending.config;

import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.NoCacheSqlFileRepository;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;

/**
 * ???
 */
@Component
public class DomaConfig implements Config {

    private DataSource dataSource;

    private Dialect dialect;

    private SqlFileRepository sqlFileRepository;

    /**
     *
     */
    public DomaConfig() {
    }

    /**
     * @param dataSource ???
     */
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = new TransactionAwareDataSourceProxy(dataSource);
    }

    /**
     * @param domaDialect ???
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @Autowired
    public void setDialect(@Value("${doma.dialect}") String domaDialect)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException
            , NoSuchMethodException, InvocationTargetException {
        this.dialect = (Dialect) Class.forName(domaDialect).getConstructor().newInstance();
    }

    /**
     * @param springProfilesActive ???
     */
    @Autowired
    public void setSqlFileRepository(@Value("${spring.profiles.active}") String springProfilesActive) {
        // develop モードの時は SQL ファイルがキャッシュされないようにする
        if (StringUtils.equals(springProfilesActive, "develop")) {
            this.sqlFileRepository = new NoCacheSqlFileRepository();
        } else {
            this.sqlFileRepository = new GreedyCacheSqlFileRepository();
        }
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public Dialect getDialect() {
        return this.dialect;
    }

    @Override
    public SqlFileRepository getSqlFileRepository() {
        return this.sqlFileRepository;
    }

}
