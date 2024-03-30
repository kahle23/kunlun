/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.chain.support;

import kunlun.chain.ChainNode;

import java.io.Serializable;
import java.util.Map;

/**
 * The chain node configuration default implementation class.
 * @author Kahle
 */
public class NodeConfigImpl implements ChainNode.Config, Serializable {
    private Map<String, Object> configContent;
    private String nextConfigId;
    private String nodeName;
    private String description;
    private String id;

    public NodeConfigImpl(String id, String nodeName, String nextConfigId) {
        this.nextConfigId = nextConfigId;
        this.nodeName = nodeName;
        this.id = id;
    }

    public NodeConfigImpl() {

    }

    @Override
    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    @Override
    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    @Override
    public String getNodeName() {

        return nodeName;
    }

    public void setNodeName(String nodeName) {

        this.nodeName = nodeName;
    }

    @Override
    public String getNextConfigId() {

        return nextConfigId;
    }

    public void setNextConfigId(String nextConfigId) {

        this.nextConfigId = nextConfigId;
    }

    @Override
    public Map<String, Object> getConfigContent() {

        return configContent;
    }

    public void setConfigContent(Map<String, Object> configContent) {

        this.configContent = configContent;
    }
}
