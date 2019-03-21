package artoria.codec;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.reflect.ReflectUtils;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;

import static artoria.common.Constants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Base64Test {
    private static Logger log = LoggerFactory.getLogger(Base64Test.class);
    private String data =
            "-->> 行路难！行路难！多歧路，今安在？ <<--\n" +
            "-->> 长风破浪会有时，直挂云帆济沧海。 <<--";
    private byte[] dataBytes = data.getBytes(Charset.forName(UTF_8));
    private Base64 base64 = new Base64();

    private void change(boolean isJava8) throws Exception {
        Class<?>[] declaredClasses = Base64.class.getDeclaredClasses();
        Class<?> innerClass = null;
        for (Class<?> declaredClass : declaredClasses) {
            int modifiers = declaredClass.getModifiers();
            if (Modifier.isInterface(modifiers)) { continue; }
            String toString = declaredClass.toString();
            if (isJava8 && toString.contains("Java8")) {
                innerClass = declaredClass;
            }
            if (!isJava8 && toString.contains("Java7")) {
                innerClass = declaredClass;
            }
        }
        log.info("Inner class: {}", innerClass);
        Constructor<?> constructor = ReflectUtils.findConstructor(innerClass, Base64.class);
        log.info("Constructor info: {}", constructor);
        ReflectUtils.makeAccessible(constructor);
        Object instance = constructor.newInstance(base64);
        log.info("Inner class instance: {}", instance);
        Field delegate = ReflectUtils.findField(Base64.class, "delegate");
        ReflectUtils.makeAccessible(delegate);
        delegate.set(base64, instance);
    }

    private void testLogic(boolean isJava8) throws Exception {
        this.change(isJava8);
        String encode = base64.encodeToString(dataBytes);
        assertTrue(encode.contains(PLUS));
        assertTrue(encode.contains(SLASH));
        log.info(encode);
        byte[] decode = base64.decodeFromString(encode);
        String decodeStr = new String(decode, UTF_8);
        assertEquals(data, decodeStr);
        log.info(decodeStr);

        base64.setUrlSafe(true);
        this.change(isJava8);
        encode = base64.encodeToString(dataBytes);
        assertTrue(encode.contains(MINUS));
        assertTrue(encode.contains(UNDERLINE));
        log.info(encode);
        decode = base64.decodeFromString(encode);
        decodeStr = new String(decode, UTF_8);
        assertEquals(data, decodeStr);
        log.info(decodeStr);

        base64.setUrlSafe(false);
        base64.setMime(true);
        this.change(isJava8);
        encode = base64.encodeToString(dataBytes);
        assertTrue(encode.contains(PLUS));
        assertTrue(encode.contains(SLASH));
        assertTrue(encode.contains("\r\n"));
        log.info(encode);
        decode = base64.decodeFromString(encode);
        decodeStr = new String(decode, UTF_8);
        assertEquals(data, decodeStr);
        log.info(decodeStr);
    }

    @Test
    public void testJava7Base64() throws Exception {

        this.testLogic(false);
    }

    @Test
    public void testJava8Base64() throws Exception {

        this.testLogic(true);
    }

}
