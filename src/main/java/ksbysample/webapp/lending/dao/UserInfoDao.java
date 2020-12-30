package ksbysample.webapp.lending.dao;

import ksbysample.webapp.lending.entity.UserInfo;
import ksbysample.webapp.lending.util.doma.ComponentAndAutowiredDomaConfig;
import org.seasar.doma.*;

import java.util.List;

/**
 * ???
 */
@Dao
@ComponentAndAutowiredDomaConfig
public interface UserInfoDao {

    /**
     * ???
     *
     * @param userId ???
     * @return the UserInfo entity
     */
    @Select
    UserInfo selectById(Long userId);

    /**
     * ???
     *
     * @param mailAddress ???
     * @return ???
     */
    @Select
    UserInfo selectByMailAddress(String mailAddress);

    /**
     * ???
     *
     * @return ???
     */
    @Select
    List<String> selectApproverMailAddrList();

    /**
     * ???
     *
     * @param entity ???
     * @return affected rows
     */
    @Insert
    int insert(UserInfo entity);

    /**
     * ???
     *
     * @param entity ???
     * @return affected rows
     */
    @Update
    int update(UserInfo entity);

    /**
     * ???
     *
     * @param mailAddress ???
     * @return ???
     */
    @Update(sqlFile = true)
    int incCntBadcredentialsByMailAddress(String mailAddress);

    /**
     * ???
     *
     * @param mailAddress ???
     * @return ???
     */
    @Update(sqlFile = true)
    int initCntBadcredentialsByMailAddress(String mailAddress);

    /**
     * ???
     *
     * @param entity ???
     * @return affected rows
     */
    @Delete
    int delete(UserInfo entity);
}