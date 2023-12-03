package artoria.core;

public interface ChainNode {

    /**
     *
     * Here, it can be determined whether the current node is executable by evaluation.
     * If it cannot be executed, you can terminate the current node by using `return true`;
     * @param context
     * @return
     */
    boolean execute(Chain.Context context);

}
