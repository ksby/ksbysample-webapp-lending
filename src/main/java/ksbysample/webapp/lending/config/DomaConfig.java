package ksbysample.webapp.lending.config;

import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.NoCacheSqlFileRepository;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DomaConfig implements Config {

    private DataSource dataSource;

    @Autowired
    private Dialect dialect;

    private SqlFileRepository sqlFileRepository;

    public DomaConfig() {
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = new TransactionAwareDataSourceProxy(dataSource);
    }

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

    @Configuration
    protected static class DomaBeanConfig {

        @Value("${doma.dialect}")
        private String domaDialect;

        @Bean
        public Dialect dialect()
                throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            return (Dialect) Class.forName(domaDialect).newInstance();
        }

    }

}
