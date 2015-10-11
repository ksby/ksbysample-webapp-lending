package ksbysample.webapp.lending.dao;

import ksbysample.webapp.lending.entity.LendingBook;
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
public interface LendingBookDao {

    /**
     * @param lendingBookId
     * @return the LendingBook entity
     */
    @Select
    LendingBook selectById(Long lendingBookId);

    @Select
    List<LendingBook> selectByLendingAppId(Long lendingAppId);
    
    /**
     * @param entity
     * @return affected rows
     */
    @Insert
    int insert(LendingBook entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Update
    int update(LendingBook entity);

    /**
     * @param entity
     * @return affected rows
     */
    @Delete
    int delete(LendingBook entity);
}