package artoria.crypto;

import artoria.codec.Hex;
import artoria.util.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class HashTest {
    private static Hash md5 = new Hash(Hash.MD5);
    private static Hash sha1 = new Hash(Hash.SHA1);
    private static Hash sha256 = new Hash(Hash.SHA256);
    private static Hash sha384 = new Hash(Hash.SHA384);
    private static Hash sha512 = new Hash(Hash.SHA512);
    private static Hex hex = Hex.getInstance(true);

    @Test
    public void hashString() throws Exception {
        String data = "1234567890";
        System.out.println(hex.encodeToString(md5.calc(data)));
        System.out.println(hex.encodeToString(sha1.calc(data)));
        System.out.println(hex.encodeToString(sha256.calc(data)));
        System.out.println(hex.encodeToString(sha384.calc(data)));
        System.out.println(hex.encodeToString(sha512.calc(data)));
    }

    @Test
    @Ignore
    public void hashFile() throws Exception {
        System.out.println("Please insure file is exists. ");
        File data = new File("e:\\123.md");
        Assert.isTrue(data.exists(), "File are not find. ");
        System.out.println(hex.encodeToString(md5.calc(data)));
        System.out.println(hex.encodeToString(sha1.calc(data)));
        System.out.println(hex.encodeToString(sha256.calc(data)));
        System.out.println(hex.encodeToString(sha384.calc(data)));
        System.out.println(hex.encodeToString(sha512.calc(data)));
    }

}
