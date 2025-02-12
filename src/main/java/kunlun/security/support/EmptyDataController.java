package kunlun.security.support;

import kunlun.core.DataController;

public class EmptyDataController implements DataController {

    @Override
    public <T extends Rule> T getRule(String permission, Object userId, Object userType) {

        return null;
    }

}
