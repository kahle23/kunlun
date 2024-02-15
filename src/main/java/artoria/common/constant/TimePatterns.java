package artoria.common.constant;

/**
 * The common time pattern constants.
 * @author Kahle
 */
public class TimePatterns {

    public static final String Y4MD2MI = "yyyy-MM-dd";
    public static final String Y4MD2SL = "yyyy/MM/dd";
    public static final String Y4MD2PU = "yyyyMMdd";
    public static final String HMS2CO  = "HH:mm:ss";
    public static final String HMS2PU  = "HHmmss";
    public static final String HM2CO   = "HH:mm";
    public static final String HM2PU   = "HHmm";
    public static final String Y4MD2MI_HMS2CO_S3 = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String Y4MD2SL_HMS2CO_S3 = "yyyy/MM/dd HH:mm:ss SSS";
    public static final String Y4MD2MI_HMS2CO    = "yyyy-MM-dd HH:mm:ss";
    public static final String Y4MD2SL_HMS2CO    = "yyyy/MM/dd HH:mm:ss";
    public static final String Y4MD2MI_HM2CO     = "yyyy-MM-dd HH:mm";
    public static final String Y4MD2SL_HM2CO     = "yyyy/MM/dd HH:mm";

    public static final String NORM_DATETIME    = Y4MD2MI_HMS2CO;
    public static final String NORM_DATETIME_MS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PURE_DATETIME    = "yyyyMMddHHmmss";
    public static final String PURE_DATETIME_MS = "yyyyMMddHHmmssSSS";
    public static final String UTC        = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String UTC_NOZ    = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String UTC_MS     = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String UTC_MS_NOZ = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static final String DEFAULT_DATETIME = Y4MD2MI_HMS2CO_S3;

    private TimePatterns() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
