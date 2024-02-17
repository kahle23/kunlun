/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.lifecycle;

/**
 * Represents that the object needs to be initialized.
 * @author Kahle
 */
public interface Initializable {

    /**
     * Initialize this object.
     * @throws LifecycleException Maybe occur exception
     */
    void initialize() throws LifecycleException;

}
