/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto;

import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Provide a high level abstract encryption and decryption tools.
 * @author Kahle
 */
public interface Crypto {

    /**
     * Get encryption and decryption algorithm.
     * @return Encryption and decryption algorithm
     */
    String getAlgorithm();

    /**
     * Set encryption and decryption algorithm.
     * @param algorithm Encryption and decryption algorithm
     */
    void setAlgorithm(String algorithm);

    /**
     * Get mode.
     * @return Mode
     */
    String getMode();

    /**
     * Set mode.
     * @param mode Mode
     */
    void setMode(String mode);

    /**
     * Set mode.
     * @param mode An enumeration object for mode
     */
    void setMode(Mode mode);

    /**
     * Get padding mode.
     * @return Padding mode
     */
    String getPadding();

    /**
     * Set padding mode.
     * @param padding Padding mode
     */
    void setPadding(String padding);

    /**
     * Set padding mode.
     * @param padding An enumeration object for padding mode
     */
    void setPadding(Padding padding);

    /**
     * Get SecureRandom.
     * @return SecureRandom
     */
    SecureRandom getSecureRandom();

    /**
     * Set SecureRandom.
     * @param secureRandom SecureRandom
     */
    void setSecureRandom(SecureRandom secureRandom);

    /**
     * Get AlgorithmParameters.
     * @return AlgorithmParameters
     */
    AlgorithmParameters getAlgorithmParameters();

    /**
     * Set AlgorithmParameters.
     * @param algorithmParameters AlgorithmParameters
     */
    void setAlgorithmParameters(AlgorithmParameters algorithmParameters);

    /**
     * Get AlgorithmParameterSpec.
     * @return AlgorithmParameterSpec
     */
    AlgorithmParameterSpec getAlgorithmParameterSpec();

    /**
     * Set AlgorithmParameterSpec.
     * @param algorithmParameterSpec AlgorithmParameterSpec
     */
    void setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameterSpec);

}
