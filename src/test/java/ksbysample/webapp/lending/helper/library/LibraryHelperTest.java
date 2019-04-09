package ksbysample.webapp.lending.helper.library;

import ksbysample.webapp.lending.dao.LibraryForsearchDao;
import ksbysample.webapp.lending.entity.LibraryForsearch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class LibraryHelperTest {

    @Autowired
    private LibraryHelper libraryHelper;

    @MockBean
    private LibraryForsearchDao libraryForsearchDao;

    @Test
    void testGetSelectedLibrary_図書館が選択されていない場合() {
        given(libraryForsearchDao.selectSelectedLibrary()).willReturn(null);

        String result = libraryHelper.getSelectedLibrary();
        assertThat(result).isNull();
    }

    @Test
    void testGetSelectedLibrary_図書館が選択されている場合() {
        LibraryForsearch libraryForsearch = new LibraryForsearch();
        libraryForsearch.setSystemid("System_Id");
        libraryForsearch.setFormal("図書館名");
        given(libraryForsearchDao.selectSelectedLibrary()).willReturn(libraryForsearch);

        String result = libraryHelper.getSelectedLibrary();
        assertThat(result).isEqualTo("選択中：図書館名");
    }

}
