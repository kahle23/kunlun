package com.github.kahlkn.artoria.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Hook tools.
 * @author Kahle
 */
public class HookUtils {
    private static List<Hook> shutdownHooks = new ArrayList<Hook>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                if (CollectionUtils.isEmpty(shutdownHooks)) {
                    return;
                }
                for (Hook hook : shutdownHooks) {
                    if (hook == null) { continue; }
                    hook.call();
                }
            }
        });
    }

    public static void addShutdownHook(Hook hook) {
        shutdownHooks.add(hook);
    }

    public static boolean removeShutdownHook(Hook hook) {
        return shutdownHooks.remove(hook);
    }

}
