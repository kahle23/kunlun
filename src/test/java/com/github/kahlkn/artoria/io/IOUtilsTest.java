package com.github.kahlkn.artoria.io;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class IOUtilsTest {

    @Test
    public void test1() throws IOException {
        InputStream in = IOUtils.findJarClasspath("logging.properties");
        System.out.println(IOUtils.toString(in));
    }

}
