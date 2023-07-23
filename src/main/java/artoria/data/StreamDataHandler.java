package artoria.data;

import artoria.core.Handler;

/**
 * The stream data handler (equivalent callback).
 * @author Kahle
 */
public interface StreamDataHandler extends Handler {

    /**
     * Handle the stream data.
     * No return value, no failed retry (too complicated).
     *      0 Which program
     *      1 Which method
     *      2 What scene
     *      3 The method input parameter
     *      4 The data to be processed (most scene is string, readLine())
     * @param arguments The arguments to be processed
     */
    void handle(Object... arguments);

}
