package ksbysample.webapp.lending.dao;

import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.util.doma.ComponentAndAutowiredDomaConfig;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.SelectOptions;

/**
 */
@Dao
@ComponentAndAutowiredDomaConfig
public interface LendingAppDao {

    /**
     * @param lendingAppId
     * @return the LendingApp entity
     */
    @Select
    LendingApp selectById(Long lendingAppId);
    @Select
    LendingApp selectById(Long lendingAppId, SelectOptions options);

    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(LendingApp entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(LendingApp entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(LendingApp entity);
}