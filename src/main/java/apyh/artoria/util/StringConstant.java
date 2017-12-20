package apyh.artoria.util;

import java.nio.charset.Charset;

/**
 * String constant.
 * @author Kahle
 */
public class StringConstant {

    public static final String ENDL = System.getProperty("line.separator");
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
    public static final String COLON = ":";
    public static final String COMMA = ",";
    public static final String UNDERLINE = "_";
    public static final String AMPERSAND = "&";
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

    public static final String STRING_GET = "get";
    public static final String STRING_SET = "set";
    public static final String STRING_NULL = "null";
    public static final String STRING_TRUE = "true";
    public static final String STRING_FALSE = "false";

}
