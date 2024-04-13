/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.chain;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import static kunlun.chain.ChainNode.Config;

/**
 * The service interface that the process (chain) invokes.
 *
 * It is primarily used in scenarios such as responsibility chains
 *  and should not be targeted at workflows or approval processes.
 *
 * @author Kahle
 */
public interface ChainService {

    /**
     * Register common properties' information.
     * @param commonProperties The common properties
     */
    void registerCommonProperties(Map<?, ?> commonProperties);

    /**
     * Clears common properties' information.
     */
    void clearCommonProperties();

    /**
     * Get common properties' information.
     * @return The common properties
     */
    Map<String, Object> getCommonProperties();

    /**
     * Register the chain node.
     * @param nodeName The chain node name
     * @param chainNode The chain node
     */
    void registerNode(String nodeName, ChainNode chainNode);

    /**
     * Deregister the chain node.
     * @param nodeName The chain node name
     */
    void deregisterNode(String nodeName);

    /**
     * Get the chain node by name.
     * @param nodeName The chain node name
     * @return The chain node
     */
    ChainNode getChainNode(String nodeName);

    /**
     * Add node configurations.
     * @param chainId The chain ID
     * @param nodeConfigs The node configurations
     */
    void addNodeConfigs(String chainId, Collection<? extends Config> nodeConfigs);

    /**
     * Remove node configurations based on the chain id.
     * @param chainId The chain ID
     */
    void removeNodeConfigs(String chainId);

    /**
     * Get node configurations based on the chain id.
     * @param chainId The chain ID
     * @return The node configurations
     */
    Collection<? extends Config> getNodeConfigs(String chainId);

    /**
     * Execute the corresponding logic chain based on the chain ID and input arguments.
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     * @param chainId The chain ID
     * @param arguments The arguments when executing the chain
     * @return The execution result of the chain
     */
    Object execute(String chainId, Object[] arguments);

    /**
     * Execute the corresponding logic chain based on the chain ID and input arguments.
     * @param chainId The chain ID
     * @param input The arguments when executing the chain
     * @param type The type of the return value
     * @return The execution result of the chain
     */
    Object execute(String chainId, Object input, Type type);

}
