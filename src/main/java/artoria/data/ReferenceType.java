package artoria.data;

import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * The reference type of java.
 * @author Kahle
 */
public enum ReferenceType {

    /**
     * Strong reference: Assign values to variables directly.
     */
    STRONG,

    /**
     * Use {@link SoftReference}s.
     */
    SOFT,

    /**
     * Use {@link WeakReference}s.
     */
    WEAK,

    /**
     * Use {@link PhantomReference}s.
     */
    PHANTOM

}
