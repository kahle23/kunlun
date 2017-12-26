package apyh.artoria.crypto;

import apyh.artoria.exception.ReflectionException;
import apyh.artoria.logging.Logger;
import apyh.artoria.logging.LoggerFactory;
import apyh.artoria.util.Assert;
import apyh.artoria.util.ClassUtils;
import apyh.artoria.util.ReflectUtils;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.Provider;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

public class Ciphers {
    private static final Logger log = LoggerFactory.getLogger(Ciphers.class);

    static {
        String className = "org.bouncycastle.jce.provider.BouncyCastleProvider";
        ClassLoader loader = ClassUtils.getDefaultClassLoader();
        if (ClassUtils.isPresent(className, loader)) {
            try {
                Provider provider = (Provider) ReflectUtils.newInstance(className);
                Security.addProvider(provider);
            } catch (ReflectionException e) {
                // ignore
            }
        }
    }

    public static Cipher getInstance(String transformation, Key key, int opmode) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(opmode, key);
        return cipher;
    }

    public static Cipher getInstance(String transformation, Key key, AlgorithmParameterSpec iv, int opmode) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(opmode, key, iv);
        return cipher;
    }

    public static byte[] fill(byte[] data, int multiple) {
        Assert.notEmpty(data, "Data must is not empty. ");
        Assert.state(multiple > 0, "Fill length must greater than 0. ");
        int len = data.length;
        int fill = len % multiple;
        fill = fill != 0 ? multiple - fill : 0;
        byte[] result = new byte[len + fill];
        System.arraycopy(data, 0, result, 0, len);
        return result;
    }


}
