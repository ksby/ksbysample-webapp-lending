package ksbysample.webapp.lending.helper.message;

import ksbysample.webapp.lending.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class MessagesPropertiesHelperTest {

    @Autowired
    private MessagesPropertiesHelper mph;

    @Test
    public void testGetMessage_NoArgs() throws Exception {
        String message
                = mph.getMessage("AbstractUserDetailsAuthenticationProvider.locked"
                , null);
        assertThat(message).isEqualTo("入力された ID はロックされています");
    }

    @Test
    public void testGetMessage_Args() throws Exception {
        int line = 1;
        int length = 3;
        String message
                = mph.getMessage("UploadBooklistForm.fileupload.lengtherr"
                , new Object[]{line, length});
        assertThat(message).isEqualTo("1行目のレコードの項目数が 2個ではありません ( 3個 )。");
    }
}
