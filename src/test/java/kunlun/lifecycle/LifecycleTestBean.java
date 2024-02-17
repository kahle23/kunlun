/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.lifecycle;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;

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
