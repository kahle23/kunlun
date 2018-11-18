package artoria.common;

import artoria.util.PathUtils;

import java.nio.charset.Charset;

/**
 * Common constant.
 * @author Kahle
 */
public class Constants {

    public static final String NEWLINE = System.getProperty("line.separator");
    public static final String CLASSPATH = PathUtils.getClasspath();
    public static final String ROOT_PATH = PathUtils.getRootPath();
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");
    public static final String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();

    public static final String DOT = ".";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String SLASH = "/";
    public static final String EQUAL = "=";
    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String SEMICOLON = ";";
    public static final String UNDERLINE = "_";
    public static final String ASTERISK = "*";
    public static final String AMPERSAND = "&";
    public static final String POUND_MARK = "#";
    public static final String QUOTE_MARK = "\"";
    public static final String DOLLAR_MARK = "$";
    public static final String QUESTION_MARK = "?";
    public static final String BACKLASH = "\\";
    public static final String EMPTY_STRING = "";
    public static final String BLANK_SPACE = " ";
    public static final String LEFT_PARENTHESIS = "(";
    public static final String RIGHT_PARENTHESIS = ")";
    public static final String LEFT_SQUARE_BRACKET = "[";
    public static final String RIGHT_SQUARE_BRACKET = "]";
    public static final String LEFT_CURLY_BRACKET = "{";
    public static final String RIGHT_CURLY_BRACKET = "}";

    public static final String NULL = "null";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String ROOT = "root";
    public static final String GET = "get";
    public static final String SET = "set";
    public static final Integer GET_OR_SET_LENGTH = 3;

    private Constants() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
