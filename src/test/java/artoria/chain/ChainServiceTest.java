package artoria.chain;

import artoria.core.ChainNode;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ObjectUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertTrue;

/**
 * The chain service Test.
 * @author Kahle
 */
public class ChainServiceTest {
    private static final Logger log = LoggerFactory.getLogger(ChainServiceTest.class);
    private static final AbstractChainService chainService;

    static {
        chainService = new AbstractChainService() {
            @Override
            protected Collection<NodeConfig> getNodeConfigs(String chainId) {
                List<NodeConfig> list = new ArrayList<NodeConfig>();
                list.add(new NodeConfigImpl("a", singletonList("b")));
                list.add(new NodeConfigImpl("b", singletonList("c")));
                list.add(new NodeConfigImpl("c", null));
                return list;
            }
        };
        chainService.registerNode("a", new ChainNode() {
            @Override
            public void execute(Map<String, ?> config, Context context) {
                Object data = context.getResult() != null ? context.getResult() : context.getArguments()[0];
                context.setResult((Integer) data + 1);
            }
        });
        chainService.registerNode("b", new ChainNode() {
            @Override
            public void execute(Map<String, ?> config, Context context) {
                Object data = context.getResult() != null ? context.getResult() : context.getArguments()[0];
                context.setResult((Integer) data + 2);
            }
        });
        chainService.registerNode("c", new ChainNode() {
            @Override
            public void execute(Map<String, ?> config, Context context) {
                Object data = context.getResult() != null ? context.getResult() : context.getArguments()[0];
                context.setResult((Integer) data + 3);
            }
        });
    }

    @Test
    public void test1() {
        Object result = chainService.execute("1", new Object[]{1});
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result, 7));
    }

}
