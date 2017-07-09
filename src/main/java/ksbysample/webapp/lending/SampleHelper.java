package ksbysample.webapp.lending;

import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * ???
 */
@Component
public class SampleHelper {

    /**
     * 渡された文字列を暗号化する
     *
     * @param str 暗号化する文字列
     * @return 暗号化された文字列
     */
    public String encrypt(String str) {
        try {
            return BrowfishUtils.encrypt(str);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

}
