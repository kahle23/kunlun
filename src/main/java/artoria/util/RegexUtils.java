package artoria.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kahle
 */
public class RegexUtils {

    public static boolean matches(Pattern pattern, CharSequence input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static boolean matches(String regex, CharSequence input) {
        Pattern pattern = Pattern.compile(regex);
        return matches(pattern, input);
    }

    public static String findFirst(Pattern pattern, CharSequence input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.find() ? matcher.group() : null;
    }

    public static String findFirst(String regex, CharSequence input) {
        Pattern pattern = Pattern.compile(regex);
        return findFirst(pattern, input);
    }

    public static List<String> findAll(Pattern pattern, CharSequence input) {
        List<String> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    public static List<String> findAll(String regex, CharSequence input) {
        Pattern pattern = Pattern.compile(regex);
        return findAll(pattern, input);
    }

    public static List<String> split(Pattern pattern, CharSequence input) {
        List<String> result = new ArrayList<>();
        Collections.addAll(result, pattern.split(input));
        return result;
    }

    public static List<String> split(String regex, CharSequence input) {
        Pattern pattern = Pattern.compile(regex);
        return split(pattern, input);
    }

    public static String replaceFirst(Pattern pattern, CharSequence input, String replacement) {
        return pattern.matcher(input).replaceFirst(replacement);
    }

    public static String replaceFirst(String regex, CharSequence input, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        return replaceFirst(pattern, input, replacement);
    }

    public static String replaceAll(Pattern pattern, CharSequence input, String replacement) {
        return pattern.matcher(input).replaceAll(replacement);
    }

    public static String replaceAll(String regex, CharSequence input, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        return replaceAll(pattern, input, replacement);
    }


    private static final Pattern CHINESE_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5]+$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?\\d+\\.?\\d*$");
    private static final Pattern FLOAT_PATTERN = Pattern.compile("^-?[1-9]\\d*\\.?\\d*|-?0\\.?\\d*[1-9]\\d*$");
    private static final Pattern INTEGER_PATTERN = Pattern.compile("^-?[1-9]\\d*|0$");
    private static final Pattern QQ_PATTERN = Pattern.compile("^[1-9][0-9]{4,12}$");
    private static final Pattern WECHAT_PATTERN = Pattern.compile("^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
    private static final Pattern URL_PATTERN = Pattern.compile("^[a-zA-Z]+://[^\\s]*$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\s*?(.+)@(.+?)\\s*$");
    // private static final Pattern EMAIL_PATTERN = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");

    private static final Pattern A_LABEL_PATTERN = Pattern.compile("(?i)<a\\s+(.|\\n)*?</a>");
    private static final Pattern IMG_LABEL_PATTERN = Pattern.compile("(?i)<img\\s+(.|\\n)*?/?>");

    public static boolean isChinese(String chinese) {
        return matches(CHINESE_PATTERN, chinese);
    }

    public static boolean isNotChinese(String chinese) {
        return !matches(CHINESE_PATTERN, chinese);
    }

    public static boolean isNumber(String number) {
        return matches(NUMBER_PATTERN, number);
    }

    public static boolean isNotNumber(String number) {
        return !matches(NUMBER_PATTERN, number);
    }

    public static boolean isFloat(String floatNum) {
        return matches(FLOAT_PATTERN, floatNum);
    }

    public static boolean isNotFloat(String floatNum) {
        return !matches(FLOAT_PATTERN, floatNum);
    }

    public static boolean isInteger(String integer) {
        return matches(INTEGER_PATTERN, integer);
    }

    public static boolean isNotInteger(String integer) {
        return !matches(INTEGER_PATTERN, integer);
    }

    public static boolean isQq(String qq) {
        return matches(QQ_PATTERN, qq);
    }

    public static boolean isNotQq(String qq) {
        return !matches(QQ_PATTERN, qq);
    }

    public static boolean isWeChat(String weChat) {
        return matches(WECHAT_PATTERN, weChat);
    }

    public static boolean isNotWeChat(String weChat) {
        return !matches(WECHAT_PATTERN, weChat);
    }

    public static boolean isPhone(String phone) {
        return matches(PHONE_PATTERN, phone);
    }

    public static boolean isNotPhone(String phone) {
        return !matches(PHONE_PATTERN, phone);
    }

    public static boolean isUrl(String url) {
        return matches(URL_PATTERN, url);
    }

    public static boolean isNotUrl(String url) {
        return !matches(URL_PATTERN, url);
    }

    public static boolean isEmail(String email) {
        return matches(EMAIL_PATTERN, email);
    }

    public static boolean isNotEmail(String email) {
        return !matches(EMAIL_PATTERN, email);
    }

    public static List<String> findAllALabel(String html) {
        return findAll(A_LABEL_PATTERN, html);
    }

    public static List<String> findAllImgLabel(String html) {
        return findAll(IMG_LABEL_PATTERN, html);
    }

}
