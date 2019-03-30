package artoria.crypto;

import java.security.Provider;
import java.security.Security;

public abstract class BouncyCastleSupport {

    static {
        Provider provider = null;
        try {
            provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
        }
        catch (NoClassDefFoundError e) {
            // Ignore.
        }
        if (provider != null) {
            Security.addProvider(provider);
        }
    }

}
