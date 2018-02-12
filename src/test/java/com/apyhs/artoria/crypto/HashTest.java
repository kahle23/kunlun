package com.apyhs.artoria.crypto;

import com.apyhs.artoria.util.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class HashTest {
    private static final Hash md5 = Hash.create(Hash.MD5);
    private static final Hash sha1 = Hash.create(Hash.SHA1);
    private static final Hash sha256 = Hash.create(Hash.SHA256);
    private static final Hash sha384 = Hash.create(Hash.SHA384);
    private static final Hash sha512 = Hash.create(Hash.SHA512);

    @Test
    public void hashString() throws Exception {
        String data = "1234567890";
        System.out.println(md5.calcToHexString(data));
        System.out.println(sha1.calcToHexString(data));
        System.out.println(sha256.calcToHexString(data));
        System.out.println(sha384.calcToHexString(data));
        System.out.println(sha512.calcToHexString(data));
    }

    @Test
    @Ignore
    public void hashFile() throws Exception {
        System.out.println("Please insure file is exists. ");
        File data = new File("e:\\123.md");
        Assert.isTrue(data.exists(), "File are not find. ");
        System.out.println(md5.calcToHexString(data));
        System.out.println(sha1.calcToHexString(data));
        System.out.println(sha256.calcToHexString(data));
        System.out.println(sha384.calcToHexString(data));
        System.out.println(sha512.calcToHexString(data));
    }

}
