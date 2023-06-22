package artoria.aop;

/**
 * The subject for proxy Test.
 * @author Kahle
 */
public interface Subject {

    /**
     * Say hello.
     * @param name The user name
     * @return The result
     */
    String sayHello(String name);

    /**
     * Say goodbye.
     * @param name The user name
     * @return The result
     */
    String sayGoodbye(String name);

}
