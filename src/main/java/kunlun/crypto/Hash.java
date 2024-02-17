/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto;

import kunlun.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import static kunlun.common.constant.Numbers.ZERO;
import static kunlun.io.util.IOUtils.DEFAULT_BUFFER_SIZE;
import static kunlun.io.util.IOUtils.EOF;

/**
 * Hash tools, in JDK, support list:
 * MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512.
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest">MessageDigest Algorithms</a>
 * @author Kahle
 */
public class Hash extends AbstractDigester {

    public Hash(String algorithm) {

        setAlgorithm(algorithm);
    }

    @Override
    public byte[] digest(byte[] data) throws GeneralSecurityException {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        String algorithm = getAlgorithm();
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        MessageDigest md = MessageDigest.getInstance(algorithm);
        return md.digest(data);
    }

    @Override
    public byte[] digest(InputStream inputStream) throws GeneralSecurityException, IOException {
        Assert.notNull(inputStream, "Parameter \"inputStream\" must not null. ");
        String algorithm = getAlgorithm();
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        for (int len; (len = inputStream.read(buffer)) != EOF;) {
            md.update(buffer, ZERO, len);
        }
        return md.digest();
    }

}
