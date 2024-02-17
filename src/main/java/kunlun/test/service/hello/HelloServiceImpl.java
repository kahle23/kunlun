/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.test.service.hello;

/**
 * The hello service implementation class (test class).
 * @author Kahle
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {

        return "Hello, " + name;
    }

    @Override
    public String sayGoodbye(String name) {

        return "Goodbye, " + name;
    }

}
