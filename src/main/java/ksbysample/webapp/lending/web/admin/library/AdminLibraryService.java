package ksbysample.webapp.lending.web.admin.library;

import ksbysample.webapp.lending.dao.LibraryForsearchDao;
import ksbysample.webapp.lending.entity.LibraryForsearch;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * ???
 */
@Service
public class AdminLibraryService {

    private final LibraryForsearchDao libraryForsearchDao;

    /**
     * @param libraryForsearchDao ???
     */
    public AdminLibraryService(LibraryForsearchDao libraryForsearchDao) {
        this.libraryForsearchDao = libraryForsearchDao;
    }

    /**
     * library_forsearch テーブルのデータを全て削除してから、指定されたデータを１件登録する
     *
     * @param setSelectedLibraryForm ???
     */
    public void deleteAndInsertLibraryForSearch(SetSelectedLibraryForm setSelectedLibraryForm) {
        // library_forsearch テーブルのデータを全て削除する
        libraryForsearchDao.deleteAll();

        // 選択された図書館を登録する
        LibraryForsearch libraryForsearch = new LibraryForsearch();
        BeanUtils.copyProperties(setSelectedLibraryForm, libraryForsearch);
        libraryForsearchDao.insert(libraryForsearch);
    }

}
