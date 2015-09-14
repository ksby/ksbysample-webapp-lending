package ksbysample.webapp.lending.helper.library;

import ksbysample.webapp.lending.dao.LibraryForsearchDao;
import ksbysample.webapp.lending.entity.LibraryForsearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LibraryHelper {
       
    @Autowired
    private LibraryForsearchDao libraryForsearchDao;
    
    public String getSelectedLibrary() {
        String result;
        LibraryForsearch libraryForsearch = libraryForsearchDao.selectSelectedLibrary();
        if (libraryForsearch == null) {
            result = "※図書館が選択されていません";
        }
        else {
            result = "選択中：" + libraryForsearch.getFormal();
        }

        return result;
    }

}
