/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.cache.support;

import kunlun.data.ReferenceType;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * The simple cache configuration.
 * @author Kahle
 */
public class SimpleCacheConfig implements Serializable {
    private ReferenceType referenceType;
    private Long     capacity;
    private Float    fullRatio;
    private Long     timeToLive;
    private TimeUnit timeToLiveUnit;
    private Long     timeToIdle;
    private TimeUnit timeToIdleUnit;

    public SimpleCacheConfig(ReferenceType referenceType, Long timeToLive, TimeUnit timeToLiveUnit) {
        this.referenceType = referenceType;
        this.timeToLiveUnit = timeToLiveUnit;
        this.timeToLive = timeToLive;
    }

    public SimpleCacheConfig(Long timeToLive, TimeUnit timeToLiveUnit) {
        this.timeToLiveUnit = timeToLiveUnit;
        this.timeToLive = timeToLive;
    }

    public SimpleCacheConfig(ReferenceType referenceType, Long capacity) {
        this.referenceType = referenceType;
        this.capacity = capacity;
    }

    public SimpleCacheConfig() {

    }

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

    public Float getFullRatio() {

        return fullRatio;
    }

    public void setFullRatio(Float fullRatio) {

        this.fullRatio = fullRatio;
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

}
