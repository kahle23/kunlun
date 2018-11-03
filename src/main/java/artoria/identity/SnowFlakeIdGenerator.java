package artoria.identity;

import artoria.time.DateUtils;
import artoria.util.Assert;

import java.util.logging.Logger;

import static artoria.common.Constants.MINUS;

/**
 * Id generator implement by snow flake id simple.
 * @author Kahle
 */
public class SnowFlakeIdGenerator implements IdGenerator<Long> {
    private static final long TIME_OFFSET = DateUtils.create(2018
            , 1, 1, 0, 0, 0, 0).getTimeInMillis();
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_BITS = 10L;
    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;
    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;
    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;
    private static Logger log = Logger.getLogger(SnowFlakeIdGenerator.class.getName());
    private long workerId = 1L;
    private long sequence;
    private long lastTime;

    public SnowFlakeIdGenerator() {
    }

    public SnowFlakeIdGenerator(long workerId) {

        this.setWorkerId(workerId);
    }

    public long getWorkerId() {

        return this.workerId;
    }

    public void setWorkerId(Long workerId) {
        boolean isNormal = workerId >= 0L && workerId < WORKER_ID_MAX_VALUE;
        Assert.state(isNormal, "Parameter \"workerId\" must " +
                "greater than 0 and less than " + WORKER_ID_MAX_VALUE + ". ");
        this.workerId = workerId;
    }

    private long takeCurrentTime() {

        return System.currentTimeMillis();
    }

    private long waitUntilNextTime(long thisTime) {
        long currentTime = this.takeCurrentTime();
        while (currentTime <= thisTime) {
            currentTime = this.takeCurrentTime();
        }
        return currentTime;
    }

    @Override
    public Long next() {
        long currentTime = this.takeCurrentTime();
        boolean isNormal = lastTime <= currentTime;
        Assert.state(isNormal, "Clock is moving backwards, last time is "
                + lastTime + " milliseconds, current time is " + currentTime + " milliseconds. ");
        if (lastTime == currentTime) {
            if ((sequence = ++sequence & SEQUENCE_MASK) == 0L) {
                currentTime = this.waitUntilNextTime(currentTime);
            }
        }
        else {
            sequence = 0;
        }
        lastTime = currentTime;
        log.fine(DateUtils.format(lastTime) + MINUS + workerId + MINUS + sequence);
        long timeInterval = currentTime - TIME_OFFSET;
        return (timeInterval << TIMESTAMP_LEFT_SHIFT_BITS) | (workerId << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
    }

}
