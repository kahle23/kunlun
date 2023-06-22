package artoria.aop;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * The proxy tools Test.
 * @author Kahle
 */
public class ProxyUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(ProxyUtilsTest.class);
    private static final String name = "zhangsan";

    @Test
    public void testJdkProxy() {
        RealSubject subject = new RealSubject();
        Subject subjectProxy = ProxyUtils.proxy(
                new AbstractInterceptor<RealSubject>(subject) {
            @Override
            public Object intercept(Object proxyObject, Method method, Object[] args) throws Throwable {
                log.info("Proxy object's class is {}", proxyObject.getClass().getName());
                log.info("Hello, this is intercept. ");
                return method.invoke(getOriginalObject(), args);
            }
        });
        log.info(subjectProxy.sayHello(name));
        log.info(subjectProxy.sayGoodbye(name));
    }

}
