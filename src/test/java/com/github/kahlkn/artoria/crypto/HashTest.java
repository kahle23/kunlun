package com.github.kahlkn.artoria.crypto;

import com.github.kahlkn.artoria.util.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class HashTest {
    private static final Hash MD5 = Hash.create(Hash.MD5);
    private static final Hash SHA1 = Hash.create(Hash.SHA1);
    private static final Hash SHA256 = Hash.create(Hash.SHA256);
    private static final Hash SHA384 = Hash.create(Hash.SHA384);
    private static final Hash SHA512 = Hash.create(Hash.SHA512);

    @Test
    public void hashString() throws Exception {
        String data = "1234567890";
        System.out.println(MD5.calcToHexString(data));
        System.out.println(SHA1.calcToHexString(data));
        System.out.println(SHA256.calcToHexString(data));
        System.out.println(SHA384.calcToHexString(data));
        System.out.println(SHA512.calcToHexString(data));
    }

    @Test
    @Ignore
    public void hashFile() throws Exception {
        System.out.println("Please insure file is exists. ");
        File data = new File("e:\\123.md");
        Assert.isTrue(data.exists(), "File are not find. ");
        System.out.println(MD5.calcToHexString(data));
        System.out.println(SHA1.calcToHexString(data));
        System.out.println(SHA256.calcToHexString(data));
        System.out.println(SHA384.calcToHexString(data));
        System.out.println(SHA512.calcToHexString(data));
    }

}
