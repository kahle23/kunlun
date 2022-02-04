package artoria.action;

import artoria.action.handler.ActionHandler;

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
