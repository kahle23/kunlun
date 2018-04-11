package com.github.kahlkn.artoria.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hook tools.
 * @author Kahle
 */
public class HookUtils {
    private static List<Hook> shutdownHooks = new ArrayList<Hook>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                HookUtils.execute(shutdownHooks);
            }
        });
    }

    public static void addShutdownHook(Hook hook) {
        shutdownHooks.add(hook);
    }

    public static boolean removeShutdownHook(Hook hook) {
        return shutdownHooks.remove(hook);
    }

    public static void execute(Hook... hooks) {
        // Convert to list.
        HookUtils.execute(Arrays.asList(hooks));
    }

    public static void execute(List<Hook> hooks) {
        if (CollectionUtils.isEmpty(hooks)) {
            return;
        }
        for (Hook hook : hooks) {
            if (hook == null) {
                continue;
            }
            hook.call();
        }
    }

}
