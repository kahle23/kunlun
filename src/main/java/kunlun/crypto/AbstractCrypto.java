/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto;

import kunlun.util.Assert;

import javax.crypto.Cipher;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;

import static kunlun.common.constant.Symbols.SLASH;

/**
 * Abstract implementation of encryption and decryption tools.
 * @see kunlun.crypto.Crypto
 * @author Kahle
 */
public abstract class AbstractCrypto implements Crypto {
    private AlgorithmParameterSpec algorithmParameterSpec;
    private AlgorithmParameters algorithmParameters;
    private SecureRandom secureRandom = new SecureRandom();
    private String algorithm;
    private String mode;
    private String padding;

    @Override
    public String getAlgorithm() {

        return algorithm;
    }

    @Override
    public void setAlgorithm(String algorithm) {
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        this.algorithm = algorithm;
    }

    @Override
    public String getMode() {

        return mode;
    }

    @Override
    public void setMode(String mode) {
        Assert.notBlank(mode, "Parameter \"mode\" must not blank. ");
        this.mode = mode;
    }

    @Override
    public void setMode(Mode mode) {
        Assert.notNull(mode, "Parameter \"mode\" must not null. ");
        this.mode = mode.getName();
    }

    @Override
    public String getPadding() {

        return padding;
    }

    @Override
    public void setPadding(String padding) {
        Assert.notBlank(padding, "Parameter \"padding\" must not blank. ");
        this.padding = padding;
    }

    @Override
    public void setPadding(Padding padding) {
        Assert.notNull(padding, "Parameter \"padding\" must not null. ");
        this.padding = padding.getName();
    }

    @Override
    public SecureRandom getSecureRandom() {

        return secureRandom;
    }

    @Override
    public void setSecureRandom(SecureRandom secureRandom) {
        Assert.notNull(secureRandom, "Parameter \"secureRandom\" must not null. ");
        this.secureRandom = secureRandom;
    }

    @Override
    public AlgorithmParameters getAlgorithmParameters() {

        return algorithmParameters;
    }

    @Override
    public void setAlgorithmParameters(AlgorithmParameters algorithmParameters) {

        this.algorithmParameters = algorithmParameters;
    }

    @Override
    public AlgorithmParameterSpec getAlgorithmParameterSpec() {

        return algorithmParameterSpec;
    }

    @Override
    public void setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameterSpec) {

        this.algorithmParameterSpec = algorithmParameterSpec;
    }

    protected String getTransformation() {
        String algorithm = getAlgorithm();
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        String mode = getMode();
        Assert.notBlank(mode, "Parameter \"mode\" must not blank. ");
        String padding = getPadding();
        Assert.notBlank(padding, "Parameter \"padding\" must not blank. ");
        return algorithm + SLASH + mode + SLASH + padding;
    }

    /**
     * Create "Cipher" object based on existing conditions.
     * @param cipherMode Cipher mode (encrypt or decrypt)
     * @param key The key for encryption or decryption
     * @param certificate Certificate object if any
     * @return The "Cipher" object that was successfully created
     * @throws GeneralSecurityException Some Java encryption and decryption exceptions
     * @see Cipher#ENCRYPT_MODE Encryption mode
     * @see Cipher#DECRYPT_MODE Decryption mode
     */
    protected Cipher createCipher(int cipherMode, Key key, Certificate certificate) throws GeneralSecurityException {
        AlgorithmParameterSpec parameterSpec = getAlgorithmParameterSpec();
        AlgorithmParameters parameters = getAlgorithmParameters();
        SecureRandom random = getSecureRandom();
        String transformation = getTransformation();
        Cipher cipher = Cipher.getInstance(transformation);
        if (key != null) {
            if (parameterSpec != null) {
                cipher.init(cipherMode, key, parameterSpec, random);
            }
            else if (parameters != null) {
                cipher.init(cipherMode, key, parameters, random);
            }
            else {
                cipher.init(cipherMode, key, random);
            }
            return cipher;
        }
        if (certificate != null) {
            cipher.init(cipherMode, certificate, random);
            return cipher;
        }
        throw new IllegalArgumentException("Invalid parameter while creating Cipher. ");
    }

}
