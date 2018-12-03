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

    public static final String GET = "get";
    public static final String SET = "set";
    public static final String ROOT = "root";
    public static final String NULL = "null";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    private Constants() {

        throw new UnsupportedOperationException("Don't allow instantiation. ");
    }

}
