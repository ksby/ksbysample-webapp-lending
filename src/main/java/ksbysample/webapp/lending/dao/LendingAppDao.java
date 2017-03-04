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

    /**
     * @param lendingAppId ???
     * @param version      ???
     * @return ???
     */
    @Select(ensureResult = true)
    LendingApp selectByIdAndVersion(Long lendingAppId, Long version);

    /**
     * @param lendingAppId ???
     * @param options      ???
     * @return ???
     */
    @Select
    LendingApp selectById(Long lendingAppId, SelectOptions options);

    /**
     * @param lendingAppId ???
     * @param version      ???
     * @param options      ???
     * @return ???
     */
    @Select(ensureResult = true)
    LendingApp selectByIdAndVersion(Long lendingAppId, Long version, SelectOptions options);

    /**
     * @param lendingAppId ???
     * @param statusList   ???
     * @return ???
     */
    @Select
    LendingApp selectByIdAndStatus(Long lendingAppId, List<String> statusList);

    /**
     * @param lendingAppId ???
     * @param statusList   ???
     * @param options      ???
     * @return ???
     */
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