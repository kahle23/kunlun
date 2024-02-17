/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.system.User;
import kunlun.test.service.hello.HelloService;
import kunlun.test.service.hello.HelloServiceImpl;
import org.junit.Test;

/**
 * The assert tools Test.
 * @author Kahle
 */
public class AssertTest {
    private static final Logger log = LoggerFactory.getLogger(AssertTest.class);

    @Test
    public void testNotNull() {
        try {
            Assert.notNull(null, null);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void testIsSupport() {
        try {
            Assert.isSupport(User.class, Boolean.FALSE, Object.class, null, String.class);
//            Assert.isSupport(User.class, Boolean.FALSE, null, null);
//            Assert.isSupport(User.class, Boolean.FALSE);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void testIsSupport1() {
        try {
            Assert.isSupport(HelloServiceImpl.class, Boolean.TRUE, HelloService.class, null, String.class);
//            Assert.isSupport(RealSubject.class, Boolean.FALSE, Subject.class, null, String.class);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
