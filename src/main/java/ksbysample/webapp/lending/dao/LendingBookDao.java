package ksbysample.webapp.lending.dao;

import ksbysample.webapp.lending.entity.LendingBook;
import ksbysample.webapp.lending.util.doma.ComponentAndAutowiredDomaConfig;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

/**
 */
@Dao
@ComponentAndAutowiredDomaConfig
public interface LendingBookDao {

    /**
     * @param lendingBookId ???
     * @return the LendingBook entity
     */
    @Select
    LendingBook selectById(Long lendingBookId);

    /**
     * @param lendingBookId ???
     * @param version       ???
     * @return ???
     */
    @Select(ensureResult = true)
    LendingBook selectByIdAndVersion(Long lendingBookId, Long version);

    /**
     * @param lendingAppId ???
     * @return ???
     */
    @Select
    List<LendingBook> selectByLendingAppId(Long lendingAppId);

    /**
     * @param lendingAppId ???
     * @param options      ???
     * @return ???
     */
    @Select
    List<LendingBook> selectByLendingAppId(Long lendingAppId, SelectOptions options);

    /**
     * @param lendingAppId  ???
     * @param lendingAppFlg ???
     * @return ???
     */
    @Select
    List<LendingBook> selectByLendingAppIdAndLendingAppFlg(Long lendingAppId, String lendingAppFlg);

    /**
     * @param lendingAppId  ???
     * @param lendingAppFlg ???
     * @param options       ???
     * @return ???
     */
    @Select
    List<LendingBook> selectByLendingAppIdAndLendingAppFlg(Long lendingAppId, String lendingAppFlg
            , SelectOptions options);

    /**
     * @param entity ???
     * @return affected rows
     */
    @Insert
    int insert(LendingBook entity);

    /**
     * @param entity ???
     * @return affected rows
     */
    @Update
    int update(LendingBook entity);

    /**
     * @param entity ???
     * @return ???
     */
    @Update(include = {"lendingState"})
    int updateLendingState(LendingBook entity);

    /**
     * @param entity ???
     * @return ???
     */
    @Update(include = {"lendingAppFlg", "lendingAppReason"})
    int updateLendingAppFlgAndReason(LendingBook entity);

    /**
     * @param entity ???
     * @return ???
     */
    @Update(include = {"approvalResult", "approvalReason"})
    int updateApprovalResultAndReason(LendingBook entity);

    /**
     * @param entity ???
     * @return affected rows
     */
    @Delete
    int delete(LendingBook entity);
}