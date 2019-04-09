package artoria.crypto;

/**
 * Cipher Algorithm Padding.
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher">Cipher (Encryption) Algorithms</a>
 * @author Kahle
 */
public enum Padding {

    /**
     * No padding.
     */
    NO_PADDING("NoPadding"),

    /**
     * Zero padding mean when the bits in the block is insufficient, the block will be supplemented by zero.
     * In this case, "ZeroPadding" is not supported
     *      , while "NoPadding" requires manual complement, so "NoPadding" is equivalent to "ZeroPadding".
     */
    ZERO_PADDING("ZeroPadding"),

    /**
     * This padding for block ciphers is described
     *      in 5.2 Block Encryption Algorithms
     *      in the W3C's "XML Encryption Syntax and Processing" document.
     */
    ISO10126_PADDING("ISO10126Padding"),

    /**
     * Optimal Asymmetric Encryption Padding scheme defined in PKCS1,
     *      where &lt;digest&gt; should be replaced by the message digest
     *      and &lt;mgf&gt; by the mask generation function.
     */
    OAEP_PADDING("OAEPPadding"),

    /**
     * The padding scheme described in PKCS #1, used with the RSA algorithm.
     */
    PKCS1_PADDING("PKCS1Padding"),

    /**
     * The padding scheme described in RSA Laboratories,
     *      "PKCS #5: Password-Based Encryption Standard," version 1.5, November 1993.
     */
    PKCS5_PADDING("PKCS5Padding"),

    /**
     * This makes the padding scheme similar (but not quite) to PKCS5Padding,
     *      where the padding length is encoded in the padding (and ranges from 1 to block_length).
     *      With the SSL scheme, the sizeof(padding) is encoded
     *      in the always present padding_length and therefore ranges from 0 to block_length-1.
     */
    SSL3_PADDING("SSL3Padding"),

    ;

    private String name;

    Padding(String name) {

        this.name = name;
    }

    public String getName() {

        return this.name;
    }

    @Override
    public String toString() {

        return this.name;
    }

}
