package artoria.chain.support;

import artoria.chain.ChainUtils;
import artoria.core.ChainNode;
import artoria.data.Dict;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ObjectUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * The polyglot chain service Test.
 * @author Kahle
 */
public class PolyglotChainServiceTest {
    private static final Logger log = LoggerFactory.getLogger(PolyglotChainServiceTest.class);
    private static final String chainId = "1";

    static {
        ChainUtils.addNodeConfigs(chainId, Arrays.asList(
                new NodeConfigImpl("0", "node0", "a"),
                new NodeConfigImpl("a", "node1", "b"),
                new NodeConfigImpl("b", "node2", "c"),
                new NodeConfigImpl("c", "node3", null)
        ));
        String scriptName = "javascript";
        ChainUtils.registerNode("node0",
                new ChainNode() {
                    @Override
                    public void execute(Map<String, ?> config, Context context) {
                        context.setResult(context.getArguments()[1]);
                    }
                });
        ChainUtils.registerNode("node1",
                new PolyglotChainNode(scriptName, "result.a = result.a + 1;\n" +
                "result.b = result.b + 1;\n" +
                "result.c = null;\n" +
                "result;"
                ));
        ChainUtils.registerNode("node2",
                new PolyglotChainNode(scriptName, "result.d = 1;" +
                "result.e = 2;" +
                "result.f = 3;" +
                "result.c = result.a + result.b; " +
                "result;"
                ));
        ChainUtils.registerNode("node3",
                new PolyglotChainNode(scriptName, "result.delete(\"d\");" +
                "result.delete(\"e\");" +
                "result;"
                ));
    }

    @Test
    public void test1() {
        Dict dict = Dict.of("a", 1).set("b", 2).set("c", 3);
        Dict result = Dict.of((Map<?, ?>) ChainUtils.execute(chainId, dict, Map.class));
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result.getDouble("a"), 2d));
        assertTrue(ObjectUtils.equals(result.getDouble("b"), 3d));
        assertTrue(ObjectUtils.equals(result.getDouble("c"), 5d));
        assertNull(result.get("d"));
        assertNull(result.get("e"));
        assertTrue(ObjectUtils.equals(result.getInteger("f"), 3));
    }

}
