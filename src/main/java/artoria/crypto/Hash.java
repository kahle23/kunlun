package artoria.crypto;

import artoria.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import static artoria.io.IOUtils.DEFAULT_BUFFER_SIZE;
import static artoria.io.IOUtils.EOF;

/**
 * Hash tools, in JDK, support list:
 * MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512.
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest">MessageDigest Algorithms</a>
 * @author Kahle
 */
public class Hash extends AbstractDigester {

    public Hash(String algorithm) {

        this.setAlgorithm(algorithm);
    }

    @Override
    public byte[] digest(byte[] data) throws GeneralSecurityException {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        MessageDigest md = MessageDigest.getInstance(this.getAlgorithm());
        return md.digest(data);
    }

    @Override
    public byte[] digest(InputStream inputStream) throws GeneralSecurityException, IOException {
        Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
        MessageDigest md = MessageDigest.getInstance(this.getAlgorithm());
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        for (int len; (len = inputStream.read(buffer)) != EOF;) {
            md.update(buffer, 0, len);
        }
        return md.digest();
    }

}
