package artoria.core;

import java.util.Map;

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
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     */
    Object execute(String chainId, Object[] arguments);

}
