package artoria.identity;

import artoria.time.DateUtils;
import artoria.util.Assert;
import artoria.util.StringUtils;

import java.util.UUID;
import java.util.logging.Logger;

import static artoria.common.Constants.MINUS;

/**
 * Id generator simple implement by jdk.
 * @author Kahle
 */
public class SimpleIdGenerator implements IdGenerator {
    private static Logger log = Logger.getLogger(SimpleIdGenerator.class.getName());
    private static final long TIME_OFFSET = DateUtils.create(2017
            , 11, 11, 11, 11, 11, 11).getTimeInMillis();
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_BITS = 10L;
    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;
    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;
    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;
    private long workerId = 1L;
    private long sequence;
    private long lastTime;

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
    public synchronized Number nextNumber() {
        long currentTime = this.takeCurrentTime();
        boolean isNormal = this.lastTime <= currentTime;
        Assert.state(isNormal, "Clock is moving backwards, last time is "
                + this.lastTime + " milliseconds, current time is " + currentTime + " milliseconds. ");
        if (this.lastTime == currentTime) {
            if ((this.sequence = ++this.sequence & SEQUENCE_MASK) == 0L) {
                currentTime = this.waitUntilNextTime(currentTime);
            }
        }
        else {
            this.sequence = 0;
        }
        this.lastTime = currentTime;
        log.fine(DateUtils.format(this.lastTime) + MINUS + this.workerId + MINUS + this.sequence);
        long timeInterval = currentTime - TIME_OFFSET;
        return (timeInterval << TIMESTAMP_LEFT_SHIFT_BITS)
                | (this.workerId << WORKER_ID_LEFT_SHIFT_BITS) | this.sequence;
    }

    private String separator;

    public String getSeparator() {

        return this.separator;
    }

    public void setSeparator(String separator) {
        Assert.notBlank(separator, "Parameter \"separator\" must not blank. ");
        this.separator = separator;
    }

    @Override
    public String nextString() {
        String uuid = UUID.randomUUID().toString();
        if (this.separator != null && !MINUS.equals(this.separator)) {
            return StringUtils.replace(uuid, MINUS, this.separator);
        }
        else {
            return uuid;
        }
    }

}
