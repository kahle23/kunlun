package com.apyhs.artoria.crypto;

import org.junit.Test;

import java.io.File;

public class HashTest {
    private static final Hash md5 = Hash.create(Hash.MD5);
    private static final Hash sha1 = Hash.create(Hash.SHA1);
    private static final Hash sha256 = Hash.create(Hash.SHA256);
    private static final Hash sha384 = Hash.create(Hash.SHA384);
    private static final Hash sha512 = Hash.create(Hash.SHA512);

    @Test
    public void test1() throws Exception {
        String data = "1234567890";
        System.out.println(md5.calcToHexString(data));
        System.out.println(sha1.calcToHexString(data));
        System.out.println(sha256.calcToHexString(data));
        System.out.println(sha384.calcToHexString(data));
        System.out.println(sha512.calcToHexString(data));
    }

    @Test
    public void test2() throws Exception {
        File data = new File("e:\\123.md");
        System.out.println(md5.calcToHexString(data));
        System.out.println(sha1.calcToHexString(data));
        System.out.println(sha256.calcToHexString(data));
        System.out.println(sha384.calcToHexString(data));
        System.out.println(sha512.calcToHexString(data));
    }

}
