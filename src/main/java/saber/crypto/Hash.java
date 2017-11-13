package saber.crypto;

import org.apache.commons.io.IOUtils;
import saber.codec.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Kahle
 */
public class Hash {

    public enum Algorithm {

        /**
         * MD5
         */
        MD5("MD5"),

        /**
         * SHA-1
         */
        SHA1("SHA-1"),

        /**
         * SHA-256
         */
        SHA256("SHA-256"),

        /**
         * SHA-384
         */
        SHA384("SHA-384"),

        /**
         * SHA-512
         */
        SHA512("SHA-512")

        ;

        public static Algorithm find(String algorithm) {
            Algorithm[] values = Algorithm.values();
            for (Algorithm value : values) {
                if (value.getAlgorithm().equalsIgnoreCase(algorithm)) {
                    return value;
                }
            }
            return null;
        }

        private String algorithm;

        Algorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        @Override
        public String toString() {
            return algorithm;
        }

    }

    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static final Algorithm DEFAULT_ALGORITHM = Algorithm.SHA1;
    public static final Hash MD5 = new Hash(Algorithm.MD5.toString());
    public static final Hash SHA1 = new Hash(Algorithm.SHA1.toString());
    public static final Hash SHA256 = new Hash(Algorithm.SHA256.toString());
    public static final Hash SHA384 = new Hash(Algorithm.SHA384.toString());
    public static final Hash SHA512 = new Hash(Algorithm.SHA512.toString());

    public static Hash on() {
        return new Hash(DEFAULT_ALGORITHM.toString());
    }

    public static Hash on(String algorithm) {
        return new Hash(algorithm);
    }

    public static Hash on(Algorithm algorithm) {
        return new Hash(algorithm.toString());
    }

    public static Hash on(String algorithm, String charset) {
        return new Hash(algorithm).setCharset(charset);
    }

    public static Hash on(Algorithm algorithm, String charset) {
        return new Hash(algorithm.toString()).setCharset(charset);
    }

    private String charset = Charset.defaultCharset().name();
    private Hex hex = Hex.on();
    private String algorithm;

    private Hash(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getCharset() {
        return charset;
    }

    public Hash setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public Hex getHex() {
        return hex;
    }

    public Hash setHex(Hex hex) {
        this.hex = hex;
        return this;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public Hash setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public byte[] digest(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        return md.digest(data);
    }

    public byte[] digest(String data) throws NoSuchAlgorithmException {
        return digest(data.getBytes(Charset.forName(charset)));
    }

    public byte[] digest(InputStream in) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        for (int len; (len = in.read(buffer)) != EOF;) {
            md.update(buffer, 0, len);
        }
        return md.digest();
    }

    public byte[] digest(File file) throws NoSuchAlgorithmException, IOException {
        FileInputStream in = new FileInputStream(file);
        try {
            return digest(in);
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

    public String digestToHex(byte[] data) throws NoSuchAlgorithmException {
        return hex.encodeToString(digest(data));
    }

    public String digestToHex(String data) throws NoSuchAlgorithmException {
        return hex.encodeToString(digest(data));
    }

    public String digestToHex(InputStream in) throws NoSuchAlgorithmException, IOException {
        return hex.encodeToString(digest(in));
    }

    public String digestToHex(File file) throws NoSuchAlgorithmException, IOException {
        return hex.encodeToString(digest(file));
    }

}
