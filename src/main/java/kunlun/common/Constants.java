/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.common;

import kunlun.io.util.FilenameUtils;
import kunlun.net.NetUtils;

import java.nio.charset.Charset;

/**
 * The common constants.
 * @see <a href="https://en.wikipedia.org/wiki/Carriage_return">Carriage return</a>
 * @see <a href="https://en.wikipedia.org/wiki/Newline#Escape_sequences">Line feed</a>
 * @author Kahle
 */
@Deprecated
public class Constants {
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String NEWLINE = System.getProperty("line.separator");

    public static final String COMPUTER_NAME = System.getenv("ComputerName");
    public static final String HOST_NAME = NetUtils.getHostName();
    public static final String ROOT_PATH = FilenameUtils.getRootPath();
    public static final String CLASSPATH = FilenameUtils.getClasspath();

    public static final String NORM_DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    @Deprecated
    public static final String DEFAULT_DATETIME_PATTERN = NORM_DATETIME_MS_PATTERN;
    public static final String UTC_SIMPLE_MS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String UTC_MS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    @Deprecated
    public static final String FULL_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final Object NULL_OBJ = null;
    public static final String NULL_STR = null;

    public static final String GBK = "GBK";
    public static final String UTF_8 = "UTF-8";
    public static final String GB2312 = "GB2312";
    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String DEFAULT_ENCODING_NAME = UTF_8;
    public static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();

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
    public static final String ERROR = "error";
    public static final String SYSTEM = "system";
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String DEFAULT = "default";
    public static final String UNKNOWN = "unknown";
    public static final String ONE_STR = "1";
    public static final String ZERO_STR = "0";
    public static final String TRUE_STR = "true";
    public static final String FALSE_STR = "false";

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

//    /** @see artoria.common.constant.Symbols */ @Deprecated
    public static final String DOT = ".";
//    /** @see artoria.common.constant.Symbols */ @Deprecated
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String EQUAL = "=";
    public static final String COMMA = ",";
    public static final String SLASH = "/";
    public static final String COLON = ":";
    public static final String TILDE = "~";
    public static final String AT_SIGN = "@";
    public static final String ASTERISK = "*";
    public static final String SEMICOLON = ";";
    public static final String UNDERLINE = "_";
    public static final String AMPERSAND = "&";
    public static final String LINE_FEED = "\n";
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
    public static final String CARRIAGE_RETURN = "\r";
    public static final String EXCLAMATION_MARK = "!";
    public static final String GREATER_THAN_SIGN = ">";
    public static final String DOUBLE_VERTICAL_BAR = "||";
    public static final String LEFT_PARENTHESIS = "(";
    public static final String RIGHT_PARENTHESIS = ")";
    public static final String LEFT_CURLY_BRACKET = "{";
    public static final String RIGHT_CURLY_BRACKET = "}";
    public static final String LEFT_SQUARE_BRACKET = "[";
    public static final String RIGHT_SQUARE_BRACKET = "]";
    public static final String DOUBLE_PLUS = "++";
    public static final String DOUBLE_MINUS = "--";
    public static final String DOUBLE_EQUAL = "==";
    public static final String DOUBLE_SLASH = "//";

//    /** @see artoria.common.constant.Numbers */ @Deprecated
    public static final int MINUS_NINETY = -90;
//    /** @see artoria.common.constant.Numbers */ @Deprecated
    public static final int MINUS_EIGHTY = -80;
    public static final int MINUS_SEVENTY = -70;
    public static final int MINUS_SIXTY = -60;
    public static final int MINUS_FIFTY = -50;
    public static final int MINUS_FORTY = -40;
    public static final int MINUS_THIRTY = -30;
    public static final int MINUS_TWENTY = -20;
    public static final int MINUS_NINETEEN = -19;
    public static final int MINUS_EIGHTEEN = -18;
    public static final int MINUS_SEVENTEEN = -17;
    public static final int MINUS_SIXTEEN = -16;
    public static final int MINUS_FIFTEEN = -15;
    public static final int MINUS_FOURTEEN = -14;
    public static final int MINUS_THIRTEEN = -13;
    public static final int MINUS_TWELVE = -12;
    public static final int MINUS_ELEVEN = -11;
    public static final int MINUS_TEN = -10;
    public static final int MINUS_NINE = -9;
    public static final int MINUS_EIGHT = -8;
    public static final int MINUS_SEVEN = -7;
    public static final int MINUS_SIX = -6;
    public static final int MINUS_FIVE = -5;
    public static final int MINUS_FOUR = -4;
    public static final int MINUS_THREE = -3;
    public static final int MINUS_TWO = -2;
    public static final int MINUS_ONE = -1;
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
    public static final int ELEVEN = 11;
    public static final int TWELVE = 12;
    public static final int THIRTEEN = 13;
    public static final int FOURTEEN = 14;
    public static final int FIFTEEN = 15;
    public static final int SIXTEEN = 16;
    public static final int SEVENTEEN = 17;
    public static final int EIGHTEEN = 18;
    public static final int NINETEEN = 19;
    public static final int TWENTY = 20;
    public static final int TWENTY_ONE = 21;
    public static final int TWENTY_TWO = 22;
    public static final int TWENTY_THREE = 23;
    public static final int TWENTY_FOUR = 24;
    public static final int TWENTY_FIVE = 25;
    public static final int TWENTY_SIX = 26;
    public static final int THIRTY = 30;
    public static final int THIRTY_ONE = 31;
    public static final int THIRTY_TWO = 32;
    public static final int THIRTY_THREE = 33;
    public static final int THIRTY_FIVE = 35;
    public static final int THIRTY_SIX = 36;
    public static final int THIRTY_SEVEN = 37;
    public static final int THIRTY_NINE = 39;
    public static final int FORTY = 40;
    public static final int FIFTY = 50;
    public static final int FIFTY_ONE = 51;
    public static final int FIFTY_EIGHT = 58;
    public static final int FIFTY_NINE = 59;
    public static final int SIXTY = 60;
    public static final int SIXTY_ONE = 61;
    public static final int SIXTY_TWO = 62;
    public static final int SIXTY_THREE = 63;
    public static final int SIXTY_FOUR = 64;
    public static final int SIXTY_FIVE = 65;
    public static final int SIXTY_SIX = 66;
    public static final int SIXTY_EIGHT = 68;
    public static final int SIXTY_NINE = 69;
    public static final int SEVENTY = 70;
    public static final int SEVENTY_TWO = 72;
    public static final int SEVENTY_SEVEN = 77;
    public static final int EIGHTY = 80;
    public static final int EIGHTY_ONE = 81;
    public static final int EIGHTY_TWO = 82;
    public static final int EIGHTY_SEVEN = 87;
    public static final int EIGHTY_EIGHT = 88;
    public static final int EIGHTY_NINE = 89;
    public static final int NINETY = 90;
    public static final int NINETY_NINE = 99;
    public static final int ONE_HUNDRED = 100;
    public static final int TWO_HUNDRED = 200;
    public static final int THREE_HUNDRED = 300;
    public static final int FOUR_HUNDRED = 400;
    public static final int FIVE_HUNDRED = 500;
    public static final int SIX_HUNDRED = 600;
    public static final int SEVEN_HUNDRED = 700;
    public static final int EIGHT_HUNDRED = 800;
    public static final int NINE_HUNDRED = 900;
    public static final int ONE_THOUSAND = 1000;
    public static final int TWO_THOUSAND = 2000;
    public static final int THREE_THOUSAND = 3000;
    public static final int FOUR_THOUSAND = 4000;
    public static final int FIVE_THOUSAND = 5000;
    public static final int SIX_THOUSAND = 6000;
    public static final int SEVEN_THOUSAND = 7000;
    public static final int EIGHT_THOUSAND = 8000;
    public static final int NINE_THOUSAND = 9000;
    public static final int TEN_THOUSAND = 10000;
    public static final int NINE_HUNDRED_NINETY_NINE = 999;

    private Constants() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
