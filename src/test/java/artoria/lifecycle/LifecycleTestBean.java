package artoria.lifecycle;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;

public class LifecycleTestBean implements Initializable, Destroyable {
    private static Logger log = LoggerFactory.getLogger(LifecycleTestBean.class);

    @Override
    public void initialize() throws LifecycleException {
        log.info(">>>> This is initialize. ");
        throw new LifecycleException("Test throw LifecycleException ... ... ");
    }

    @Override
    public void destroy() throws Exception {
        log.info(">>>> This is destroy. ");
    }

}
