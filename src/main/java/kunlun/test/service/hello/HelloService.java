/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.test.service.hello;

/**
 * The hello service (test class).
 * @author Kahle
 */
public interface HelloService {

    /**
     * Say hello.
     * @param name The name
     * @return The result
     */
    String sayHello(String name);

    /**
     * Say goodbye.
     * @param name The name
     * @return The result
     */
    String sayGoodbye(String name);

}
