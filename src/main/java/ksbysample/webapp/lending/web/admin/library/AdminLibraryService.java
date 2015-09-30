package ksbysample.webapp.lending.web.admin.library;

import ksbysample.webapp.lending.dao.LibraryForsearchDao;
import ksbysample.webapp.lending.entity.LibraryForsearch;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminLibraryService {

    @Autowired
    private LibraryForsearchDao libraryForsearchDao;
    
    public void deleteAndInsertLibraryForSearch(SetSelectedLibraryForm setSelectedLibraryForm) {
        // library_forsearch テーブルのデータを全て削除する
        libraryForsearchDao.deleteAll();

        // 選択された図書館を登録する
        LibraryForsearch libraryForsearch = new LibraryForsearch();
        BeanUtils.copyProperties(setSelectedLibraryForm, libraryForsearch);
        libraryForsearchDao.insert(libraryForsearch);
    }

}