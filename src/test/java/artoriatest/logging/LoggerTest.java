package artoriatest.logging;

import artoria.logging.Logger;
import org.junit.Test;

public class LoggerTest {

    @Test
    public void test1() {
        Logger.finest("Hello, World!");
        Logger.finer("Hello, World!");
        Logger.fine("Hello, World!");
        Logger.config("Hello, World!");
        Logger.info("Hello, World!");
        Logger.warning("Hello, World!");
        Logger.severe("Hello, World!");
    }

    @Test
    public void test2() {
        Logger.finest("Hello, World!", new RuntimeException());
        Logger.finer("Hello, World!", new RuntimeException());
        Logger.fine("Hello, World!", new RuntimeException());
        Logger.config("Hello, World!", new RuntimeException());
        Logger.info("Hello, World!", new RuntimeException());
        Logger.warning("Hello, World!", new RuntimeException());
        Logger.severe("Hello, World!", new RuntimeException());
    }

}
