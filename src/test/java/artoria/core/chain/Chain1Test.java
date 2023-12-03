package artoria.core.chain;

import artoria.chain.ListChain;
import artoria.chain.support.PolyglotChainNode;
import artoria.core.Chain;
import artoria.core.ChainNode;
import artoria.data.Dict;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ObjectUtils;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class Chain1Test {
    private static final Logger log = LoggerFactory.getLogger(Chain1Test.class);
    private static final ListChain chain = new ListChain();

    static {
        String scriptName = "javascript";
        chain.add(new ChainNode() {
            @Override
            public boolean execute(Chain.Context context) {
                context.setResult(context.getArguments()[1]);
                return true;
            }
        }).add(new PolyglotChainNode(scriptName, "result.a = result.a + 1;\n" +
                        "result.b = result.b + 1;\n" +
                        "result.c = null;\n" +
                        "result;"
        )).add(new PolyglotChainNode(scriptName, "result.d = 1;" +
                        "result.e = 2;" +
                        "result.f = 3;" +
                        "result.c = result.a + result.b; " +
                        "result;"
        )).add(new PolyglotChainNode(scriptName, "result.delete(\"d\");" +
                        "result.delete(\"e\");" +
                        "result;"))
        ;
    }

    @Test
    public void test1() {
        Dict dict = Dict.of("a", 1).set("b", 2).set("c", 3);
        Dict result = Dict.of((Map<?, ?>) chain.execute(dict));
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result.getInteger("a"), 2));
        assertTrue(ObjectUtils.equals(result.getInteger("b"), 3));
        assertTrue(ObjectUtils.equals(result.getInteger("c"), 5));
        assertNull(result.get("d"));
        assertNull(result.get("e"));
        assertTrue(ObjectUtils.equals(result.getInteger("f"), 3));
    }

}
