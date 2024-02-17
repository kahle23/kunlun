/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import java.util.Map;

/**
 * The simple action tools provider.
 * @author Kahle
 */
public class SimpleActionProvider extends AbstractActionProvider {

    protected SimpleActionProvider(Map<String, Object> commonProperties,
                                   Map<String, ActionHandler> actionHandlers) {

        super(commonProperties, actionHandlers);
    }

    public SimpleActionProvider() {

    }

}
