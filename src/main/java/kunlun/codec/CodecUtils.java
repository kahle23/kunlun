/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.codec;

import kunlun.codec.support.Base64;
import kunlun.codec.support.Hex;
import kunlun.codec.support.Unicode;
import kunlun.core.Codec;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The codec tools.
 * @author Kahle
 */
public class CodecUtils {
    private static final Logger log = LoggerFactory.getLogger(CodecUtils.class);
    private static volatile CodecManager codecManager;
    public static final String UNICODE = "unicode";
    public static final String BASE64  = "base64";
    public static final String HEX     = "hex";

    public static CodecManager getCodecManager() {
        if (codecManager != null) { return codecManager; }
        synchronized (CodecUtils.class) {
            if (codecManager != null) { return codecManager; }
            CodecUtils.setCodecManager(new SimpleCodecManager());
            // Register the default codec.
            registerCodec(UNICODE, new Unicode());
            registerCodec(BASE64, new Base64());
            registerCodec(HEX, new Hex());
            return codecManager;
        }
    }

    public static void setCodecManager(CodecManager codecManager) {
        Assert.notNull(codecManager, "Parameter \"codecManager\" must not null. ");
        log.info("Set codec manager: {}", codecManager.getClass().getName());
        CodecUtils.codecManager = codecManager;
    }

    public static void registerCodec(String name, Codec codec) {

        getCodecManager().registerCodec(name, codec);
    }

    public static void deregisterCodec(String name) {

        getCodecManager().deregisterCodec(name);
    }

    public static Codec getCodec(String name) {

        return getCodecManager().getCodec(name);
    }

    public static byte[] encode(String name, Codec.Config config, byte[] source) {

        return getCodecManager().encode(name, config, source);
    }

    public static byte[] decode(String name, Codec.Config config, byte[] source) {

        return getCodecManager().decode(name, config, source);
    }

    public static byte[] encode(String name, byte[] source) {

        return getCodecManager().encode(name, null, source);
    }

    public static byte[] decode(String name, byte[] source) {

        return getCodecManager().decode(name, null, source);
    }

    public static void encode(String name, Codec.Config config, InputStream source, OutputStream out) {

        getCodecManager().encode(name, config, source, out);
    }

    public static void decode(String name, Codec.Config config, InputStream source, OutputStream out) {

        getCodecManager().decode(name, config, source, out);
    }

    public static void encode(String name, InputStream source, OutputStream out) {

        getCodecManager().encode(name, null, source, out);
    }

    public static void decode(String name, InputStream source, OutputStream out) {

        getCodecManager().decode(name, null, source, out);
    }



    // ----

    public static String encodeToString(String name, Codec.Config config, byte[] source) {

        return getCodecManager().encodeToString(name, config, source);
    }

    public static byte[] decodeFromString(String name, Codec.Config config, String source) {

        return getCodecManager().decodeFromString(name, config, source);
    }

    public static String encodeToString(String name, byte[] source) {

        return getCodecManager().encodeToString(name, null, source);
    }

    public static byte[] decodeFromString(String name, String source) {

        return getCodecManager().decodeFromString(name, null, source);
    }

    // ----

    public static String encode(String name, Codec.Config config, String source) {

        return getCodecManager().encode(name, config, source);
    }

    public static String decode(String name, Codec.Config config, String source) {

        return getCodecManager().decode(name, config, source);
    }

    public static String encode(String name, String source) {

        return getCodecManager().encode(name, null, source);
    }

    public static String decode(String name, String source) {

        return getCodecManager().decode(name, null, source);
    }

}
