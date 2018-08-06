package artoria.crypto;

import artoria.io.IOUtils;
import artoria.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static artoria.common.Constants.DEFAULT_CHARSET_NAME;
import static artoria.io.IOUtils.DEFAULT_BUFFER_SIZE;
import static artoria.io.IOUtils.EOF;

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

    private String charset = DEFAULT_CHARSET_NAME;
    private String algorithm;

    public Hash(String algorithm) {
        this.setAlgorithm(algorithm);
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        Assert.notBlank(charset, "Parameter \"charset\" must not blank. ");
        this.charset = charset;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        this.algorithm = algorithm;
    }

    public byte[] calc(String data) throws NoSuchAlgorithmException {
        Assert.notBlank(data, "Parameter \"data\" must not blank. ");
        Charset charset = Charset.forName(this.charset);
        byte[] bytes = data.getBytes(charset);
        return this.calc(bytes);
    }

    public byte[] calc(byte[] data) throws NoSuchAlgorithmException {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        MessageDigest md = MessageDigest.getInstance(algorithm);
        return md.digest(data);
    }

    public byte[] calc(File file) throws NoSuchAlgorithmException, IOException {
        Assert.notNull(file, "Parameter \"file\" must not null. ");
        FileInputStream in = new FileInputStream(file);
        try {
            return this.calc(in);
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

    public byte[] calc(InputStream in) throws NoSuchAlgorithmException, IOException {
        Assert.notNull(in, "Parameter \"in\" must not null. ");
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        for (int len; (len = in.read(buffer)) != EOF;) {
            md.update(buffer, 0, len);
        }
        return md.digest();
    }

}
