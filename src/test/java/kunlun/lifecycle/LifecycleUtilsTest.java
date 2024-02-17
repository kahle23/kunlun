/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.lifecycle;

import kunlun.exception.ExceptionUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

public class LifecycleUtilsTest {
    private static Logger log = LoggerFactory.getLogger(LifecycleUtilsTest.class);
    private LifecycleTestBean bean = new LifecycleTestBean();

    @Test
    public void test1() {
        try {
            LifecycleUtils.initialize(bean);
        }
        catch (LifecycleException e) {
            log.info(ExceptionUtils.toString(e));
        }
    }

    @Test
    public void test2() {

        LifecycleUtils.destroy(bean);
    }

}
