package ksbysample.webapp.lending.helper.library;

import ksbysample.webapp.lending.dao.LibraryForsearchDao;
import ksbysample.webapp.lending.entity.LibraryForsearch;
import org.springframework.stereotype.Component;

@Component
public class LibraryHelper {

    private final LibraryForsearchDao libraryForsearchDao;

    /**
     * @param libraryForsearchDao ???
     */
    public LibraryHelper(LibraryForsearchDao libraryForsearchDao) {
        this.libraryForsearchDao = libraryForsearchDao;
    }

    /**
     * @return ???
     */
    public String getSelectedLibrary() {
        String result = null;
        LibraryForsearch libraryForsearch = libraryForsearchDao.selectSelectedLibrary();
        if (libraryForsearch != null) {
            result = "選択中：" + libraryForsearch.getFormal();
        }
        return result;
    }

}
