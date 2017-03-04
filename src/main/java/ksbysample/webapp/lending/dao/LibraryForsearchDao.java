package ksbysample.webapp.lending.dao;

import ksbysample.webapp.lending.entity.LibraryForsearch;
import ksbysample.webapp.lending.util.doma.ComponentAndAutowiredDomaConfig;
import org.seasar.doma.*;

/**
 */
@Dao
@ComponentAndAutowiredDomaConfig
public interface LibraryForsearchDao {

    /**
     * @param systemid ???
     * @return the LibraryForsearch entity ???
     */
    @Select
    LibraryForsearch selectById(String systemid);

    /**
     * @return ???
     */
    @Select
    LibraryForsearch selectSelectedLibrary();

    /**
     * @param entity ???
     * @return affected rows ???
     */
    @Insert
    int insert(LibraryForsearch entity);

    /**
     * @param entity ???
     * @return affected rows ???
     */
    @Update
    int update(LibraryForsearch entity);

    /**
     * @param entity ???
     * @return affected rows ???
     */
    @Delete
    int delete(LibraryForsearch entity);

    /**
     * @return ???
     */
    @Delete(sqlFile = true)
    int deleteAll();
}