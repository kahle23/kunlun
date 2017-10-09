package saber;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.UUID;

public abstract class RandomUtils {
    private static final char[] DEFAULT_CHAR_ARRAY = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final SecureRandom random = new SecureRandom();

    public static UUID uuid() {
        return UUID.randomUUID();
    }

    public static BigDecimal number(int keyLength) {
        StringBuilder result = new StringBuilder();
        result.append(random.nextInt(9) + 1);
        for (int i = 0, len = keyLength - 1; i < len; i++) {
            result.append(random.nextInt(10));
        }
        return new BigDecimal(result.toString());
    }

    public static byte[] bytes(int keyLength) {
        byte[] bytes = new byte[keyLength];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String string(int keyLength) {
        return string(DEFAULT_CHAR_ARRAY, keyLength);
    }

    public static String string(char[] charArray, int keyLength) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, len = charArray.length; i < keyLength; i++)
            result.append(charArray[random.nextInt(len)]);
        return result.toString();
    }

}
