package artoria.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

/**
 * Provide a high level abstract digest tools.
 * @author Kahle
 */
public interface Digester {

    /**
     * Get digest algorithm.
     * @return Digest algorithm
     */
    String getAlgorithm();

    /**
     * Set digest algorithm.
     * @param algorithm Digest algorithm
     */
    void setAlgorithm(String algorithm);

    /**
     * Perform digest operations on the data.
     * @param data Data to be digested
     * @return Result after digest
     * @throws GeneralSecurityException Error in digest calculation
     */
    byte[] digest(byte[] data) throws GeneralSecurityException;

    /**
     * Perform digest operations on the input stream.
     * @param inputStream Input stream to be digested
     * @return Result after digest
     * @throws GeneralSecurityException Error in digest calculation
     * @throws IOException Error while io stream operation
     */
    byte[] digest(InputStream inputStream) throws GeneralSecurityException, IOException;

}
