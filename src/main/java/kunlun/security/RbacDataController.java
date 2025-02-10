package kunlun.security;

import kunlun.core.DataController;

public interface RbacDataController extends DataController {

    <T extends Rule> T getRule(Object resource, String role);

}
