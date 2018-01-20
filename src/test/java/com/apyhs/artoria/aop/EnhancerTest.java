package com.apyhs.artoria.aop;

import org.junit.Test;

import java.lang.reflect.Method;

public class EnhancerTest {

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
            System.out.println("Proxy object's class is " + proxyObject.getClass().getName());
            System.out.println("Why i am in here. ");
            return method.invoke(proxiedObject, args);
        }

    }

    @Test
    public void testJdkEnhancer() {
        Enhancer.setProxyFactory(new JdkProxyFactory());
        RealSubject subject = new RealSubject();
        TestInterceptor intertr = new TestInterceptor(subject);
        Subject subjectProxy = (Subject) Enhancer.enhance(subject, intertr);
        System.out.println(subjectProxy.sayHello("zhangsan"));
        System.out.println(subjectProxy.sayGoodBye("zhangsan"));
    }

    @Test
    public void testCglibEnhancer() {
        Enhancer.setProxyFactory(new CglibProxyFactory());
        RealSubject subject = new RealSubject();
        TestInterceptor intertr = new TestInterceptor(subject);
        RealSubject subjectProxy = (RealSubject) Enhancer.enhance(subject, intertr);
        System.out.println(subjectProxy.sayHello("zhangsan"));
        System.out.println(subjectProxy.sayGoodBye("zhangsan"));
    }

}
