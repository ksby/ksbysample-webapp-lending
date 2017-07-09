package ksbysample.webapp.lending;

import ksbysample.webapp.lending.BrowfishUtils;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class SampleHelper {

    public String encrypt(String str) {
        try {
            return BrowfishUtils.encrypt(str);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

}
