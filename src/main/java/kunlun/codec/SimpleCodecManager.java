/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.codec;

import kunlun.core.Codec;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The simple codec manager.
 * @author Kahle
 */
public class SimpleCodecManager implements CodecManager {
    private static final Logger log = LoggerFactory.getLogger(SimpleCodecManager.class);
    protected final Map<String, Object> commonProperties;
    protected final Map<String, Codec> codecs;

    protected SimpleCodecManager(Map<String, Object> commonProperties,
                                 Map<String, Codec> codecs) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(codecs, "Parameter \"codecs\" must not null. ");
        this.commonProperties = commonProperties;
        this.codecs = codecs;
    }

    public SimpleCodecManager() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, Codec>());
    }

    protected Codec getCodecOrThrow(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Codec codec = codecs.get(name);
        Assert.notNull(codec, "The corresponding codec could not be found by name. ");
        return codec;
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @Override
    public void registerCodec(String name, Codec codec) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Assert.notNull(codec, "Parameter \"codec\" must not null. ");
        String className = codec.getClass().getName();
        codecs.put(name, codec);
        log.debug("Register the codec \"{}\" to \"{}\". ", className, name);
    }

    @Override
    public void deregisterCodec(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        Codec remove = codecs.remove(name);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.debug("Deregister the codec \"{}\" from \"{}\". ", className, name);
        }
    }

    @Override
    public Codec getCodec(String name) {
        Assert.notBlank(name, "Parameter \"name\" must not blank. ");
        return codecs.get(name);
    }

    @Override
    public byte[] encode(String name, Codec.Config config, byte[] source) {

        return getCodecOrThrow(name).encode(config, source);
    }

    @Override
    public byte[] decode(String name, Codec.Config config, byte[] source) {

        return getCodecOrThrow(name).decode(config, source);
    }

    @Override
    public void encode(String name, Codec.Config config, InputStream source, OutputStream out) {

        getCodecOrThrow(name).encode(config, source, out);
    }

    @Override
    public void decode(String name, Codec.Config config, InputStream source, OutputStream out) {

        getCodecOrThrow(name).decode(config, source, out);
    }

    // ----

    @Override
    public String encodeToString(String name, Codec.Config config, byte[] source) {

        return ((ByteCodec) getCodecOrThrow(name)).encodeToString(config, source);
    }

    @Override
    public byte[] decodeFromString(String name, Codec.Config config, String source) {

        return ((ByteCodec) getCodecOrThrow(name)).decodeFromString(config, source);
    }

    // ----

    @Override
    public String encode(String name, Codec.Config config, String source) {

        return ((CharCodec) getCodecOrThrow(name)).encode(config, source);
    }

    @Override
    public String decode(String name, Codec.Config config, String source) {

        return ((CharCodec) getCodecOrThrow(name)).decode(config, source);
    }

}
