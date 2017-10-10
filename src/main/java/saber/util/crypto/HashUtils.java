package saber.util.crypto;

import saber.util.codec.HexUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class HashUtils {
    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();
    public static final String ALGORITHM_MD5 = "MD5";
    public static final String ALGORITHM_SHA1 = "SHA-1";
    public static final String ALGORITHM_SHA256 = "SHA-256";
    public static final String ALGORITHM_SHA384 = "SHA-384";
    public static final String ALGORITHM_SHA512 = "SHA-512";

    public static byte[] hash(File file, String algorithm)
            throws NoSuchAlgorithmException, IOException {
        FileInputStream in = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            in = new FileInputStream(file);
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            for (int len; (len = in.read(buffer)) != EOF;) {
                md.update(buffer, 0, len);
            }
            return md.digest();
        } finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    // ignore
                }
            }

        }
    }

    public static byte[] hash(byte[] data, String algorithm)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        return md.digest(data);
    }

    public static String md5f(File file)
            throws NoSuchAlgorithmException, IOException {
        byte[] bytes = hash(file, ALGORITHM_MD5);
        return HexUtils.encode(bytes);
    }

    public static byte[] md5(File file)
            throws NoSuchAlgorithmException, IOException {
        return hash(file, ALGORITHM_MD5);
    }

    public static String md5(String data)
            throws NoSuchAlgorithmException {
        return md5(data, DEFAULT_CHARSET_NAME);
    }

    public static String md5(String data, String charset)
            throws NoSuchAlgorithmException {
        byte[] bytes = data.getBytes(Charset.forName(charset));
        return HexUtils.encode(md5(bytes));
    }

    public static byte[] md5(byte[] data)
            throws NoSuchAlgorithmException {
        return hash(data, ALGORITHM_MD5);
    }

    public static String sha1f(File file)
            throws NoSuchAlgorithmException, IOException {
        byte[] bytes = hash(file, ALGORITHM_SHA1);
        return HexUtils.encode(bytes);
    }

    public static byte[] sha1(File file)
            throws NoSuchAlgorithmException, IOException {
        return hash(file, ALGORITHM_SHA1);
    }

    public static String sha1(String data)
            throws NoSuchAlgorithmException {
        return sha1(data, DEFAULT_CHARSET_NAME);
    }

    public static String sha1(String data, String charset)
            throws NoSuchAlgorithmException {
        byte[] bytes = data.getBytes(Charset.forName(charset));
        return HexUtils.encode(sha1(bytes));
    }

    public static byte[] sha1(byte[] data)
            throws NoSuchAlgorithmException {
        return hash(data, ALGORITHM_SHA1);
    }

    public static String sha256f(File file)
            throws NoSuchAlgorithmException, IOException {
        byte[] bytes = hash(file, ALGORITHM_SHA256);
        return HexUtils.encode(bytes);
    }

    public static byte[] sha256(File file)
            throws NoSuchAlgorithmException, IOException {
        return hash(file, ALGORITHM_SHA256);
    }

    public static String sha256(String data)
            throws NoSuchAlgorithmException {
        return sha256(data, DEFAULT_CHARSET_NAME);
    }

    public static String sha256(String data, String charset)
            throws NoSuchAlgorithmException {
        byte[] bytes = data.getBytes(Charset.forName(charset));
        return HexUtils.encode(sha256(bytes));
    }

    public static byte[] sha256(byte[] data)
            throws NoSuchAlgorithmException {
        return hash(data, ALGORITHM_SHA256);
    }

    public static String sha384f(File file)
            throws NoSuchAlgorithmException, IOException {
        byte[] bytes = hash(file, ALGORITHM_SHA384);
        return HexUtils.encode(bytes);
    }

    public static byte[] sha384(File file)
            throws NoSuchAlgorithmException, IOException {
        return hash(file, ALGORITHM_SHA384);
    }

    public static String sha384(String data)
            throws NoSuchAlgorithmException {
        return sha384(data, DEFAULT_CHARSET_NAME);
    }

    public static String sha384(String data, String charset)
            throws NoSuchAlgorithmException {
        byte[] bytes = data.getBytes(Charset.forName(charset));
        return HexUtils.encode(sha384(bytes));
    }

    public static byte[] sha384(byte[] data)
            throws NoSuchAlgorithmException {
        return hash(data, ALGORITHM_SHA384);
    }

    public static String sha512f(File file)
            throws NoSuchAlgorithmException, IOException {
        byte[] bytes = hash(file, ALGORITHM_SHA512);
        return HexUtils.encode(bytes);
    }

    public static byte[] sha512(File file)
            throws NoSuchAlgorithmException, IOException {
        return hash(file, ALGORITHM_SHA512);
    }

    public static String sha512(String data)
            throws NoSuchAlgorithmException {
        return sha512(data, DEFAULT_CHARSET_NAME);
    }

    public static String sha512(String data, String charset)
            throws NoSuchAlgorithmException {
        byte[] bytes = data.getBytes(Charset.forName(charset));
        return HexUtils.encode(sha512(bytes));
    }

    public static byte[] sha512(byte[] data)
            throws NoSuchAlgorithmException {
        return hash(data, ALGORITHM_SHA512);
    }

}
