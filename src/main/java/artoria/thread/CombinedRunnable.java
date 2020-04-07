package artoria.thread;

import artoria.exception.ExceptionUtils;
import artoria.util.Assert;
import artoria.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Combined runnable.
 * @author Kahle
 */
public class CombinedRunnable implements Runnable {
    private final List<Runnable> runnableList;
    private Boolean ignoreException = true;

    public CombinedRunnable() {

        this(new LinkedList<Runnable>());
    }

    public CombinedRunnable(List<Runnable> runnableList) {
        Assert.notNull(runnableList, "Parameter \"runnableList\" must not null. ");
        this.runnableList = runnableList;
    }

    public Boolean getIgnoreException() {

        return ignoreException;
    }

    public void setIgnoreException(Boolean ignoreException) {
        Assert.notNull(ignoreException, "Parameter \"ignoreException\" must not null. ");
        this.ignoreException = ignoreException;
    }

    public void add(Runnable runnable) {
        Assert.notNull(runnable, "Parameter \"runnable\" must not null. ");
        runnableList.add(runnable);
    }

    public void addAll(Collection<? extends Runnable> collection) {
        Assert.notEmpty(collection, "Parameter \"collection\" must not empty. ");
        runnableList.addAll(collection);
    }

    public void remove(Runnable runnable) {
        Assert.notNull(runnable, "Parameter \"runnable\" must not null. ");
        runnableList.remove(runnable);
    }

    @Override
    public void run() {
        if (CollectionUtils.isEmpty(runnableList)) {
            return;
        }
        for (Runnable runnable : runnableList) {
            if (runnable == null) { continue; }
            try {
                runnable.run();
            }
            catch (Exception e) {
                if (ignoreException) {
                    // The logger may not be available when shutdown.
                    e.printStackTrace();
                }
                else {
                    throw ExceptionUtils.wrap(e);
                }
            }
        }
        //
    }

}
