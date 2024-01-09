package artoria.chain.support;

import artoria.core.ChainNode;
import artoria.data.bean.BeanUtils;
import artoria.polyglot.PolyglotUtils;
import artoria.util.Assert;

import java.util.Map;

public class PolyglotChainNode implements ChainNode {
    private final String scriptContent;
    private final String scriptName;

    public PolyglotChainNode(String scriptName, String scriptContent) {
        Assert.notBlank(scriptContent, "Parameter \"scriptContent\" must not blank. ");
        Assert.notBlank(scriptName, "Parameter \"scriptName\" must not blank. ");
        this.scriptContent = scriptContent;
        this.scriptName = scriptName;
    }

    public String getScriptName() {

        return scriptName;
    }

    public String getScriptContent() {

        return scriptContent;
    }

    @Override
    public void execute(Map<String, ?> config, Context context) {
        // Polyglot call.
        Map<String, Object> contextMap = BeanUtils.beanToMap(context);
        Object eval = PolyglotUtils.eval(scriptName, scriptContent, contextMap);
        context.setResult(eval);
    }

}
