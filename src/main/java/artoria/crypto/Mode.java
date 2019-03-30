package artoria.crypto;

/**
 * Cipher Algorithm Modes.
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Cipher">Cipher (Encryption) Algorithms</a>
 * @author Kahle
 */
public enum Mode {

    /**
     * No mode.
     */
    NONE("NONE"),

    /**
     * Cipher Block Chaining.
     */
    CBC("CBC"),

    /**
     * Cipher Feedback.
     */
    CFB("CFB"),

    /**
     * A simplification of OFB.
     */
    CTR("CTR"),

    /**
     * Cipher Text Stealing.
     */
    CTS("CTS"),

    /**
     * Electronic code book.
     */
    ECB("ECB"),

    /**
     * Output Feedback.
     */
    OFB("OFB"),

    /**
     * Propagating Cipher Block Chaining.
     */
    PCBC("PCBC"),

    ;

    private String name;

    Mode(String name) {

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
