package com.apyhs.artoria.callback;

/**
 * Common function.
 * @author Kahle
 */
public interface Fn<R, P> {

    /**
     * Function will be call.
     * @param p Parameter if hava
     * @return Result if hava
     */
    R call(P p);

}
