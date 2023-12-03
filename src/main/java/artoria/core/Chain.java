package artoria.core;

public interface Chain {

    /**
     *
     * The arguments mean (most of the scenes):
     *      0 strategy or operation or null,
     *      1 input object,
     *      2 return value type
     * @param arguments
     * @return
     */
    Object execute(Object[] arguments);

    interface Context extends artoria.core.Context {

        Chain getChain();

        Object[] getArguments();

        Object getResult();

        void setResult(Object result);

    }

}
