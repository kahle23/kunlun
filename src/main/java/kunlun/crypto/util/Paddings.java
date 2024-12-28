/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.util;

/**
 * The cipher algorithm padding.
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher">Cipher (Encryption) Algorithms</a>
 * @author Kahle
 */
public class Paddings {

    /**
     * No padding.
     */
    public static final String NO_PADDING = "NoPadding";

    /**
     * Zero padding mean when the bits in the block is insufficient,
     *      the block will be supplemented by zero.
     */
    public static final String ZERO_PADDING = "ZeroPadding";

    /**
     * This padding for block ciphers is described
     *      in 5.2 Block Encryption Algorithms
     *      in the W3C's "XML Encryption Syntax and Processing" document.
     */
    public static final String ISO10126_PADDING = "ISO10126Padding";

    /**
     * Optimal Asymmetric Encryption Padding scheme defined in PKCS1,
     *      where &lt;digest&gt; should be replaced by the message digest
     *      and &lt;mgf&gt; by the mask generation function.
     */
    public static final String OAEP_PADDING = "OAEPPadding";

    /**
     * The padding scheme described in PKCS #1, used with the RSA algorithm.
     */
    public static final String PKCS1_PADDING = "PKCS1Padding";

    /**
     * The padding scheme described in RSA Laboratories,
     *      "PKCS #5: Password-Based Encryption Standard," version 1.5, November 1993.
     */
    public static final String PKCS5_PADDING = "PKCS5Padding";

    /**
     * The padding scheme described in PKCS #7 .
     */
    public static final String PKCS7_PADDING = "PKCS7Padding";

    /**
     * This makes the padding scheme similar (but not quite) to PKCS5Padding,
     *      where the padding length is encoded in the padding (and ranges from 1 to block_length).
     *      With the SSL scheme, the sizeof(padding) is encoded
     *      in the always present padding_length and therefore ranges from 0 to block_length-1.
     */
    public static final String SSL3_PADDING = "SSL3Padding";

    private Paddings() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
