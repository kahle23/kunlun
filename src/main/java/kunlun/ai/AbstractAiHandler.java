/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai;

import kunlun.core.ArtificialIntelligence;
import kunlun.core.Handler;
import kunlun.util.Assert;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * The abstract AI handler.
 * @author Kahle
 */
public abstract class AbstractAIHandler implements ArtificialIntelligence, Handler {
    private Map<Object, Object> commonProperties = Collections.emptyMap();

    public Map<Object, Object> getCommonProperties() {

        return commonProperties;
    }

    public void setCommonProperties(Map<?, ?> properties) {
        Assert.notNull(properties, "Parameter \"properties\" must not null. ");
        this.commonProperties = Collections.unmodifiableMap(properties);
    }

    /**
     * The abstract AI handler configuration.
     * @author Kahle
     */
    public static abstract class AbstractConfig implements Serializable {
        private String  proxyType;
        private String  proxyHostname;
        private Integer proxyPort;
        private Boolean debug;

        public String getProxyType() {

            return proxyType;
        }

        public void setProxyType(String proxyType) {

            this.proxyType = proxyType;
        }

        public String getProxyHostname() {

            return proxyHostname;
        }

        public void setProxyHostname(String proxyHostname) {

            this.proxyHostname = proxyHostname;
        }

        public Integer getProxyPort() {

            return proxyPort;
        }

        public void setProxyPort(Integer proxyPort) {

            this.proxyPort = proxyPort;
        }

        public Boolean getDebug() {

            return debug;
        }

        public void setDebug(Boolean debug) {

            this.debug = debug;
        }
    }

}
