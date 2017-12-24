package apyh.artoria.crypto;

import apyh.artoria.exception.ReflectionException;
import apyh.artoria.exception.UnexpectedException;
import apyh.artoria.util.Assert;
import apyh.artoria.util.ReflectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static apyh.artoria.util.StringConstant.SLASH;

public class CipherFactory {
    private static final Map<String, Class<? extends Cipher>> ciphers;

    static {
        ciphers = new ConcurrentHashMap<String, Class<? extends Cipher>>();
        ciphers.put("desede", DESede.class);
    }

    public static Class<? extends Cipher> unregisterCipher(String algorithm) {
        Assert.notBlank(algorithm, "Algorithm name must is not blank. ");
        return ciphers.remove(algorithm);
    }

    public static void registerCipher(String algorithm, Class<? extends Cipher> clazz) {
        Assert.notBlank(algorithm, "Algorithm name must is not blank. ");
        Assert.notNull(clazz, "Clazz must is not null. ");
        ciphers.put(algorithm, clazz);
    }

    public static Cipher getCipher(String transformation) {
        int index = transformation.indexOf(SLASH);
        String algorithm = transformation;
        if (index != -1) {
            algorithm = transformation.substring(0, index);
            algorithm = algorithm.toLowerCase();
        }
        Class<? extends Cipher> clazz = ciphers.get(algorithm);
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
