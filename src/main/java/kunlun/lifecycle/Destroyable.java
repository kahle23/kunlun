/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.lifecycle;

/**
 * Represents that the object needs to be destroyed.
 * @author Kahle
 */
public interface Destroyable {

    /**
     * Destroy this object.
     * @throws Exception Maybe occur exception
     */
    void destroy() throws Exception;

}
