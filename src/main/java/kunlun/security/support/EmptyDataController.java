package kunlun.security.support;

import kunlun.core.DataController;

public class EmptyDataController implements DataController {

    @Override
    public <T extends Rule> T getRule(String permission, Object userId, Object userType) {

        return null;
    }

    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {

        return null;
    }

}
