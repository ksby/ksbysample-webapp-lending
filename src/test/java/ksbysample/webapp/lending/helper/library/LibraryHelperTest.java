package ksbysample.webapp.lending.helper.library;

import ksbysample.webapp.lending.Application;
import ksbysample.webapp.lending.dao.LibraryForsearchDao;
import ksbysample.webapp.lending.entity.LibraryForsearch;
import mockit.Delegate;
import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Tested;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class LibraryHelperTest {

    @Tested
    private LibraryHelper libraryHelper;

    @Injectable
    private LibraryForsearchDao libraryForsearchDao;

    @Test
    public void testGetSelectedLibrary_図書館が選択されていない場合() throws Exception {
        new NonStrictExpectations() {{
            libraryForsearchDao.selectSelectedLibrary(); result = null;
        }};

        String result = libraryHelper.getSelectedLibrary();
        assertThat(result).isEqualTo("※図書館が選択されていません");
    }

    @Test
    public void testGetSelectedLibrary_図書館が選択されている場合() throws Exception {
        new NonStrictExpectations() {{
            libraryForsearchDao.selectSelectedLibrary();
            result = new Delegate() {
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
