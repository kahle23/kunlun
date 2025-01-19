/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.action.ai;

import kunlun.action.AbstractAction;
import kunlun.core.Action;
import kunlun.core.ArtificialIntelligence;

import java.io.Serializable;

/**
 * The abstract AI action.
 * @author Kahle
 */
public abstract class AbstractAIAction extends AbstractAction implements ArtificialIntelligence, Action {

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
