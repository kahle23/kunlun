package artoria.crypto;

import artoria.codec.Hex;
import artoria.util.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class HashTest {
    private static Hash md5 = Hash.getInstance(Hash.MD5);
    private static Hash sha1 = Hash.getInstance(Hash.SHA1);
    private static Hash sha256 = Hash.getInstance(Hash.SHA256);
    private static Hash sha384 = Hash.getInstance(Hash.SHA384);
    private static Hash sha512 = Hash.getInstance(Hash.SHA512);
    private static Hex hex = Hex.getInstance(true);

    @Test
    public void hashString() throws Exception {
        String data = "1234567890";
        System.out.println(hex.encodeToString(md5.digest(data)));
        System.out.println(hex.encodeToString(sha1.digest(data)));
        System.out.println(hex.encodeToString(sha256.digest(data)));
        System.out.println(hex.encodeToString(sha384.digest(data)));
        System.out.println(hex.encodeToString(sha512.digest(data)));
    }

    @Test
    @Ignore
    public void hashFile() throws Exception {
        System.out.println("Please insure file is exists. ");
        File data = new File("e:\\123.md");
        Assert.isTrue(data.exists(), "File are not find. ");
        System.out.println(hex.encodeToString(md5.digest(data)));
        System.out.println(hex.encodeToString(sha1.digest(data)));
        System.out.println(hex.encodeToString(sha256.digest(data)));
        System.out.println(hex.encodeToString(sha384.digest(data)));
        System.out.println(hex.encodeToString(sha512.digest(data)));
    }

}
