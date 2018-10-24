package artoria.lifecycle;

public class LifecycleTestBean implements Initializable, Destroyable {

    @Override
    public void initialize() throws LifecycleException {
        System.out.println(">>>> This is initialize. ");
        throw new LifecycleException("Test throw LifecycleException ... ... ");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println(">>>> This is destroy. ");
    }

}
