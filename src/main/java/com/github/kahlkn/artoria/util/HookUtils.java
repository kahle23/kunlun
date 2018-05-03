package com.github.kahlkn.artoria.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hook tools.
 * @author Kahle
 */
public class HookUtils {
    private static final List<Hook> SHUTDOWN_HOOKS = new ArrayList<Hook>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                HookUtils.execute(SHUTDOWN_HOOKS);
            }
        });
    }

    public static void addShutdownHook(Hook hook) {
        synchronized (SHUTDOWN_HOOKS) {
            SHUTDOWN_HOOKS.add(hook);
        }
    }

    public static boolean removeShutdownHook(Hook hook) {
        synchronized (SHUTDOWN_HOOKS) {
            return SHUTDOWN_HOOKS.remove(hook);
        }
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
