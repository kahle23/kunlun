package apyh.artoria.crypto;

import apyh.artoria.exception.ReflectionException;
import apyh.artoria.exception.UnexpectedException;
import apyh.artoria.util.Assert;
import apyh.artoria.util.ClassUtils;
import apyh.artoria.util.ReflectUtils;

import java.security.Provider;
import java.security.Security;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static apyh.artoria.util.StringConstant.SLASH;

/**
 * Cipher factory.
 * @author Kahle
 */
public class CipherFactory {
    private static final Map<String, Class<? extends Cipher>> CIPHERS;

    static {
        CIPHERS = new ConcurrentHashMap<String, Class<? extends Cipher>>();
        CIPHERS.put("des", DES.class);
        CIPHERS.put("aes", AES.class);
        CIPHERS.put("rsa", RSA.class);
        CIPHERS.put("desede", DESede.class);
        CIPHERS.put("blowfish", Blowfish.class);

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

    public static Class<? extends Cipher> unregisterCipher(String algorithm) {
        Assert.notBlank(algorithm, "Algorithm name must is not blank. ");
        return CIPHERS.remove(algorithm);
    }

    public static void registerCipher(String algorithm, Class<? extends Cipher> clazz) {
        Assert.notBlank(algorithm, "Algorithm name must is not blank. ");
        Assert.notNull(clazz, "Clazz must is not null. ");
        CIPHERS.put(algorithm, clazz);
    }

    public static Cipher getCipher(String transformation) {
        int index = transformation.indexOf(SLASH);
        String algorithm = transformation;
        if (index != -1) {
            algorithm = transformation.substring(0, index);
            algorithm = algorithm.toLowerCase();
        }
        Class<? extends Cipher> clazz = CIPHERS.get(algorithm);
        Assert.notNull(clazz, "Unregistered algorithm \"" + algorithm + "\". ");
        try {
            Cipher cipher = (Cipher) ReflectUtils.newInstance(clazz);
            cipher.setTransformation(transformation);
            return cipher;
        }
        catch (ReflectionException e) {
            throw new UnexpectedException(e);
        }
    }

}
