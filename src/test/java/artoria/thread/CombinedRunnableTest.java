package artoria.thread;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

public class CombinedRunnableTest {
    private static Logger log = LoggerFactory.getLogger(CombinedRunnableTest.class);

    @Test
    public void test1() {
        CombinedRunnable compositeRunnable = new CombinedRunnable();
        compositeRunnable.add(new Runnable() {
            @Override
            public void run() {
                System.out.println(">> Runnable 10");
            }
        });
        compositeRunnable.add(new Runnable() {
            @Override
            public void run() {
                System.out.println(">> Runnable 2");
            }
        });
        compositeRunnable.add(new Runnable() {
            @Override
            public void run() {
                System.out.println(">> Runnable 5");
            }
        });
        compositeRunnable.add(new Runnable() {
            @Override
            public void run() {
                System.out.println(">> Runnable -100");
            }
        });
        compositeRunnable.run();
    }

}
