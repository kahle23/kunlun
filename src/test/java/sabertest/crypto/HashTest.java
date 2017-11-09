package sabertest.crypto;

import org.junit.Test;
import saber.crypto.Hash;

import java.io.File;

public class HashTest {

    @Test
    public void test1() throws Exception {
        String data = "1234567890";
        System.out.println(Hash.MD5.digestToHex(data));
        System.out.println(Hash.SHA1.digestToHex(data));
        System.out.println(Hash.SHA256.digestToHex(data));
        System.out.println(Hash.SHA384.digestToHex(data));
        System.out.println(Hash.SHA512.digestToHex(data));
    }

    @Test
    public void test2() throws Exception {
        File data = new File("e:\\123.md");
        System.out.println(Hash.MD5.digestToHex(data));
        System.out.println(Hash.SHA1.digestToHex(data));
        System.out.println(Hash.SHA256.digestToHex(data));
        System.out.println(Hash.SHA384.digestToHex(data));
        System.out.println(Hash.SHA512.digestToHex(data));
    }

}
