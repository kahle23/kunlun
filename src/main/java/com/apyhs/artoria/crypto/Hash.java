package com.apyhs.artoria.crypto;

import com.apyhs.artoria.codec.Hex;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.apyhs.artoria.util.IOUtils.DEFAULT_BUFFER_SIZE;
import static com.apyhs.artoria.util.IOUtils.EOF;
import static com.apyhs.artoria.constant.Const.DEFAULT_CHARSET_NAME;

/**
 * Hash tools.
 * In JDK, support list:
 *  MD5
 *  SHA-1
 *  SHA-256
 *  SHA-384
 *  SHA-512
 *
 * @author Kahle
 */
public class Hash {
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA384 = "SHA-384";
    public static final String SHA512 = "SHA-512";

    public static Hash create() {
        return new Hash(Hash.MD5);
    }

    public static Hash create(String algorithm) {
        return new Hash(algorithm);
    }

    private String charset = DEFAULT_CHARSET_NAME;
    private Hex hex = Hex.ME;
    private String algorithm;

    private Hash(String algorithm) {
        this.setAlgorithm(algorithm);
    }

    public String getCharset() {
        return charset;
    }

    public Hash setCharset(String charset) {
        Assert.notBlank(charset, "Charset must is not blank. ");
        this.charset = charset;
        return this;
    }

    public Hex getHex() {
        return hex;
    }

    public Hash setHex(Hex hex) {
        Assert.notNull(hex, "Hex must is not null. ");
        this.hex = hex;
        return this;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Hash setAlgorithm(String algorithm) {
        Assert.notBlank(algorithm, "Algorithm must is not blank. ");
        this.algorithm = algorithm;
        return this;
    }

    public byte[] calc(String data) throws NoSuchAlgorithmException {
        Assert.notNull(data, "Data must is not null. ");
        Charset charset = Charset.forName(this.charset);
        byte[] bytes = data.getBytes(charset);
        return this.calc(bytes);
    }

    public byte[] calc(byte[] data) throws NoSuchAlgorithmException {
        Assert.notNull(data, "Data must is not null. ");
        MessageDigest md = MessageDigest.getInstance(algorithm);
        return md.digest(data);
    }

    public byte[] calc(File file) throws NoSuchAlgorithmException, IOException {
        Assert.notNull(file, "File must is not null. ");
        Assert.state(file.isFile(), "File object must is a file. ");
        FileInputStream in = new FileInputStream(file);
        try { return this.calc(in); }
        finally { IOUtils.closeQuietly(in); }
    }

    public byte[] calc(InputStream in) throws NoSuchAlgorithmException, IOException {
        Assert.notNull(in, "InputStream must is not null. ");
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        for (int len; (len = in.read(buffer)) != EOF;) {
            md.update(buffer, 0, len);
        }
        return md.digest();
    }

    public String calcToHexString(String data) throws NoSuchAlgorithmException {
        byte[] digest = this.calc(data);
        return hex.encodeToString(digest);
    }

    public String calcToHexString(byte[] data) throws NoSuchAlgorithmException {
        byte[] digest = this.calc(data);
        return hex.encodeToString(digest);
    }

    public String calcToHexString(File file) throws NoSuchAlgorithmException, IOException {
        byte[] digest = this.calc(file);
        return hex.encodeToString(digest);
    }

    public String calcToHexString(InputStream in) throws NoSuchAlgorithmException, IOException {
        byte[] digest = this.calc(in);
        return hex.encodeToString(digest);
    }

}
