/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.chain.support;

import kunlun.chain.AbstractChainService;
import kunlun.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static kunlun.chain.ChainNode.Config;

/**
 * The simple chain service.
 * @author Kahle
 */
public class SimpleChainService extends AbstractChainService {
    private final Map<String, Collection<? extends Config>> configs = new ConcurrentHashMap<String, Collection<? extends Config>>();

    @Override
    public void addNodeConfigs(String chainId, Collection<? extends Config> nodeConfigs) {
        Assert.notNull(nodeConfigs, "Parameter \"nodeConfigs\" must not blank. ");
        Assert.notBlank(chainId, "Parameter \"chainId\" must not blank. ");
        configs.put(chainId, nodeConfigs);
    }

    @Override
    public void removeNodeConfigs(String chainId) {
        Assert.notBlank(chainId, "Parameter \"chainId\" must not blank. ");
        configs.remove(chainId);
    }

    @Override
    public Collection<? extends Config> getNodeConfigs(String chainId) {
        Assert.notBlank(chainId, "Parameter \"chainId\" must not blank. ");
        return configs.get(chainId);
    }

}
