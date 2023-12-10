package artoria.test.service.hello;

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
