package com.apyhs.artoria.aop;

public class RealSubject implements Subject {

    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    @Override
    public String sayGoodBye(String name) {
        return "Good bye, " + name;
    }

}
