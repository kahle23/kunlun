package artoria.core;

import java.util.Map;

/**
 * This is a step or node in the business process,
 *  or this is a node in the responsibility chain.
 * @author Kahle
 */
public interface ChainNode {

    /**
     * Execute the logic of this node.
     * @param config The configuration of this node in a certain chain
     * @param context The context object when executing this chain
     */
    void execute(Map<String, ?> config, Context context);

    /**
     * The chain node context.
     * @author Kahle
     */
    interface Context extends artoria.core.Context {

        /**
         * Get the id of the chain executed this time.
         * @return The chain id
         */
        String getChainId();

        /**
         * Get the arguments of the chain executed this time.
         * @return The chain arguments
         */
        Object[] getArguments();

        /**
         * Get the result of the chain executed this time.
         * @return The chain result
         */
        Object getResult();

        /**
         * Set the result of the chain executed this time.
         * @param result The chain result
         */
        void setResult(Object result);

        String getSelectedNextName();

        void setSelectedNextName(String nextName);

    }

}
