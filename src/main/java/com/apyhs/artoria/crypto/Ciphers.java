package com.apyhs.artoria.crypto;

import com.apyhs.artoria.exception.ReflectionException;
import com.apyhs.artoria.logging.Logger;
import com.apyhs.artoria.logging.LoggerFactory;
import com.apyhs.artoria.util.Assert;
import com.apyhs.artoria.util.ClassUtils;
import com.apyhs.artoria.util.ReflectUtils;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.Provider;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Cipher tools.
 * @author Kahle
 */
public class Ciphers {
    private static final Logger log = LoggerFactory.getLogger(Ciphers.class);

    static {
        String className = "org.bouncycastle.jce.provider.BouncyCastleProvider";
        ClassLoader loader = ClassUtils.getDefaultClassLoader();
        if (ClassUtils.isPresent(className, loader)) {
            try {
                Object o = ReflectUtils.newInstance(className);
                Provider provider = (Provider) o;
                Security.addProvider(provider);
                String msg = "Init " + provider.getClass().getName();
                msg += " " + provider.getVersion();
                log.info(msg);
            }
            catch (ReflectionException e) {
                // ignore
            }
        }
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            String msg = "Provider: " + provider.getClass().getName();
            msg += "(Version: " + provider.getVersion() + ")";
            log.debug(msg);
        }
    }

    public static Cipher getEncrypter(String transformation, Key key)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher;
    }

    public static Cipher getEncrypter(String transformation, Key key, AlgorithmParameterSpec iv)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher;
    }

    public static Cipher getDecrypter(String transformation, Key key)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher;
    }

    public static Cipher getDecrypter(String transformation, Key key, AlgorithmParameterSpec iv)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher;
    }

    public static Cipher getInstance(String transformation, Key key, int opmode)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(opmode, key);
        return cipher;
    }

    public static Cipher getInstance(String transformation, Key key, AlgorithmParameterSpec iv, int opmode)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(opmode, key, iv);
        return cipher;
    }

    public static byte[] fill(byte[] data, int multiple) {
        Assert.notEmpty(data, "Data must is not empty. ");
        Assert.state(multiple > 0, "Multiple length must greater than 0. ");
        int len = data.length;
        int fill = len % multiple;
        fill = fill != 0 ? multiple - fill : 0;
        byte[] result = new byte[len + fill];
        System.arraycopy(data, 0, result, 0, len);
        return result;
    }

}
