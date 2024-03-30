/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.chain;

import java.util.Map;

/**
 * This is a step or node in the business process,
 *      or this is a node in the responsibility chain.
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
         * Get the next node configuration id.
         * The node configuration is the real "node".
         * Regardless of how many candidate nodes there are,
         *  there will definitely be only one node that gets executed next.
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
