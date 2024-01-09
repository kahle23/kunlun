package artoria.chain.support;

import artoria.chain.AbstractChainService;
import artoria.core.ChainNode;
import artoria.data.Dict;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ObjectUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * The polyglot chain service Test.
 * @author Kahle
 */
public class PolyglotChainServiceTest {
    private static final Logger log = LoggerFactory.getLogger(PolyglotChainServiceTest.class);
    private static final AbstractChainService chainService;

    static {
        chainService = new AbstractChainService() {
            @Override
            protected Collection<NodeConfig> getNodeConfigs(String chainId) {
                List<NodeConfig> list = new ArrayList<NodeConfig>();
                list.add(new NodeConfigImpl("0", singletonList("a")));
                list.add(new NodeConfigImpl("a", singletonList("b")));
                list.add(new NodeConfigImpl("b", singletonList("c")));
                list.add(new NodeConfigImpl("c", null));
                return list;
            }
        };
        String scriptName = "javascript";
        chainService.registerNode("0",
                new ChainNode() {
                    @Override
                    public void execute(Map<String, ?> config, Context context) {
                        context.setResult(context.getArguments()[0]);
                    }
                });
        chainService.registerNode("a",
                new PolyglotChainNode(scriptName, "result.a = result.a + 1;\n" +
                "result.b = result.b + 1;\n" +
                "result.c = null;\n" +
                "result;"
                ));
        chainService.registerNode("b",
                new PolyglotChainNode(scriptName, "result.d = 1;" +
                "result.e = 2;" +
                "result.f = 3;" +
                "result.c = result.a + result.b; " +
                "result;"
                ));
        chainService.registerNode("c",
                new PolyglotChainNode(scriptName, "result.delete(\"d\");" +
                "result.delete(\"e\");" +
                "result;"
                ));
    }

    @Test
    public void test1() {
        Dict dict = Dict.of("a", 1).set("b", 2).set("c", 3);
        Dict result = Dict.of((Map<?, ?>) chainService.execute("1", new Object[]{dict}));
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result.getInteger("a"), 2));
        assertTrue(ObjectUtils.equals(result.getInteger("b"), 3));
        assertTrue(ObjectUtils.equals(result.getInteger("c"), 5));
        assertNull(result.get("d"));
        assertNull(result.get("e"));
        assertTrue(ObjectUtils.equals(result.getInteger("f"), 3));
    }

}
