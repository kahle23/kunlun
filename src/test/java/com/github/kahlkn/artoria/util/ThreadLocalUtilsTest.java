package com.github.kahlkn.artoria.util;

import org.junit.Test;

public class ThreadLocalUtilsTest {
    private String testKey = "Test_Key";

    private void doGetFromThreadLocal() {
        Object value = ThreadLocalUtils.getValue(testKey);
        System.out.println(value);
    }

    @Test
    public void testSetGet() {
        ThreadLocalUtils.setValue(testKey, "rt.jar");
        doGetFromThreadLocal();
    }

    @Test
    public void testRemove() {
        ThreadLocalUtils.setValue(testKey, "rt.jar");
        doGetFromThreadLocal();
        ThreadLocalUtils.remove(testKey);
        doGetFromThreadLocal();
    }

    @Test
    public void testClear() {
        ThreadLocalUtils.setValue(testKey, "rt.jar");
        doGetFromThreadLocal();
        ThreadLocalUtils.clear();
        doGetFromThreadLocal();
        ThreadLocalUtils.setValue(testKey, "rt.jar");
        doGetFromThreadLocal();
    }

}
