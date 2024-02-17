/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common.constant;

/**
 * The common algorithms constants.
 * @author Kahle
 */
public class Algorithms {

    public static final String MD2 = "MD2";
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA384 = "SHA-384";
    public static final String SHA512 = "SHA-512";
    public static final String HMAC_MD5 = "HmacMD5";
    public static final String HMAC_SHA1 = "HmacSHA1";
    public static final String HMAC_SHA256 = "HmacSHA256";
    public static final String HMAC_SHA384 = "HmacSHA384";
    public static final String HMAC_SHA512 = "HmacSHA512";
    public static final String AES = "AES";
    public static final String CCM = "CCM";
    public static final String DES = "DES";
    public static final String GCM = "GCM";
    public static final String PBE = "PBE";
    public static final String RC2 = "RC2";
    public static final String RC4 = "RC4";
    public static final String RC5 = "RC5";
    public static final String ECIES = "ECIES";
    public static final String DESEDE = "DESede";
    public static final String ARCFOUR = "ARCFOUR";
    public static final String BLOWFISH = "Blowfish";
    public static final String EC = "EC";
    public static final String DSA = "DSA";
    public static final String RSA = "RSA";
    public static final String DIFFIE_HELLMAN = "DiffieHellman";

    private Algorithms() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }
}
