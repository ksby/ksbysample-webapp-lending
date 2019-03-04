package ksbysample.webapp.lending.helper.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MessagesPropertiesHelperTest {

    @Autowired
    private MessagesPropertiesHelper mph;

    @Test
    void testGetMessage_NoArgs() {
        String message
                = mph.getMessage("AbstractUserDetailsAuthenticationProvider.locked"
                , null);
        assertThat(message).isEqualTo("入力された ID はロックされています");
    }

    @Test
    void testGetMessage_Args() {
        int line = 1;
        int length = 3;
        String message
                = mph.getMessage("UploadBooklistForm.fileupload.lengtherr"
                , new Object[]{line, length});
        assertThat(message).isEqualTo("1行目のレコードの項目数が 2個ではありません ( 3個 )。");
    }
}
