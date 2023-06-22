package artoria.aop;

/**
 * The real subject for proxy Test.
 * @author Kahle
 */
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
