/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.cipher.support;

import kunlun.crypto.cipher.SymmetricCipher;
import kunlun.util.Assert;

import static kunlun.crypto.util.CustomUtils.*;

/**
 * The custom encryption and decryption tools.
 * @author Kahle
 */
public class CustomCipher extends SymmetricCipher {

    @Override
    public byte[] encrypt(Config config, byte[] data) {
        Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
        byte[] encrypt = desEncrypt(data);
        encrypt = desEncrypt(addSalt(encrypt));
        encrypt = aesEncrypt(addSalt(encrypt));
        return encrypt;
    }

    @Override
    public byte[] decrypt(Config config, byte[] data) {
        Assert.notEmpty(data, "Parameter \"data\" must not empty. ");
        byte[] decrypt = aesDecrypt(data);
        decrypt = desDecrypt(removeSalt(decrypt));
        decrypt = desDecrypt(removeSalt(decrypt));
        return decrypt;
    }

}
