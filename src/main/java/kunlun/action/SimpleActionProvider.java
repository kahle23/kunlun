/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action;

import kunlun.core.Action;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * The simple action tools provider.
 * @author Kahle
 */
public class SimpleActionProvider extends AbstractActionProvider {

    protected SimpleActionProvider(Map<String, Object> commonProperties,
                                   Map<String, Action> actions,
                                   Map<Type,   String> shortcuts) {

        super(commonProperties, actions, shortcuts);
    }

    public SimpleActionProvider() {

    }

}
