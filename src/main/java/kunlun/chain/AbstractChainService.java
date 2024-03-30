/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.chain;

import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.Assert;
import kunlun.util.MapUtils;
import kunlun.util.StringUtils;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static kunlun.chain.ChainNode.Config;

/**
 * The abstract chain service.
 * @author Kahle
 */
public abstract class AbstractChainService implements ChainService {
    private static final Logger log = LoggerFactory.getLogger(AbstractChainService.class);
    protected final Map<String, ChainNode> nodes;
    protected final Map<String, Object> commonProperties;

    protected AbstractChainService(Map<String, Object> commonProperties,
                                   Map<String, ChainNode> nodes) {
        Assert.notNull(commonProperties, "Parameter \"commonProperties\" must not null. ");
        Assert.notNull(nodes, "Parameter \"nodes\" must not null. ");
        this.commonProperties = commonProperties;
        this.nodes = nodes;
    }

    public AbstractChainService() {
        this(new ConcurrentHashMap<String, Object>(),
                new ConcurrentHashMap<String, ChainNode>());
    }

    @Override
    public void registerCommonProperties(Map<?, ?> commonProperties) {
        if (MapUtils.isEmpty(commonProperties)) { return; }
        for (Map.Entry<?, ?> entry : commonProperties.entrySet()) {
            String keyStr = String.valueOf(entry.getKey());
            this.commonProperties.put(keyStr, entry.getValue());
        }
    }

    @Override
    public void clearCommonProperties() {

        this.commonProperties.clear();
    }

    @Override
    public Map<String, Object> getCommonProperties() {

        return Collections.unmodifiableMap(commonProperties);
    }

    @Override
    public void registerNode(String nodeName, ChainNode chainNode) {
        Assert.notNull(chainNode, "Parameter \"chainNode\" must not null. ");
        Assert.notBlank(nodeName, "Parameter \"nodeName\" must not blank. ");
        String className = chainNode.getClass().getName();
        nodes.put(nodeName, chainNode);
        log.info("Register the chain node \"{}\" to \"{}\". ", className, nodeName);
    }

    @Override
    public void deregisterNode(String nodeName) {
        Assert.notBlank(nodeName, "Parameter \"nodeName\" must not blank. ");
        ChainNode remove = nodes.remove(nodeName);
        if (remove != null) {
            String className = remove.getClass().getName();
            log.info("Deregister the chain node \"{}\" from \"{}\". ", className, nodeName);
        }
    }

    @Override
    public ChainNode getChainNode(String nodeName) {
        Assert.notBlank(nodeName, "Parameter \"nodeName\" must not blank. ");
        ChainNode chainNode = nodes.get(nodeName);
        Assert.notNull(chainNode
                , "The corresponding chain node could not be found by name. ");
        return chainNode;
    }

    /**
     * Construct a context based on the chainId and arguments.
     * @param chainId The chain ID
     * @param arguments The arguments when executing the chain
     * @return The context object
     */
    protected ContextImpl buildContext(String chainId, Object[] arguments) {

        return new ContextImpl(chainId, arguments);
    }

    /**
     * Build the configuration map based on the configuration object.
     * @param nodeConfig The node configuration object
     * @return The node configuration map
     */
    protected Map<String, Object> buildConfig(Config nodeConfig) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        if (MapUtils.isNotEmpty(nodeConfig.getConfigContent())) {
            map.putAll(nodeConfig.getConfigContent());
        }
        map.put("_id", nodeConfig.getId());
        map.put("_description", nodeConfig.getDescription());
        map.put("_nodeName", nodeConfig.getNodeName());
        map.put("_nextConfigId", nodeConfig.getNextConfigId());
        return map;
    }

    @Override
    public Object execute(String chainId, Object[] arguments) {
        // Build the context.
        ChainNode.Context context = buildContext(chainId, arguments);
        // Convert the node configs to map.
        Collection<? extends Config> nodeConfigs = getNodeConfigs(chainId);
        Map<String, Config> configMap = new LinkedHashMap<String, Config>();
        Config first = null;
        for (Config config : nodeConfigs) {
            if (config == null) { continue; }
            if (first == null) { first = config; }
            configMap.put(config.getId(), config);
        }
        // Iterate the node configs.
        Config now = first;
        while (now != null) {
            // Execute the chain node.
            ChainNode chainNode = getChainNode(now.getNodeName());
            chainNode.execute(buildConfig(now), context);
            // Get the next config id.
            String nextConfigId = context.getNextConfigId();
            if (StringUtils.isBlank(nextConfigId)) {
                nextConfigId = now.getNextConfigId();
            }
            if (StringUtils.isBlank(nextConfigId)) { break; }
            // Get the next config object.
            Config config = configMap.get(nextConfigId);
            Assert.notNull(config, "next node config error! ");
            now = config;
        }
        return context.getResult();
    }

    @Override
    public Object execute(String chainId, Object input, Type type) {

        return execute(chainId, new Object[]{ null, input, type });
    }

    /**
     * The inner chain context.
     * @author Kahle
     */
    protected static class ContextImpl implements ChainNode.Context {
        private String chainId;
        private Object[] arguments;
        private Object result;
        private String nextConfigId;

        public ContextImpl(String chainId, Object[] arguments) {
            this.arguments = arguments;
            this.chainId = chainId;
        }

        public ContextImpl() {
        }

        @Override
        public String getChainId() {

            return chainId;
        }

        public void setChainId(String chainId) {

            this.chainId = chainId;
        }

        @Override
        public Object[] getArguments() {

            return arguments;
        }

        public void setArguments(Object[] arguments) {

            this.arguments = arguments;
        }

        @Override
        public Object getResult() {

            return result;
        }

        @Override
        public void setResult(Object result) {

            this.result = result;
        }

        @Override
        public String getNextConfigId() {

            return nextConfigId;
        }

        @Override
        public void setNextConfigId(String nextConfigId) {

            this.nextConfigId = nextConfigId;
        }
    }

}
