/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.security.support;

/**
 * The empty data controller.
 * @author Kahle
 */
public class EmptyDataController extends AbstractDataController {

    @Override
    public <T extends Rule> T getRule(String permission, Object userId, Object userType) {

        return null;
    }

    @Override
    public Object execute(String strategy, Object input, Object[] arguments) {

        return null;
    }

}
