package ksbysample.webapp.lending.helper.library;

import ksbysample.webapp.lending.dao.LibraryForsearchDao;
import ksbysample.webapp.lending.entity.LibraryForsearch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryHelperTest {

    @Autowired
    private LibraryHelper libraryHelper;

    @MockBean
    private LibraryForsearchDao libraryForsearchDao;

    @Test
    public void testGetSelectedLibrary_図書館が選択されていない場合() throws Exception {
        given(libraryForsearchDao.selectSelectedLibrary()).willReturn(null);

        String result = libraryHelper.getSelectedLibrary();
        assertThat(result).isEqualTo("※図書館が選択されていません");
    }

    @Test
    public void testGetSelectedLibrary_図書館が選択されている場合() throws Exception {
        LibraryForsearch libraryForsearch = new LibraryForsearch();
        libraryForsearch.setSystemid("System_Id");
        libraryForsearch.setFormal("図書館名");
        given(libraryForsearchDao.selectSelectedLibrary()).willReturn(libraryForsearch);

        String result = libraryHelper.getSelectedLibrary();
        assertThat(result).isEqualTo("選択中：図書館名");
    }

}
