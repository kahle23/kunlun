package com.apyhs.artoria.reflect;

import com.apyhs.artoria.entity.Student;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PerformanceTest {
    private static final Integer count = 40;
    private static final Integer loopCount = 100000;

    @Test
    public void test1() throws Exception {
        Field reflecter = ReflectUtils.findField(ReflectUtils.class, "reflecter");
        ReflectUtils.makeAccessible(reflecter);
        reflecter.set(null, new JdkReflecter());
        System.out.print("ReflectUtils not cache: ");
        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < loopCount; j++) {
                ReflectUtils.findAccessMethods(Student.class);
            }
            long end = System.currentTimeMillis();
            System.out.print((end - start) + " ");
        }
        System.out.println();
    }

    @Test
    public void test2() throws Exception {
        ReflectUtils.setReflecter(new JdkReflecter());
        System.out.print("ReflectUtils has cache: ");
        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            for (int j = 0; j < loopCount; j++) {
                ReflectUtils.findAccessMethods(Student.class);
            }
            long end = System.currentTimeMillis();
            System.out.print((end - start) + " ");
        }
        System.out.println();
    }

    @Test
    public void test3() throws Exception {
        Field reflecter = ReflectUtils.findField(ReflectUtils.class, "reflecter");
        ReflectUtils.makeAccessible(reflecter);
        reflecter.set(null, new JdkReflecter());
        System.out.print("ReflectUtils no cache(multithreaded): ");

        ExecutorService pool = Executors.newFixedThreadPool(count);
        List<Future<Long>> list = new ArrayList<Future<Long>>();

        for (int i = 0; i < count; i++) {
            Callable<Long> c = new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    long start = System.currentTimeMillis();
                    for (int j = 0; j < loopCount; j++) {
                        ReflectUtils.findAccessMethods(Student.class);
                    }
                    long end = System.currentTimeMillis();
                    return end - start;
                }
            };
            Future<Long> f = pool.submit(c);
            list.add(f);
        }
        pool.shutdown();

        for (Future f : list) {
            System.out.print(f.get() + " ");
        }

        System.out.println();
    }

    @Test
    public void test4() throws Exception {
        ReflectUtils.setReflecter(new JdkReflecter());
        System.out.print("ReflectUtils has cache(multithreaded): ");

        ExecutorService pool = Executors.newFixedThreadPool(count);
        List<Future<Long>> list = new ArrayList<Future<Long>>();

        for (int i = 0; i < count; i++) {
            Callable<Long> c = new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    long start = System.currentTimeMillis();
                    for (int j = 0; j < loopCount; j++) {
                        ReflectUtils.findAccessMethods(Student.class);
                    }
                    long end = System.currentTimeMillis();
                    return end - start;
                }
            };
            Future<Long> f = pool.submit(c);
            list.add(f);
        }
        pool.shutdown();

        for (Future f : list) {
            System.out.print(f.get() + " ");
        }

        System.out.println();
    }

}
