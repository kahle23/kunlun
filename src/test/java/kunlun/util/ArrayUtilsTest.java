/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.util;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * The array tools Test.
 * @author Kahle
 */
public class ArrayUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(ArrayUtilsTest.class);

    @Test
    public void reverseTest1() {
        byte[] arr = new byte[]{1, 2, 3, 4, 5};
        ArrayUtils.reverse(arr);
        log.info(Arrays.toString(arr));
    }

    @Test
    public void reverseTest2() {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5};
        ArrayUtils.reverse(arr);
        log.info(Arrays.toString(arr));
    }

    @Test
    public void toListTest1() {
        int[] arr = new int[]{1, 2, 3, 4, 5};
        List<Integer> list = ArrayUtils.toList(arr);
        log.info("list {}, class {}", list, list.getClass());
    }

    @Test
    public void toListTest2() {
        Integer[] arr = new Integer[]{1, 2, 3, 4, 5};
        List<Integer> list = ArrayUtils.toList(arr);
        log.info("list {}, class {}", list, list.getClass());
    }

}
