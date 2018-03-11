package com.github.kahlkn.artoria.util;

import java.nio.charset.Charset;

/**
 * Const.
 * @author Kahle
 */
public interface Const {

    Integer GET_OR_SET_LENGTH = 3;

    String ENDL = System.getProperty("line.separator");
    String CLASSPATH = PathUtils.getClasspath();
    String ROOT_PATH = PathUtils.getRootPath();
    String FILE_SEPARATOR = System.getProperty("file.separator");
    String PATH_SEPARATOR = System.getProperty("path.separator");
    String DEFAULT_CHARSET_NAME = Charset.defaultCharset().name();

    String DOT = ".";
    String PLUS = "+";
    String MINUS = "-";
    String SLASH = "/";
    String EQUAL = "=";
    String COLON = ":";
    String COLON_SPACE = ": ";
    String COMMA = ",";
    String COMMA_SPACE = ", ";
    String UNDERLINE = "_";
    String AMPERSAND = "&";
    String QUESTION_MARK = "?";
    String BACKLASH = "\\";
    String EMPTY_STRING = "";
    String BLANK_SPACE = " ";
    String LEFT_PARENTHESIS = "(";
    String RIGHT_PARENTHESIS = ")";
    String LEFT_SQUARE_BRACKET = "[";
    String RIGHT_SQUARE_BRACKET = "]";
    String LEFT_CURLY_BRACKET = "{";
    String RIGHT_CURLY_BRACKET = "}";

    String GET = "get";
    String SET = "set";
    String NULL = "null";
    String TRUE = "true";
    String FALSE = "false";
    String ROOT = "root";

}
