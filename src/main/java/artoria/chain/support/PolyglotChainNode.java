package artoria.chain.support;

import artoria.core.Chain;
import artoria.core.ChainNode;
import artoria.data.Dict;
import artoria.data.bean.BeanUtils;
import artoria.polyglot.PolyglotUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;

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
    public boolean execute(Chain.Context context) {
        // Polyglot call.
        Map<String, Object> contextMap = BeanUtils.beanToMap(context);
        Object eval = PolyglotUtils.eval(scriptName, scriptContent, contextMap);
        // If boolean and return boolean.
        if (eval instanceof Boolean) {
            return (Boolean) eval;
        }
        // Default return true.
        if (eval == null ||
                ClassUtils.isSimpleValueType(eval.getClass())) {
            context.setResult(eval);
            return true;
        }
        // If object by `_next` and default true.
        Dict dict = Dict.of(BeanUtils.beanToMap(eval));
        context.setResult(dict);
        return dict.getBoolean("_next", true);
    }

}
