package com.ping.timeseriesrollback.service.impl;

import com.ping.timeseriesrollback.constant.CacheConstants;
import com.ping.timeseriesrollback.entity.Message;
import com.ping.timeseriesrollback.entity.Report;
import com.ping.timeseriesrollback.repository.ReportRepository;
import com.ping.timeseriesrollback.service.ReportService;
import com.ping.timeseriesrollback.util.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final RedisUtil redisUtil;

    private final RedissonClient redissonClient;

    @Qualifier("defaultThreadPool")
    private final ThreadPoolTaskExecutor executors;

    private final ReportRepository reportRepository;

    @Override
    public void save(Report report) {
        reportRepository.save(report);
    }

    @Override
    public void handle(String senorId) {

        CompletableFuture.runAsync(() -> {
            String singleGroupLockKey=CacheConstants.SENSOR_GROUP_LOCK.concat(senorId);
            RLock lock = redissonClient.getLock(singleGroupLockKey);

            try {
                boolean tryLock = lock.tryLock(10, TimeUnit.SECONDS);
                if (tryLock) {
                  while(true)
                  {
                      String singleQueueKey = CacheConstants.SENSOR_GROUP_QUEUE.concat(senorId);
                      Boolean isEmpty = Optional.ofNullable(redisUtil.lRightPop(singleQueueKey))
                             .map(data->{
                                 Message lastMessage= (Message) data;
                                 Report report=new Report();
                                 report.setSensorId(lastMessage.getSenorId());
                                 report.setSensorData(lastMessage.getSenorData());
                                 report.setStatus(0);
                                 report.setSensorTime(lastMessage.getSenorTime());
                                 save(report);
                                 // todo rollback queue

                                 return  false;
                             }).orElse( true );

                     if(isEmpty)
                     {
                         break;
                     }

                  }
                }
            } catch (Exception e) {
                 log.info("catch error:{}",e);
            } finally {

                if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }

            }
        }, executors);
    }
}
