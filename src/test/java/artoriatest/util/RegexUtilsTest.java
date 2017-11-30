package artoriatest.util;

import org.junit.Test;
import artoria.util.Http;
import artoria.util.RegexUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtilsTest {

    @Test
    public void test0() throws Exception {
        String s1 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw==";
        String s2 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw==";
        String s3 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw=";
        String s4 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw=";
        String s5 = "bVITZQ+c4Ao2bvTFxtG/lEFTIw";
        String s6 = "bVITZQ-c4Ao2bvTFxtG_lEFTIw";
        String regex = "^[a-zA-Z0-9+/]+={0,2}$";
        String regex1 = "^[a-zA-Z0-9-_]+={0,2}$";
        System.out.println(RegexUtils.matches(regex, s1));
        System.out.println(RegexUtils.matches(regex1, s2));
        System.out.println(RegexUtils.matches(regex, s3));
        System.out.println(RegexUtils.matches(regex1, s4));
        System.out.println(RegexUtils.matches(regex, s5));
        System.out.println(RegexUtils.matches(regex1, s6));
    }

    @Test
    public void test1() {
        String data = "<html><header>Hello</header><body><h1><a href='http://uux.me'>aaaa</a></h1><br /><a href='https://baidu.com'>bbb</a></body></html>";
        Pattern datePattern = Pattern.compile("(?i)<a(.|\\n)*?</a>");
        Matcher dateMatcher = datePattern.matcher(data);
        while(dateMatcher.find()) {
            System.out.println(dateMatcher.group());
        }
    }

    @Test
    public void test2() {
        String data = "<html><header>Hello</header><body><h1><A" +
                " target='_blank' href=\n'http://uux.me'>\naaaa\n</a>\n</h1><br /><a\n href='https://baidu.com'>bbb</a></body></html>";
        System.out.println(RegexUtils.findAll("(?i)<a(.|\\n)*?</a>", data).toString());
    }

    @Test
    public void test3() {
        String data = "<html><header>Hello</header><body><h1><A" +
                " target='_blank' href=\n'http://uux.me'>\naaaa\n</a>\n</h1><br /><a\n href='https://baidu.com'>bbb</a></body></html>";
        System.out.println(RegexUtils.findAllALabel(data).toString());
    }

    @Test
    public void test4() {
        String data = "a<html><header>Hello</header><body><h1><img src=\n" +
                "'http://uux.me' />\n" +
                "aaaa\n" +
                "</h1><br /><img\n" +
                " src='https://baidu.com' ></body></html>a\n" +
                "<img src='https://baidu.com/aaaa' /></body></html>a";
        System.out.println(RegexUtils.findAllImgLabel(data).toString());
        System.out.println(RegexUtils.findFirst("(?i)<img\\s+(.|\\n)*?/?>", data));
    }

    @Test
    public void test5() throws Exception {
        List<String> list = RegexUtils.findAllALabel(Http.on("http://uux.me").get());
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void test6() throws Exception {
        List<String> list = RegexUtils.findAllImgLabel(Http.on("http://uux.me/2").get());
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void test7() throws Exception {
        String data = "hello, world! hello, world! hello, world! ";
        Pattern datePattern = Pattern.compile("e");
        System.out.println(Arrays.toString(datePattern.split(data)));
        Matcher dateMatcher = datePattern.matcher(data);
        System.out.println(dateMatcher.replaceFirst("!!"));
        System.out.println(dateMatcher.replaceAll("!!"));
    }

}
