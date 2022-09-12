package com.ping.timeseriesrollback.config;


import com.ping.timeseriesrollback.constant.CacheConstants;
import com.ping.timeseriesrollback.entity.Message;
import com.ping.timeseriesrollback.service.ReportService;
import com.ping.timeseriesrollback.util.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author freeh
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class Consumer {
 private final RedisUtil redisUtil;
 private final RedissonClient redissonClient;
 private final ReportService reportService;
    @KafkaListener(topics="message-queue",properties = {"spring.json.value.default.type=com.ping.timeseriesrollback.entity.Message"})
    public void dispatch(@Payload Message message){

        if(message != null ){

            try {

                String singleGroupLockKey=CacheConstants.SENSOR_GROUP_LOCK
                        .concat(message.getSenorId().toString());
                RLock lock = redissonClient.getLock(singleGroupLockKey);

                redisUtil.lLeftPush(
                        CacheConstants.SENSOR_GROUP_QUEUE.concat(message.getSenorId().toString()),
                        message
                );

                if(!lock.isLocked())
                {

                    reportService.handle(message.getSenorId().toString());

                    redisUtil.zAdd(
                            CacheConstants.SENSOR_LAST_HANLE_TIME,
                            message.getSenorId().toString(),
                            System.currentTimeMillis()/1000);
                }


            } catch (Exception e) {
                log.info("catch error:{}",e);
            } finally {


            }
        }
    }

}
