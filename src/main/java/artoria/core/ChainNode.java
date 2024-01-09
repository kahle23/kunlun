package artoria.core;

import java.util.Map;

public interface ChainNode {

    void execute(Map<String, ?> config, Context context);


    /**
     * The chain node context.
     * @author Kahle
     */
    interface Context extends artoria.core.Context {

        String getChainId();

        Object[] getArguments();

        Object getResult();

        void setResult(Object result);

        String getSelectedNextName();

        void setSelectedNextName(String nextName);

    }

}
