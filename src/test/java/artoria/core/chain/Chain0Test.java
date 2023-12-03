package artoria.core.chain;

import artoria.chain.ListChain;
import artoria.core.Chain;
import artoria.core.ChainNode;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.ObjectUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Chain0Test {
    private static final Logger log = LoggerFactory.getLogger(Chain0Test.class);
    private static final ListChain chain = new ListChain();

    static {
        chain.add(new ChainNode() {
            @Override
            public boolean execute(Chain.Context context) {
                Object data = context.getResult() != null ? context.getResult() : context.getArguments()[1];
                context.setResult((Integer) data + 1);
                return true;
            }
        }).add(new ChainNode() {
            @Override
            public boolean execute(Chain.Context context) {
                Object data = context.getResult() != null ? context.getResult() : context.getArguments()[1];
                context.setResult((Integer) data + 100);
                return true;
            }
        }).add(new ChainNode() {
            @Override
            public boolean execute(Chain.Context context) {
                Object data = context.getResult() != null ? context.getResult() : context.getArguments()[1];
                context.setResult((Integer) data + 200);
                return true;
            }
        });
    }

    @Test
    public void test1() {
        Object result = chain.execute(10);
        log.info("result: {}", result);
        assertTrue(ObjectUtils.equals(result, 311));
    }

}
