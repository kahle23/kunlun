/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.io.util.IoUtil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ClassLoaderUtilTest {
    private static final Logger log = LoggerFactory.getLogger(ClassLoaderUtilTest.class);

    @Test
    public void testGetResource() throws IOException {
        URL resource = ClassLoaderUtil.getResource("LICENSE.txt", getClass());
        log.info("url: {}", resource);
        assertNotNull(resource);
    }

    @Test
    public void testGetResources() {
        List<URL> urlList = ClassLoaderUtil
                .getResources("LICENSE.txt", getClass());
        for (URL url : urlList) {
            log.info("url: {}", url);
            assertNotNull(url);
        }
    }

    @Test
    public void testGetResourceAsStream() {
        InputStream in = ClassLoaderUtil
                .getResourceAsStream("jdbc.properties", getClass());
        assertNotNull(in);
        log.info(IoUtil.readUtf8(in));
    }

    @Test
    public void testLoadClass() throws ClassNotFoundException {
        Class<?> loadClass = ClassLoaderUtil
                .loadClass("kunlun.util.CollUtil", getClass());
        log.info("loadClass: {}", loadClass);
        assertNotNull(loadClass);
    }

}
