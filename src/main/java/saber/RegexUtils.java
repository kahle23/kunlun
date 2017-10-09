package saber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexUtils {

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

}
