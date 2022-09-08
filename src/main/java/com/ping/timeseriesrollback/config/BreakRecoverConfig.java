package com.ping.timeseriesrollback.config;

import com.ping.timeseriesrollback.constant.CacheConstants;
import com.ping.timeseriesrollback.service.ReportService;
import com.ping.timeseriesrollback.util.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;
import java.util.Set;

/**
 * @author freeh
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableScheduling
public class BreakRecoverConfig {

    private final RedisUtil redisUtil;

    private final ReportService reportService;

    private static Integer index = 0;
    /**
     *  check every 10 seconds:
     *  try to start thread to handle the queue data
     *  if a sensor last thread started 1 minute ago
     *  remove all the sensors from the set which last thread started 5 minutes ago
     *  total try 60*4/10 = 24 times
     */
    @Scheduled(cron = "*/10 * * * *  ? ")
    public void breakRecover() {

        Long endTime = System.currentTimeMillis() / 1000 ;

        Set<String> list = redisUtil.zRange(CacheConstants.SENSOR_LAST_HANLE_TIME, 0, endTime-60);

        Optional.ofNullable(list).map(setList -> {

            setList.forEach(senorId -> {

                reportService.handle(senorId);

            });
            log.info("retry set {}",setList);
            return null;
        });

       redisUtil.zRemoveRangeByScore(CacheConstants.SENSOR_LAST_HANLE_TIME,0,endTime-60*5);

    }

    @Scheduled(cron = "* * * * *  ? ")
    public void breakRecoverCreator() {

        redisUtil.zAdd(CacheConstants.SENSOR_LAST_HANLE_TIME,BreakRecoverConfig.index.toString(),System.currentTimeMillis()/1000);
        BreakRecoverConfig.index++;
    }
}
