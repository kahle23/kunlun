/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.data.mock.MockUtils;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.system.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionUtilsTest {
    private static Logger log = LoggerFactory.getLogger(CollectionUtilsTest.class);
    private List<User> list = new ArrayList<User>();

    @Before
    public void init() {
        list.add(null);
        list.add(MockUtils.mock(User.class));
        list.add(null);
        list.add(MockUtils.mock(User.class));
        list.add(MockUtils.mock(User.class));
    }

    @Test
    public void removeDuplicateTest() {
        List<Integer> list = new ArrayList<Integer>();
        Collections.addAll(list, 1, 2, 1, 2, 1, 2, 3, 3, 5, 4);
        CollectionUtils.removeDuplicate(list);
        log.info(String.valueOf(list));
    }

    @Test
    public void removeDuplicateWithOrderTest() {
        List<Integer> list = new ArrayList<Integer>();
        Collections.addAll(list, 1, 2, 1, 2, 1, 2, 3, 3, 5, 4);
        CollectionUtils.removeDuplicateWithOrder(list);
        log.info(String.valueOf(list));
    }

}
