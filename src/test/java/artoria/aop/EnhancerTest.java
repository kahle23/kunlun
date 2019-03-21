package artoria.aop;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Test;

import java.lang.reflect.Method;

public class EnhancerTest {
    private static Logger log = LoggerFactory.getLogger(EnhancerTest.class);

    public static class TestInterceptor implements Interceptor {
        private Object proxiedObject;

        public Object getProxiedObject() {

            return this.proxiedObject;
        }

        public void setProxiedObject(Object proxiedObject) {

            this.proxiedObject = proxiedObject;
        }

        public TestInterceptor(Object proxiedObject) {

            this.proxiedObject = proxiedObject;
        }

        @Override
        public Object intercept(Object proxyObject, Method method, Object[] args) throws Throwable {
            log.info("Proxy object's class is {}", proxyObject.getClass().getName());
            log.info("Hello, this is intercept. ");
            return method.invoke(this.proxiedObject, args);
        }

    }

    private String name = "zhangsan";

    @Test
    public void testJdkEnhancer() {
        Enhancer.setProxyFactory(new DefaultProxyFactory());
        RealSubject subject = new RealSubject();
        TestInterceptor intertr = new TestInterceptor(subject);
        // Subject subjectProxy = (Subject) Enhancer.enhance(subject, intertr);
        Subject subjectProxy = (Subject) Enhancer.enhance(Subject.class, intertr);
        log.info(subjectProxy.sayHello(name));
        log.info(subjectProxy.sayGoodbye(name));
    }

}
