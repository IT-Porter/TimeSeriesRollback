package com.ping.timeseriesrollback.constant;

public interface CacheConstants {
    /**
     *
     * check is the sensor have a living consumer
     *
     */
    String SENSOR_GROUP_LOCK="sensor_group_lock:";

    /**
     *
     * grouped by the sensor,keep each sensor data push to  its own queue
     *
     */
    String SENSOR_GROUP_QUEUE="sensor_group_queue:";
    /**
     *
     * zset to log the latest cosumer hanle time
     *
     */
    String SENSOR_LAST_HANLE_TIME="sensor_last_handle_time";
}

