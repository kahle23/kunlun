package kunlun.core;

public interface DataController {

    <T extends Rule> T getRule(String permission, Object userId, Object userType);

    interface Rule {

    }

}
