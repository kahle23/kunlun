package com.github.kahlkn.artoria.aop;

public class RealSubject implements Subject {

    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    @Override
    public String sayGoodbye(String name) {
        return "Goodbye, " + name;
    }

}
