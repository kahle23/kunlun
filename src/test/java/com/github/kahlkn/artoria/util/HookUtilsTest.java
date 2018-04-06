package com.github.kahlkn.artoria.util;

import org.junit.Test;

public class HookUtilsTest {
    private static Hook hook3 = new Hook() {
        @Override
        public void call() {
            System.out.println("Hook 3333");
        }
    };

    static {
        HookUtils.addShutdownHook(new Hook() {
            @Override
            public void call() {
                // Can not using logger, just sysout.
                System.out.println("Hook 1111");
            }
        });
        HookUtils.addShutdownHook(new Hook() {
            @Override
            public void call() {
                System.out.println("Hook 2222");
            }
        });
        HookUtils.addShutdownHook(hook3);
    }

    @Test
    public void test1() {
        HookUtils.removeShutdownHook(hook3);
        System.out.println("I am running now. ");
    }

}
