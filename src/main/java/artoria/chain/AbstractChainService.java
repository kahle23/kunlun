package artoria.chain;

import artoria.core.ChainNode;
import artoria.core.ChainService;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.MapUtils;
import artoria.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The abstract chain service.
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
     *
     * @param chainId
     * @param arguments
     * @return
     */
    protected ContextImpl buildContext(String chainId, Object[] arguments) {

        return new ContextImpl(chainId, arguments);
    }

    /**
     *
     * @param chainId
     * @return
     */
    protected abstract Collection<NodeConfig> getNodeConfigs(String chainId);

    @Override
    public Object execute(String chainId, Object[] arguments) {
        //
        ChainNode.Context context = buildContext(chainId, arguments);
        //
        Collection<NodeConfig> nodeConfigs = getNodeConfigs(chainId);
        Map<String, List<NodeConfig>> map = new LinkedHashMap<String, List<NodeConfig>>();
        NodeConfig first = null;
        for (NodeConfig config : nodeConfigs) {
            if (config == null) { continue; }
            if (first == null) { first = config; }
            String nodeName = config.getName();
            List<NodeConfig> list = map.get(nodeName);
            if (list == null) {
                map.put(nodeName, list = new ArrayList<NodeConfig>());
            }
            list.add(config);
        }
        //
        NodeConfig now = first;
        while (now != null) {
            //
            ChainNode chainNode = getChainNode(now.getName());
            chainNode.execute(now.getContent(), context);
            //
            List<String> nextNames = now.getNextNames();
            String selectedName;
            if (nextNames != null && nextNames.size() == 1) {
                selectedName = CollectionUtils.getFirst(nextNames);
            }
            else { selectedName = context.getSelectedNextName(); }
            //
            if (StringUtils.isBlank(selectedName)) { break; }
            //
            List<NodeConfig> list = map.get(selectedName);
            if (list.size() > 1) {
                Integer order = now.getOrder();
                for (NodeConfig config : list) {
                    if (config.getOrder() > order) {
                        now = config; break;
                    }
                    else {
                        throw new IllegalStateException("next node config error! ");
                    }
                }
            }
            else { now = CollectionUtils.getFirst(list); }
        }
        return context.getResult();
    }

    /**
     * The node config.
     */
    public interface NodeConfig {

        Long    getId();

        String  getName();

        Integer getOrder();

        List<String> getNextNames();

        Map<String, Object> getContent();

    }

    /**
     * The node config.
     */
    public static class NodeConfigImpl implements NodeConfig, Serializable {
        private Long    id;
        private String  name;
        private Integer order;
        private List<String> nextNames;
        private Map<String, Object> content;

        public NodeConfigImpl(String name, List<String> nextNames) {
            this.nextNames = nextNames;
            this.name = name;
        }

        public NodeConfigImpl() {

        }

        @Override
        public Long getId() {

            return id;
        }

        public void setId(Long id) {

            this.id = id;
        }

        @Override
        public String getName() {

            return name;
        }

        public void setName(String name) {

            this.name = name;
        }

        @Override
        public Integer getOrder() {

            return order;
        }

        public void setOrder(Integer order) {

            this.order = order;
        }

        @Override
        public List<String> getNextNames() {

            return nextNames;
        }

        public void setNextNames(List<String> nextNames) {

            this.nextNames = nextNames;
        }

        @Override
        public Map<String, Object> getContent() {

            return content;
        }

        public void setContent(Map<String, Object> content) {

            this.content = content;
        }
    }

    public static class ContextImpl implements ChainNode.Context {
        private String chainId;
        private Object[] arguments;
        private Object result;
        private String selectedNextName;

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
        public String getSelectedNextName() {

            return selectedNextName;
        }

        @Override
        public void setSelectedNextName(String selectedNextName) {

            this.selectedNextName = selectedNextName;
        }
    }

}
