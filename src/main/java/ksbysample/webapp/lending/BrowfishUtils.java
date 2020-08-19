package ksbysample.webapp.lending;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * ???
 */
@SuppressWarnings({"PMD.HardCodedCryptoKey"})
@SuppressFBWarnings("CIPHER_INTEGRITY")
public class BrowfishUtils {

    private static final String KEY = "sample";
    private static final String ALGORITHM = "Blowfish";
    private static final String TRANSFORMATION = "Blowfish/ECB/PKCS5Padding";

    /**
     * 渡された文字列を Blowfish で暗号化する
     *
     * @param str 暗号化する文字列
     * @return 暗号化された文字列
     * @throws NoSuchPaddingException    ???
     * @throws NoSuchAlgorithmException  ???
     * @throws InvalidKeyException       ???
     * @throws BadPaddingException       ???
     * @throws IllegalBlockSizeException ???
     */
    public static String encrypt(String str)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
            , BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

}
