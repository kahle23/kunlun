/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto.util;

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
