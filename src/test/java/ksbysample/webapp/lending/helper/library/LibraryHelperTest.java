package ksbysample.webapp.lending.helper.library;

import ksbysample.webapp.lending.dao.LibraryForsearchDao;
import ksbysample.webapp.lending.entity.LibraryForsearch;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryHelperTest {

    @Tested
    private LibraryHelper libraryHelper;

    @Injectable
    private LibraryForsearchDao libraryForsearchDao;

    @Test
    public void testGetSelectedLibrary_図書館が選択されていない場合() throws Exception {
        new Expectations() {{
            libraryForsearchDao.selectSelectedLibrary();
            result = null;
        }};

        String result = libraryHelper.getSelectedLibrary();
        assertThat(result).isEqualTo("※図書館が選択されていません");
    }

    @Test
    public void testGetSelectedLibrary_図書館が選択されている場合() throws Exception {
        new Expectations() {{
            libraryForsearchDao.selectSelectedLibrary();
            result = new Delegate<LibraryForsearch>() {
                LibraryForsearch aDelegateMethod() {
                    LibraryForsearch libraryForsearch = new LibraryForsearch();
                    libraryForsearch.setSystemid("System_Id");
                    libraryForsearch.setFormal("図書館名");
                    return libraryForsearch;
                }
            };
        }};

        String result = libraryHelper.getSelectedLibrary();
        assertThat(result).isEqualTo("選択中：図書館名");
    }

}
