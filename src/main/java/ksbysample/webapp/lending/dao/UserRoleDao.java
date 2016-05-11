package ksbysample.webapp.lending.dao;

import ksbysample.webapp.lending.entity.UserRole;
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
public interface UserRoleDao {

    /**
     * @param roleId
     * @return the UserRole entity
     */
    @Select
    UserRole selectById(Long roleId);

    @Select
    List<UserRole> selectByUserId(Long userId);
    
    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(UserRole entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(UserRole entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(UserRole entity);
}