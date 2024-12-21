/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.digest.support;

import kunlun.crypto.digest.Hash;
import kunlun.exception.ExceptionUtils;
import kunlun.io.util.IOUtils;
import kunlun.util.Assert;

import java.io.InputStream;

import static kunlun.crypto.util.CustomUtils.*;

/**
 * The custom message digest tools.
 * @author Kahle
 */
public class CustomDigester extends Hash {

    @Override
    public byte[] digest(Config config, byte[] data) {
        Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
        byte[] digest = hmacSha512(addSalt(desEncrypt(data)));
        digest = hmacSha256(addSalt(digest));
        return digest;
    }

    @Override
    public byte[] digest(Config config, InputStream data) {
        Assert.notNull(data, "Parameter \"data\" must not null. ");
        try {
            return digest(config, IOUtils.toByteArray(data));
        } catch (Exception e) { throw ExceptionUtils.wrap(e); }
    }

}
