/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.data.mock.MockUtil;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.test.pojo.entity.system.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CollUtilTest {
    private static Logger log = LoggerFactory.getLogger(CollUtilTest.class);
    private List<User> list = new ArrayList<User>();

    @Before
    public void init() {
        list.add(null);
        list.add(MockUtil.mock(User.class));
        list.add(null);
        list.add(MockUtil.mock(User.class));
        list.add(MockUtil.mock(User.class));
    }

    @Test
    public void removeDuplicateTest() {
        List<Integer> list = new ArrayList<Integer>();
        Collections.addAll(list, 1, 2, 1, 2, 1, 2, 3, 3, 5, 4);
        CollUtil.removeDuplicate(list);
        log.info(String.valueOf(list));
    }

    @Test
    public void removeDuplicateWithOrderTest() {
        List<Integer> list = new ArrayList<Integer>();
        Collections.addAll(list, 1, 2, 1, 2, 1, 2, 3, 3, 5, 4);
        CollUtil.removeDuplicateWithOrder(list);
        log.info(String.valueOf(list));
    }

    @Test
    public void toCollectionTest() {
        // null -> empty list
        assertTrue(CollUtil.toCollection(null).isEmpty());

        // scalar -> singleton list
        Collection<?> fromScalar = CollUtil.toCollection("a");
        assertEquals(1, fromScalar.size());
        assertEquals(Collections.singletonList("a"), fromScalar);

        // Collection passthrough returns the same instance
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, "a", "b");
        assertSame(list, CollUtil.toCollection(list));
        Set<Integer> set = new HashSet<Integer>();
        Collections.addAll(set, 1, 2);
        assertSame(set, CollUtil.toCollection(set));

        // object array -> list preserving order
        Collection<?> fromObjArray = CollUtil.toCollection(new String[]{"a", "b", "c"});
        assertEquals(Arrays.asList("a", "b", "c"), fromObjArray);

        // primitive array -> list of boxed elements
        Collection<?> fromPrimArray = CollUtil.toCollection(new int[]{1, 2, 3});
        assertEquals(Arrays.asList(1, 2, 3), fromPrimArray);
    }

}
