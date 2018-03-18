package com.github.kahlkn.artoria.aop;

import com.github.kahlkn.artoria.logging.Logger;
import com.github.kahlkn.artoria.logging.LoggerFactory;
import org.junit.Test;

import java.lang.reflect.Method;

public class EnhancerTest {
    private static Logger log = LoggerFactory.getLogger(EnhancerTest.class);

    public static class TestInterceptor implements Interceptor {
        private Object proxiedObject;

        public Object getProxiedObject() {
            return proxiedObject;
        }

        public void setProxiedObject(Object proxiedObject) {
            this.proxiedObject = proxiedObject;
        }

        public TestInterceptor(Object proxiedObject) {
            this.proxiedObject = proxiedObject;
        }

        @Override
        public Object intercept(Object proxyObject, Method method, Object[] args) throws Throwable {
            log.info("Proxy object's class is " + proxyObject.getClass().getName());
            System.out.println("Hello, this is intercept. ");
            return method.invoke(proxiedObject, args);
        }

    }

    private String name = "zhangsan";

    @Test
    public void testJdkEnhancer() {
        Enhancer.setProxyFactory(new JdkProxyFactory());
        RealSubject subject = new RealSubject();
        TestInterceptor intertr = new TestInterceptor(subject);
        // Subject subjectProxy = (Subject) Enhancer.enhance(subject, intertr);
        Subject subjectProxy = (Subject) Enhancer.enhance(Subject.class, intertr);
        System.out.println(subjectProxy.sayHello(name));
        System.out.println(subjectProxy.sayGoodbye(name));
    }

    @Test
    public void testCglibEnhancer() {
        Enhancer.setProxyFactory(new CglibProxyFactory());
        RealSubject subject = new RealSubject();
        TestInterceptor intertr = new TestInterceptor(subject);
        // RealSubject subjectProxy = (RealSubject) Enhancer.enhance(subject, intertr);
        Subject subjectProxy = (Subject) Enhancer.enhance(Subject.class, intertr);
        System.out.println(subjectProxy.sayHello(name));
        System.out.println(subjectProxy.sayGoodbye(name));
    }

}
