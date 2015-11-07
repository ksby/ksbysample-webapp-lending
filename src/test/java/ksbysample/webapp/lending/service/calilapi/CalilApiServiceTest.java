package ksbysample.webapp.lending.service.calilapi;

import ksbysample.webapp.lending.Application;
import ksbysample.webapp.lending.service.calilapi.response.Libraries;
import ksbysample.webapp.lending.service.calilapi.response.LibrariesForJackson2Xml;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.xml.core.ValueRequiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CalilApiServiceTest {

    @Autowired
    private CalilApiService calilApiService;
    
    @Test
    public void testGetLibraryList_都道府県名が正しい場合() throws Exception {
        Libraries libraries = calilApiService.getLibraryList("東京都");
        // systemname が "国立国会図書館" のデータがあるかチェックする
        assertThat(libraries.getLibraryList())
                .extracting("systemname")
                .contains("国立国会図書館");
    }

    @Test
    public void testGetLibraryList_都道府県名が間違っている場合() throws Exception {
        assertThatThrownBy(() -> {
            Libraries libraries = calilApiService.getLibraryList("東a京都");
        }).isInstanceOf(ValueRequiredException.class);
    }

    @Test
    public void testGetLibraryListByJackson2Xml() throws Exception {
        LibrariesForJackson2Xml libraries = calilApiService.getLibraryListByJackson2Xml("東京都");
        assertThat(libraries).isNotNull();
        // systemname が "国立国会図書館" のデータがあるかチェックする
        assertThat(libraries.getLibraryList())
                .extracting("systemname")
                .contains("国立国会図書館");
    }
    
}
