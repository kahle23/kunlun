/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.chain;

import kunlun.chain.support.NodeConfigImpl;
import kunlun.logging.Logger;
import kunlun.logging.LoggerFactory;
import kunlun.util.ObjectUtils;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * The chain service Test.
 * @author Kahle
 */
public class ChainServiceTest {
    private static final Logger log = LoggerFactory.getLogger(ChainServiceTest.class);
    private static final String chainId = "1";

    static {
        ChainUtils.addNodeConfigs(chainId, Arrays.asList(
                new NodeConfigImpl("a", "node1", "b"),
                new NodeConfigImpl("b", "node2", "c"),
                new NodeConfigImpl("c", "node3", null)
        ));
        ChainUtils.registerNode("node1", new ChainNode() {
            @Override
            public void execute(Context context) {
                Object data = context.getResult() != null ? context.getResult() : context.getArguments()[1];
                context.setResult((Integer) data + 1);
            }
        });
        ChainUtils.registerNode("node2", new ChainNode() {
            @Override
            public void execute(Context context) {
                Object data = context.getResult() != null ? context.getResult() : context.getArguments()[1];
                context.setResult((Integer) data + 2);
            }
        });
        ChainUtils.registerNode("node3", new ChainNode() {
            @Override
            public void execute(Context context) {
                Object data = context.getResult() != null ? context.getResult() : context.getArguments()[1];
                context.setResult((Integer) data + 3);
            }
        });
    }

    @Test
    public void test1() {
        Number result = ChainUtils.execute(chainId, 1, Number.class);
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result, 7));
    }

}
