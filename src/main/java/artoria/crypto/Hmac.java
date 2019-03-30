package artoria.crypto;

import artoria.util.Assert;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
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
    private SecretKey secretKey;

    public Hmac(String algorithm) {

        this.setAlgorithm(algorithm);
    }

    public Hmac(String algorithm, SecretKey secretKey) {
        this.setAlgorithm(algorithm);
        this.setSecretKey(secretKey);
    }

    public SecretKey getSecretKey() {
        Assert.notNull(this.secretKey, "Please set the parameter \"secretKey\" first. ");
        return this.secretKey;
    }

    public void setSecretKey(SecretKey secretKey) {
        Assert.notNull(secretKey, "Parameter \"secretKey\" must not null. ");
        this.secretKey = secretKey;
    }

    @Override
    public byte[] digest(byte[] data) throws GeneralSecurityException {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        SecretKey secretKey = this.getSecretKey();
        Mac mac = Mac.getInstance(this.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    @Override
    public byte[] digest(InputStream inputStream) throws GeneralSecurityException, IOException {
        Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
        SecretKey secretKey = this.getSecretKey();
        Mac mac = Mac.getInstance(this.getAlgorithm());
        mac.init(secretKey);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        for (int len; (len = inputStream.read(buffer)) != EOF;) {
            mac.update(buffer, 0, len);
        }
        return mac.doFinal();
    }

}
