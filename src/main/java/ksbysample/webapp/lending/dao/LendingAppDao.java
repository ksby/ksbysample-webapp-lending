package ksbysample.webapp.lending.dao;

import ksbysample.webapp.lending.entity.LendingApp;
import ksbysample.webapp.lending.util.doma.ComponentAndAutowiredDomaConfig;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

/**
 */
@Dao
@ComponentAndAutowiredDomaConfig
public interface LendingAppDao {

    /**
     * @param lendingAppId ???
     * @return the LendingApp entity
     */
    @Select
    LendingApp selectById(Long lendingAppId);

    @Select(ensureResult = true)
    LendingApp selectByIdAndVersion(Long lendingAppId, Long version);

    @Select
    LendingApp selectById(Long lendingAppId, SelectOptions options);

    @Select(ensureResult = true)
    LendingApp selectByIdAndVersion(Long lendingAppId, Long version, SelectOptions options);

    @Select
    LendingApp selectByIdAndStatus(Long lendingAppId, List<String> statusList);

    @Select
    LendingApp selectByIdAndStatus(Long lendingAppId, List<String> statusList, SelectOptions options);

    /**
     * @param entity ???
     * @return affected rows
     */
    @Insert
    int insert(LendingApp entity);

    /**
     * @param entity ???
     * @return affected rows
     */
    @Update
    int update(LendingApp entity);

    /**
     * @param entity ???
     * @return affected rows
     */
    @Delete
    int delete(LendingApp entity);
}