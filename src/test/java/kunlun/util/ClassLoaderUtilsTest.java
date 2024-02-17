/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.io.util.IOUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ClassLoaderUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(ClassLoaderUtilsTest.class);

    @Test
    public void testGetResource() throws IOException {
        URL resource = ClassLoaderUtils.getResource("LICENSE.txt", getClass());
        log.info("url: {}", resource);
        assertNotNull(resource);
    }

    @Test
    public void testGetResources() throws IOException {
        List<URL> urlList = ClassLoaderUtils
                .getResources("LICENSE.txt", getClass());
        for (URL url : urlList) {
            log.info("url: {}", url);
            assertNotNull(url);
        }
    }

    @Test
    public void testGetResourceAsStream() throws IOException {
        InputStream in = ClassLoaderUtils
                .getResourceAsStream("jdbc.properties", getClass());
        assertNotNull(in);
        log.info(IOUtils.toString(in));
    }

    @Test
    public void testLoadClass() throws ClassNotFoundException {
        Class<?> loadClass = ClassLoaderUtils
                .loadClass("kunlun.util.CollectionUtils", getClass());
        log.info("loadClass: {}", loadClass);
        assertNotNull(loadClass);
    }

}
