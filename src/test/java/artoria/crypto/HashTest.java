package artoria.crypto;

import artoria.codec.HexUtils;
import artoria.util.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class HashTest {
    private static final Hash MD5 = new Hash(Hash.MD5);
    private static final Hash SHA1 = new Hash(Hash.SHA1);
    private static final Hash SHA256 = new Hash(Hash.SHA256);
    private static final Hash SHA384 = new Hash(Hash.SHA384);
    private static final Hash SHA512 = new Hash(Hash.SHA512);

    @Test
    public void hashString() throws Exception {
        String data = "1234567890";
        System.out.println(HexUtils.encodeToString(MD5.calc(data)));
        System.out.println(HexUtils.encodeToString(SHA1.calc(data)));
        System.out.println(HexUtils.encodeToString(SHA256.calc(data)));
        System.out.println(HexUtils.encodeToString(SHA384.calc(data)));
        System.out.println(HexUtils.encodeToString(SHA512.calc(data)));
    }

    @Test
    @Ignore
    public void hashFile() throws Exception {
        System.out.println("Please insure file is exists. ");
        File data = new File("e:\\123.md");
        Assert.isTrue(data.exists(), "File are not find. ");
        System.out.println(HexUtils.encodeToString(MD5.calc(data)));
        System.out.println(HexUtils.encodeToString(SHA1.calc(data)));
        System.out.println(HexUtils.encodeToString(SHA256.calc(data)));
        System.out.println(HexUtils.encodeToString(SHA384.calc(data)));
        System.out.println(HexUtils.encodeToString(SHA512.calc(data)));
    }

}
