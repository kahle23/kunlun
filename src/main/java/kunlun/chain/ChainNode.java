/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.chain;

import java.util.Map;

/**
 * This is a step or node in the business process,
 *  or this is a node in the responsibility chain.
 *
 * It is primarily used in scenarios such as responsibility chains
 *  and should not be targeted at workflows or approval processes.
 *
 * @author Kahle
 */
public interface ChainNode {

    /**
     * Execute the logic of this node.
     * @param context The context object when executing this chain
     */
    void execute(Context context);

    /**
     * The chain node configuration.
     * @author Kahle
     */
    interface Config {

        /**
         * Get the node configuration id.
         * @return The node configuration id
         */
        String getId();

        /**
         * Get the node configuration description.
         * @return The node configuration description
         */
        String getDescription();

        /**
         * Get the node name in the node configuration.
         * @return The node name
         */
        String getNodeName();

        /**
         * Get the next node configuration id.
         * @return The next node configuration id
         */
        String getNextConfigId();

        /**
         * Get the node configuration content.
         * @return The node configuration content
         */
        Map<String, Object> getConfigContent();

    }

    /**
     * The chain node context.
     * @author Kahle
     */
    interface Context extends kunlun.core.Context {

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
         * Get the raw input data.
         * @return The raw input data
         */
        Object getRawInput();

        /**
         * Get the expected class.
         * @return The expected class
         */
        Class<?> getExpectedClass();

        /**
         * Get the runtime data.
         * @return The runtime data
         */
        Map<String, Object> getRuntimeData();

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

        /**
         * Get the configuration of the current node.
         * @return The configuration of the current node
         */
        Map<String, ?> getConfig();

        /**
         * Set the configuration of the current node.
         * @param config The configuration of the current node
         */
        void setConfig(Map<String, ?> config);

        /**
         * Get the next node configuration id.
         * The node configuration is the real "node".
         *
         * Regardless of how many candidate nodes there are,
         *  there will definitely be only one node that gets executed next.
         *
         * @return The next node configuration id
         */
        String getNextConfigId();

        /**
         * Set the next node configuration id.
         * @param nextConfigId The next node configuration id
         */
        void setNextConfigId(String nextConfigId);

    }

}
