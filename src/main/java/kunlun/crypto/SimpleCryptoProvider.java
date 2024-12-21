/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.crypto;

import kunlun.core.Cipher;
import kunlun.core.Digester;

import java.util.Map;

/**
 * The simple crypto tools provider.
 * @author Kahle
 */
public class SimpleCryptoProvider extends AbstractCryptoProvider {

    protected SimpleCryptoProvider(Map<String, Cipher> ciphers,
                                   Map<String, Digester> digesters) {

        super(ciphers, digesters);
    }

    public SimpleCryptoProvider() {

    }

}
