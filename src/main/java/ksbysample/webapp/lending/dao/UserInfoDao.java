package ksbysample.webapp.lending.dao;

import ksbysample.webapp.lending.entity.UserInfo;
import ksbysample.webapp.lending.util.doma.ComponentAndAutowiredDomaConfig;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

import java.util.List;

/**
 */
@Dao
@ComponentAndAutowiredDomaConfig
public interface UserInfoDao {

    /**
     * @param userId
     * @return the UserInfo entity
     */
    @Select
    UserInfo selectById(Long userId);
    @Select
    UserInfo selectByMailAddress(String mailAddress);
    @Select
    List<String> selectApproverMailAddrList();
    
    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(UserInfo entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(UserInfo entity);
    @Update(sqlFile = true)
    int incCntBadcredentialsByMailAddress(String mailAddress);
    @Update(sqlFile = true)
    int initCntBadcredentialsByMailAddress(String mailAddress);
    
    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(UserInfo entity);
}