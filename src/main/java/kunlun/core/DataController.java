package kunlun.core;

public interface DataController {

    <T extends Rule> T getRule(Object resource, Object userId, Object userType);

    interface Rule {

    }

}
