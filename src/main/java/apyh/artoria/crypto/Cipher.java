package apyh.artoria.crypto;

import apyh.artoria.codec.Base64;
import apyh.artoria.codec.Hex;
import apyh.artoria.logging.Logger;
import apyh.artoria.logging.LoggerFactory;
import apyh.artoria.util.Assert;
import apyh.artoria.util.StringUtils;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import static apyh.artoria.util.StringConstant.DEFAULT_CHARSET_NAME;

public abstract class Cipher {

    private static final Logger log = LoggerFactory.getLogger(Cipher.class);

    public enum Mode{
        ENCRYPT(javax.crypto.Cipher.ENCRYPT_MODE),
        DECRYPT(javax.crypto.Cipher.DECRYPT_MODE);

        private int opmode;

        Mode(int opmode) {
            this.opmode = opmode;
        }

        public int getOpmode() {
            return opmode;
        }

    }

    private boolean doFill = false;
    private boolean needIv = false;
    private int multiple = 0;

    protected final boolean getDoFill() {
        return doFill;
    }

    protected final void setDoFill(boolean doFill) {
        this.doFill = doFill;
    }

    protected final boolean getNeedIv() {
        return needIv;
    }

    protected final void setNeedIv(boolean needIv) {
        this.needIv = needIv;
    }

    protected final int getMultiple() {
        return multiple;
    }

    protected final void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    protected void judgeDoFill(String transformation) {
        String lowerCase = transformation.toLowerCase();
        this.setDoFill(lowerCase.contains("nopadding"));
    }

    protected void judgeNeedIv(String transformation) {
        String lowerCase = transformation.toLowerCase();
        this.setNeedIv(lowerCase.contains("cbc"));
    }

    protected byte[] fill(byte[] data, int multiple) {
        Assert.notEmpty(data, "Data must is not empty. ");
        Assert.state(multiple > 0, "Fill length must greater than 0. ");
        int len = data.length;
        int fill = len % multiple;
        fill = fill != 0 ? multiple - fill : 0;
        byte[] result = new byte[len + fill];
        System.arraycopy(data, 0, result, 0, len);
        return result;
    }

    private String charset;
    private String transformation;
    private Mode mode;
    private byte[] key;
    private byte[] iv;
    private Hex hex = Hex.ME;
    private javax.crypto.Cipher cipher;

    protected Cipher() {
    }

    public String getCharset() {
        if (StringUtils.isBlank(charset)) {
            this.setCharset(DEFAULT_CHARSET_NAME);
            log.info("Charset is blank, so set it to " + this.charset + ", and you will not set again. ");
        }
        return charset;
    }

    public void setCharset(String charset) {
        Assert.isBlank(this.charset, "Charset has value, if you want change, please new one. ");
        Assert.notBlank(charset, "Charset must is not blank. ");
        this.charset = charset;
    }

    public String getTransformation() {
        return transformation;
    }

    public Cipher setTransformation(String transformation) {
        Assert.isBlank(this.transformation, "Transformation has value, if you want change, please new one. ");
        this.transformation = transformation;
        this.judgeDoFill(transformation);
        this.judgeNeedIv(transformation);
        return this;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        Assert.notNull(mode, "Mode must is not null. ");
        this.mode = mode;
    }

    public byte[] getKey() {
        return key;
    }

    public Cipher setKey(byte[] key) {
        Assert.isEmpty(this.key, "Key has value, if you want change, please new one. ");
        this.key = key;
        return this;
    }

    public Cipher setKey(String key) {
        this.setKey(key.getBytes(Charset.forName(this.getCharset())));
        return this;
    }

    public byte[] getIv() {
        return iv;
    }

    public Cipher setIv(byte[] iv) {
        Assert.isEmpty(this.iv, "Iv has value, if you want change, please new one. ");
        this.iv = iv;
        return this;
    }

    public Cipher setIv(String iv) {
        this.setIv(iv.getBytes(Charset.forName(this.getCharset())));
        return this;
    }

    public Hex getHex() {
        return hex;
    }

    public void setHex(Hex hex) {
        this.hex = hex;
    }

    private synchronized void init() throws GeneralSecurityException {
        if (cipher != null) { return; }
        Assert.notBlank(transformation, "Transformation must is not blank. ");
        this.assertKey(key);
        this.assertIv(needIv, iv);
        if (doFill) {
            Assert.state(multiple > 0, "When need fill, multiple must greater than 0. ");
        }
        cipher = javax.crypto.Cipher.getInstance(transformation);
        Key key = this.handleKey(this.key);
        int opmode = mode.getOpmode();
        if (needIv) {
            cipher.init(opmode, key, this.handleIv(this.iv));
        }
        else {
            cipher.init(opmode, key);
        }
    }

    protected abstract Key handleKey(byte[] key);

    protected abstract AlgorithmParameterSpec handleIv(byte[] iv);

    protected abstract void assertKey(byte[] key);

    protected abstract void assertIv(boolean needIv, byte[] iv);

    public byte[] calc(byte[] data) throws GeneralSecurityException {
        if (cipher == null) { this.init(); }
        if (mode == Mode.ENCRYPT && doFill) {
            // Transformation is no padding
            // Input length must multiple of [multiple]
            data = this.fill(data, multiple);
        }
        return cipher.doFinal(data);
    }

    public byte[] calc(String data) throws GeneralSecurityException {
        return this.calc(data.getBytes(Charset.forName(this.getCharset())));
    }

    public String calcToString(byte[] data) throws GeneralSecurityException {
        byte[] calc = this.calc(data);
        return new String(calc);
    }

    public String calcToString(String data) throws GeneralSecurityException {
        byte[] calc = this.calc(data);
        return new String(calc);
    }

    public String calcToHexString(byte[] data) throws GeneralSecurityException {
        byte[] calc = this.calc(data);
        return hex.encodeToString(calc);
    }

    public String calcToHexString(String data) throws GeneralSecurityException {
        byte[] calc = this.calc(data);
        return hex.encodeToString(calc);
    }

    public String calcToBase64String(byte[] data) throws GeneralSecurityException {
        return this.calcToBase64String(data, false);
    }

    public String calcToBase64String(String data) throws GeneralSecurityException {
        return this.calcToBase64String(data, false);
    }

    public String calcToBase64String(byte[] data, boolean isUrlSafe) throws GeneralSecurityException {
        byte[] calc = this.calc(data);
        if (isUrlSafe) {
            return Base64.encodeToUrlSafeString(calc);
        }
        else {
            return Base64.encodeToString(calc);
        }
    }

    public String calcToBase64String(String data, boolean isUrlSafe) throws GeneralSecurityException {
        byte[] calc = this.calc(data);
        if (isUrlSafe) {
            return Base64.encodeToUrlSafeString(calc);
        }
        else {
            return Base64.encodeToString(calc);
        }
    }

}
