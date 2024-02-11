package artoria.chain;

import artoria.chain.support.SimpleChainService;
import artoria.core.ChainNode;
import artoria.core.ChainService;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;

import java.lang.reflect.Type;
import java.util.Collection;

import static artoria.core.ChainNode.Config;
import static artoria.util.ObjectUtils.cast;

/**
 * The chain service tools.
 * @author Kahle
 */
public class ChainUtils {
    private static final Logger log = LoggerFactory.getLogger(ChainUtils.class);
    private static volatile ChainService chainService;

    public static ChainService getChainService() {
        if (chainService != null) { return chainService; }
        synchronized (ChainUtils.class) {
            if (chainService != null) { return chainService; }
            ChainUtils.setChainService(new SimpleChainService());
            return chainService;
        }
    }

    public static void setChainService(ChainService chainService) {
        Assert.notNull(chainService, "Parameter \"chainService\" must not null. ");
        log.info("Set chain service: {}", chainService.getClass().getName());
        ChainUtils.chainService = chainService;
    }

    public static void registerNode(String nodeName, ChainNode chainNode) {

        getChainService().registerNode(nodeName, chainNode);
    }

    public static void deregisterNode(String nodeName) {

        getChainService().deregisterNode(nodeName);
    }

    public static ChainNode getChainNode(String nodeName) {

        return getChainService().getChainNode(nodeName);
    }

    public static void addNodeConfigs(String chainId, Collection<? extends Config> nodeConfigs) {

        getChainService().addNodeConfigs(chainId, nodeConfigs);
    }

    public static void removeNodeConfigs(String chainId) {

        getChainService().removeNodeConfigs(chainId);
    }

    public static Collection<? extends Config> getNodeConfigs(String chainId) {

        return getChainService().getNodeConfigs(chainId);
    }

    public static Object execute(String chainId, Object[] arguments) {

        return getChainService().execute(chainId, arguments);
    }

    public static <T> T execute(String chainId, Object input, Type type) {

        return cast(getChainService().execute(chainId, input, type));
    }

}
