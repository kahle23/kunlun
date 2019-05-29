package artoria.crypto;

import artoria.util.Assert;

import javax.crypto.Cipher;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;

import static artoria.common.Constants.SLASH;

/**
 * Abstract implementation of encryption and decryption tools.
 * @see artoria.crypto.Crypto
 * @author Kahle
 */
public abstract class AbstractCrypto implements Crypto {
    private static final SecureRandom RANDOM = new SecureRandom();
    private AlgorithmParameterSpec algorithmParameterSpec;
    private AlgorithmParameters algorithmParameters;
    private SecureRandom secureRandom = RANDOM;
    private String algorithm;
    private String mode;
    private String padding;

    @Override
    public String getAlgorithm() {

        return this.algorithm;
    }

    @Override
    public void setAlgorithm(String algorithm) {
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        this.algorithm = algorithm;
    }

    @Override
    public String getMode() {

        return this.mode;
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

        return this.padding;
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

        return this.secureRandom;
    }

    @Override
    public void setSecureRandom(SecureRandom secureRandom) {
        Assert.notNull(secureRandom, "Parameter \"secureRandom\" must not null. ");
        this.secureRandom = secureRandom;
    }

    @Override
    public AlgorithmParameters getAlgorithmParameters() {

        return this.algorithmParameters;
    }

    @Override
    public void setAlgorithmParameters(AlgorithmParameters algorithmParameters) {

        this.algorithmParameters = algorithmParameters;
    }

    @Override
    public AlgorithmParameterSpec getAlgorithmParameterSpec() {

        return this.algorithmParameterSpec;
    }

    @Override
    public void setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameterSpec) {

        this.algorithmParameterSpec = algorithmParameterSpec;
    }

    protected String getTransformation() {
        String algorithm = this.getAlgorithm();
        Assert.notBlank(algorithm, "Parameter \"algorithm\" must not blank. ");
        String mode = this.getMode();
        Assert.notBlank(mode, "Parameter \"mode\" must not blank. ");
        String padding = this.getPadding();
        Assert.notBlank(padding, "Parameter \"padding\" must not blank. ");
        return algorithm + SLASH + mode + SLASH + padding;
    }

    /**
     *
     * @param cipherMode
     * @param key
     * @param certificate
     * @return
     * @throws GeneralSecurityException
     * @see Cipher#ENCRYPT_MODE
     * @see Cipher#DECRYPT_MODE
     */
    protected Cipher createCipher(int cipherMode, Key key, Certificate certificate) throws GeneralSecurityException {
        AlgorithmParameterSpec parameterSpec = this.getAlgorithmParameterSpec();
        AlgorithmParameters parameters = this.getAlgorithmParameters();
        SecureRandom random = this.getSecureRandom();
        String transformation = this.getTransformation();
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
