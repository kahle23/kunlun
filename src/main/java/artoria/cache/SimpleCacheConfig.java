package artoria.cache;

import artoria.lang.ReferenceType;

import java.util.concurrent.TimeUnit;

/**
 * The simple cache configuration.
 * @author Kahle
 */
public class SimpleCacheConfig implements CacheConfig {
    private ReferenceType referenceType;
    private Long capacity;
    private Long timeToLive;
    private TimeUnit timeToLiveUnit;
    private Long timeToIdle;
    private TimeUnit timeToIdleUnit;

    public ReferenceType getReferenceType() {

        return referenceType;
    }

    public void setReferenceType(ReferenceType referenceType) {

        this.referenceType = referenceType;
    }

    public Long getCapacity() {

        return capacity;
    }

    public void setCapacity(Long capacity) {

        this.capacity = capacity;
    }

    public Long getTimeToLive() {

        return timeToLive;
    }

    public void setTimeToLive(Long timeToLive) {

        this.timeToLive = timeToLive;
    }

    public TimeUnit getTimeToLiveUnit() {

        return timeToLiveUnit;
    }

    public void setTimeToLiveUnit(TimeUnit timeToLiveUnit) {

        this.timeToLiveUnit = timeToLiveUnit;
    }

    public Long getTimeToIdle() {

        return timeToIdle;
    }

    public void setTimeToIdle(Long timeToIdle) {

        this.timeToIdle = timeToIdle;
    }

    public TimeUnit getTimeToIdleUnit() {

        return timeToIdleUnit;
    }

    public void setTimeToIdleUnit(TimeUnit timeToIdleUnit) {

        this.timeToIdleUnit = timeToIdleUnit;
    }

    public SimpleCacheConfig(ReferenceType referenceType, Long capacity) {
        this.referenceType = referenceType;
        this.capacity = capacity;
    }

    public SimpleCacheConfig(ReferenceType referenceType) {

        this.referenceType = referenceType;
    }

    public SimpleCacheConfig() {

    }

}
