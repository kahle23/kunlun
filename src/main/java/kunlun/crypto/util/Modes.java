/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.util;

/**
 * The cipher algorithm modes.
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher">Cipher (Encryption) Algorithms</a>
 * @author Kahle
 */
public class Modes {

    /**
     * No mode.
     */
    public static final String NONE = "NONE";

    /**
     * Cipher Block Chaining.
     */
    public static final String CBC = "CBC";

    /**
     * Cipher Feedback.
     */
    public static final String CFB = "CFB";

    /**
     * A simplification of OFB.
     */
    public static final String CTR = "CTR";

    /**
     * Cipher Text Stealing.
     */
    public static final String CTS = "CTS";

    /**
     * Electronic code book.
     */
    public static final String ECB = "ECB";

    /**
     * Output Feedback.
     */
    public static final String OFB = "OFB";

    /**
     * Propagating Cipher Block Chaining.
     */
    public static final String PCBC = "PCBC";

    private Modes() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
