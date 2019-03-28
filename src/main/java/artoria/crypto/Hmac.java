package artoria.crypto;

import artoria.util.Assert;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

import static artoria.io.IOUtils.DEFAULT_BUFFER_SIZE;
import static artoria.io.IOUtils.EOF;

/**
 * Hmac tools, in JDK, support list:
 * HMAC_MD5, HMAC_SHA1, HMAC_SHA256, HMAC_SHA384, HMAC_SHA512.
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Mac">Mac Algorithms</a>
 * @author Kahle
 */
public class Hmac extends AbstractDigester {
    private byte[] key;

    public Hmac(String algorithm) {

        this.setAlgorithm(algorithm);
    }

    public Hmac(String algorithm, byte[] key) {
        this.setAlgorithm(algorithm);
        this.setKey(key);
    }

    public byte[] getKey() {
        Assert.notNull(this.key, "Please set the parameter \"key\" first. ");
        return this.key;
    }

    public void setKey(byte[] key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        this.key = key;
    }

    public void setKey(String key) {
        Assert.notNull(key, "Parameter \"key\" must not null. ");
        Charset charset = Charset.forName(this.getCharset());
        this.key = key.getBytes(charset);
    }

    @Override
    public byte[] digest(byte[] data) throws GeneralSecurityException {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        SecretKey secretKey = new SecretKeySpec(this.getKey(), this.getAlgorithm());
        Mac mac = Mac.getInstance(this.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    @Override
    public byte[] digest(InputStream inputStream) throws GeneralSecurityException, IOException {
        Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
        SecretKey secretKey = new SecretKeySpec(this.getKey(), this.getAlgorithm());
        Mac mac = Mac.getInstance(this.getAlgorithm());
        mac.init(secretKey);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        for (int len; (len = inputStream.read(buffer)) != EOF;) {
            mac.update(buffer, 0, len);
        }
        return mac.doFinal();
    }

}
