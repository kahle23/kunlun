/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto;

import kunlun.core.Cipher;
import kunlun.core.Digester;
import kunlun.crypto.cipher.AbstractCipher;
import kunlun.crypto.cipher.AsymmetricCipher;
import kunlun.crypto.cipher.SymmetricCipher;
import kunlun.crypto.digest.AbstractDigester;
import kunlun.crypto.digest.Hash;
import kunlun.crypto.digest.Hmac;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static kunlun.common.constant.Words.DEFAULT;

public abstract class AbstractCryptoProvider implements CryptoProvider {
    private static final Logger log = LoggerFactory.getLogger(AbstractCryptoProvider.class);
    protected final Map<String, Digester> digesters;
    protected final Map<String, Cipher> ciphers;
    private AsymmetricCipher asymmetricCipher = new AsymmetricCipher();
    private SymmetricCipher symmetricCipher = new SymmetricCipher();
    private Hash hash = new Hash();
    private Hmac hmac = new Hmac();

    protected AbstractCryptoProvider(Map<String, Cipher> ciphers,
                                     Map<String, Digester> digesters) {
        Assert.notNull(digesters, "Parameter \"digesters\" must not null. ");
        Assert.notNull(ciphers, "Parameter \"ciphers\" must not null. ");
        this.digesters = digesters;
        this.ciphers = ciphers;
    }

    public AbstractCryptoProvider() {
        this(new ConcurrentHashMap<String, Cipher>(),
                new ConcurrentHashMap<String, Digester>());
    }

    protected Cipher getCipherOrThrow(Cipher.Config config) {
        Cipher result = config != null ? getCipher(config.getTransformation()) : getCipher(DEFAULT);
        if (result == null) {
            Assert.notNull(config, "Parameter \"config\" must not null. ");
            if (config instanceof SymmetricCipher.Cfg) {
                result = getSymmetricCipher();
            } else if (config instanceof AsymmetricCipher.Cfg) {
                result = getAsymmetricCipher();
            } else {
                throw new IllegalArgumentException("The config is not supported! ");
            }
        }
        return result;
    }

    protected Digester getDigesterOrThrow(Digester.Config config) {
        Digester result = config != null ? getDigester(config.getAlgorithm()) : getDigester(DEFAULT);
        if (result == null) {
            Assert.notNull(config, "Parameter \"config\" must not null. ");
            if (config instanceof Hash.Cfg) {
                result = getHash();
            } else if (config instanceof Hmac.Cfg) {
                result = getHmac();
            } else {
                throw new IllegalArgumentException("The config is not supported! ");
            }
        }
        return result;
    }

    // ====

    @Override
    public SymmetricCipher getSymmetricCipher() {

        return symmetricCipher;
    }

    public void setSymmetricCipher(SymmetricCipher symmetricCipher) {
        Assert.notNull(symmetricCipher, "Parameter \"symmetricCipher\" must not null. ");
        this.symmetricCipher = symmetricCipher;
    }

    @Override
    public AsymmetricCipher getAsymmetricCipher() {

        return asymmetricCipher;
    }

    public void setAsymmetricCipher(AsymmetricCipher asymmetricCipher) {
        Assert.notNull(asymmetricCipher, "Parameter \"asymmetricCipher\" must not null. ");
        this.asymmetricCipher = asymmetricCipher;
    }

    @Override
    public Hash getHash() {

        return hash;
    }

    public void setHash(Hash hash) {
        Assert.notNull(hash, "Parameter \"hash\" must not null. ");
        this.hash = hash;
    }

    @Override
    public Hmac getHmac() {

        return hmac;
    }

    public void setHmac(Hmac hmac) {
        Assert.notNull(hmac, "Parameter \"hmac\" must not null. ");
        this.hmac = hmac;
    }

    // ====

    @Override
    public void registerCipher(String transformation, Cipher cipher) {
        Assert.notBlank(transformation, "Parameter \"transformation\" must not blank. ");
        Assert.notNull(cipher, "Parameter \"cipher\" must not null. ");
        String className = cipher.getClass().getName();
        ciphers.put(transformation, cipher);
        log.info("Register the cipher \"{}\" to \"{}\". ", className, transformation);
    }

    @Override
    public void deregisterCipher(String transformation) {
        Assert.notBlank(transformation, "Parameter \"transformation\" must not blank. ");
        Cipher remove = ciphers.remove(transformation);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the cipher \"{}\" from \"{}\". ", className, transformation);
        }
    }

    @Override
    public Cipher getCipher(String transformation) {
        Assert.notBlank(transformation, "Parameter \"transformation\" must not blank. ");
        return ciphers.get(transformation);
    }

    @Override
    public void registerDigester(String algorithm, Digester digester) {
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        Assert.notNull(digester, "Parameter \"digester\" must not null. ");
        String className = digester.getClass().getName();
        digesters.put(algorithm, digester);
        log.info("Register the digester \"{}\" to \"{}\". ", className, algorithm);
    }

    @Override
    public void deregisterDigester(String algorithm) {
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        Digester remove = digesters.remove(algorithm);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the digester \"{}\" from \"{}\". ", className, algorithm);
        }
    }

    @Override
    public Digester getDigester(String algorithm) {
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        return digesters.get(algorithm);
    }

    // ====

    @Override
    public byte[] encrypt(Cipher.Config config, byte[] data) {

        return getCipherOrThrow(config).encrypt(config, data);
    }

    @Override
    public byte[] decrypt(Cipher.Config config, byte[] data) {

        return getCipherOrThrow(config).decrypt(config, data);
    }

    @Override
    public void encrypt(Cipher.Config config, InputStream data, OutputStream out) {

        getCipherOrThrow(config).encrypt(config, data, out);
    }

    @Override
    public void decrypt(Cipher.Config config, InputStream data, OutputStream out) {

        getCipherOrThrow(config).decrypt(config, data, out);
    }

    @Override
    public byte[] digest(Digester.Config config, byte[] data) {

        return getDigesterOrThrow(config).digest(config, data);
    }

    @Override
    public byte[] digest(Digester.Config config, InputStream data) {

        return getDigesterOrThrow(config).digest(config, data);
    }

    // ====

    public String encryptToHex(Cipher.Config config, byte[] data) {

        return ((AbstractCipher) getCipherOrThrow(config)).encryptToHex(config, data);
    }

    public String encryptToBase64(Cipher.Config config, byte[] data) {

        return ((AbstractCipher) getCipherOrThrow(config)).encryptToBase64(config, data);
    }

    public String decryptToString(Cipher.Config config, byte[] data, Charset charset) {

        return ((AbstractCipher) getCipherOrThrow(config)).decryptToString(config, data, charset);
    }

    public String decryptToString(Cipher.Config config, byte[] data) {

        return ((AbstractCipher) getCipherOrThrow(config)).decryptToString(config, data);
    }

    // ====

    public byte[] encrypt(Cipher.Config config, String data, Charset charset) {

        return ((AbstractCipher) getCipherOrThrow(config)).encrypt(config, data, charset);
    }

    public byte[] encrypt(Cipher.Config config, String data) {

        return ((AbstractCipher) getCipherOrThrow(config)).encrypt(config, data);
    }

    public String encryptToHex(Cipher.Config config, String data, Charset charset) {

        return ((AbstractCipher) getCipherOrThrow(config)).encryptToHex(config, data, charset);
    }

    public String encryptToHex(Cipher.Config config, String data) {

        return ((AbstractCipher) getCipherOrThrow(config)).encryptToHex(config, data);
    }

    public String encryptToBase64(Cipher.Config config, String data, Charset charset) {

        return ((AbstractCipher) getCipherOrThrow(config)).encryptToBase64(config, data, charset);
    }

    public String encryptToBase64(Cipher.Config config, String data) {

        return ((AbstractCipher) getCipherOrThrow(config)).encryptToBase64(config, data);
    }

    // ====

    public String digestToHex(Digester.Config config, byte[] data) {

        return ((AbstractDigester) getDigesterOrThrow(config)).digestToHex(config, data);
    }

    public String digestToBase64(Digester.Config config, byte[] data) {

        return ((AbstractDigester) getDigesterOrThrow(config)).digestToBase64(config, data);
    }

    public String digestToHex(Digester.Config config, InputStream data) {

        return ((AbstractDigester) getDigesterOrThrow(config)).digestToHex(config, data);
    }

    public String digestToBase64(Digester.Config config, InputStream data) {

        return ((AbstractDigester) getDigesterOrThrow(config)).digestToBase64(config, data);
    }

    // ====

    public byte[] digest(Digester.Config config, String data, Charset charset) {

        return ((AbstractDigester) getDigesterOrThrow(config)).digest(config, data, charset);
    }

    public byte[] digest(Digester.Config config, String data) {

        return ((AbstractDigester) getDigesterOrThrow(config)).digest(config, data);
    }

    public String digestToHex(Digester.Config config, String data, Charset charset) {

        return ((AbstractDigester) getDigesterOrThrow(config)).digestToHex(config, data, charset);
    }

    public String digestToHex(Digester.Config config, String data) {

        return ((AbstractDigester) getDigesterOrThrow(config)).digestToHex(config, data);
    }

    public String digestToBase64(Digester.Config config, String data, Charset charset) {

        return ((AbstractDigester) getDigesterOrThrow(config)).digestToBase64(config, data, charset);
    }

    public String digestToBase64(Digester.Config config, String data) {

        return ((AbstractDigester) getDigesterOrThrow(config)).digestToBase64(config, data);
    }

    // ====

}
