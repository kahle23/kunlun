/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.system.User;
import org.junit.Test;

import java.lang.reflect.GenericArrayType;

import static kunlun.common.constant.Numbers.ZERO;

public class TypeUtilTest {
    private static Logger log = LoggerFactory.getLogger(TypeUtilTest.class);

    @Test
    public void test1() {
        User[] users = new User[ZERO];
        GenericArrayType arrayType = TypeUtil.arrayOf(users.getClass());
        log.info("{}", arrayType);
    }

}
