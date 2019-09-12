package artoria.common;

import artoria.file.FilenameUtils;

import java.nio.charset.Charset;

/**
 * Common constant.
 * @author Kahle
 */
public class Constants {

    public static final String NEWLINE = System.getProperty("line.separator");
    public static final String CLASSPATH = FilenameUtils.getClasspath();
    public static final String ROOT_PATH = FilenameUtils.getRootPath();
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");
    public static final String COMPUTER_NAME = System.getenv("COMPUTERNAME");
    public static final String FILLED_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String GBK = "GBK";
    public static final String UTF_8 = "UTF-8";
    public static final String GB2312 = "GB2312";
    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();
    public static final String DEFAULT_ENCODING_NAME = UTF_8;

    public static final String DOT = ".";
    public static final String PLUS = "+";
    public static final String EQUAL = "=";
    public static final String COMMA = ",";
    public static final String MINUS = "-";
    public static final String SLASH = "/";
    public static final String COLON = ":";
    public static final String TILDE = "~";
    public static final String AT_SIGN = "@";
    public static final String ASTERISK = "*";
    public static final String SEMICOLON = ";";
    public static final String UNDERLINE = "_";
    public static final String AMPERSAND = "&";
    public static final String BACKSLASH = "\\";
    public static final String BACK_QUOTE = "`";
    public static final String CARET_SIGN = "^";
    public static final String POUND_SIGN = "#";
    public static final String DOLLAR_SIGN = "$";
    public static final String BLANK_SPACE = " ";
    public static final String EMPTY_STRING = "";
    public static final String PERCENT_SIGN = "%";
    public static final String SINGLE_QUOTE = "\'";
    public static final String DOUBLE_QUOTE = "\"";
    public static final String VERTICAL_BAR = "|";
    public static final String QUESTION_MARK = "?";
    public static final String LESS_THAN_SIGN = "<";
    public static final String EXCLAMATION_MARK = "!";
    public static final String GREATER_THAN_SIGN = ">";
    public static final String DOUBLE_VERTICAL_BAR = "||";
    public static final String LEFT_PARENTHESIS = "(";
    public static final String RIGHT_PARENTHESIS = ")";
    public static final String LEFT_CURLY_BRACKET = "{";
    public static final String RIGHT_CURLY_BRACKET = "}";
    public static final String LEFT_SQUARE_BRACKET = "[";
    public static final String RIGHT_SQUARE_BRACKET = "]";

    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;
    public static final int TWENTY = 20;
    public static final int THIRTY = 30;
    public static final int FORTY = 40;
    public static final int FIFTY = 50;
    public static final int SIXTY = 60;
    public static final int SEVENTY = 70;
    public static final int EIGHTY = 80;
    public static final int NINETY = 90;

    public static final String ON = "on";
    public static final String NO = "no";
    public static final String YES = "yes";
    public static final String OFF = "off";
    public static final String GET = "get";
    public static final String SET = "set";
    public static final String API = "api";
    public static final String PAGE = "page";
    public static final String ROOT = "root";
    public static final String NULL = "null";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String ERROR = "error";
    public static final String SYSTEM = "system";
    public static final String SUCCESS = "success";
    public static final String DEFAULT = "default";
    public static final String UNKNOWN = "unknown";

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

    private Constants() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
