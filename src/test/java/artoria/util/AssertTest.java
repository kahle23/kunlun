package artoria.util;

import artoria.aop.RealSubject;
import artoria.aop.Subject;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.test.bean.User;
import org.junit.Test;

/**
 * The assert tools Test.
 * @author Kahle
 */
public class AssertTest {
    private static final Logger log = LoggerFactory.getLogger(AssertTest.class);

    @Test
    public void testNotNull() {
        try {
            Assert.notNull(null, null);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void testIsSupport() {
        try {
            Assert.isSupport(User.class, Boolean.FALSE, Object.class, null, String.class);
//            Assert.isSupport(User.class, Boolean.FALSE, null, null);
//            Assert.isSupport(User.class, Boolean.FALSE);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void testIsSupport1() {
        try {
            Assert.isSupport(RealSubject.class, Boolean.TRUE, Subject.class, null, String.class);
//            Assert.isSupport(RealSubject.class, Boolean.FALSE, Subject.class, null, String.class);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
