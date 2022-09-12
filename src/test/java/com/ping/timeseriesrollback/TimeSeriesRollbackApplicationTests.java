package com.ping.timeseriesrollback;

import com.ping.timeseriesrollback.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class TimeSeriesRollbackApplicationTests {


    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisUtil redisUtil;



    @Test
    void contextLoads() throws InterruptedException {
        ExecutorService executors = Executors.newFixedThreadPool(10);
        redissonClient.getBucket("hello").set("bug");
        String test = (String) redissonClient.getBucket("hello").get();
        System.out.println(test);

        redisUtil.set("test","values");
        System.out.println(redisUtil.get("test"));

//for(Integer i=0;i<50;i++) {
//    String key = "lock_"+(i%5);
//
//
//}

        String key="lock_1";
        CompletableFuture.runAsync(() -> {
            RLock lock = redissonClient.getLock(key);
            try {
                // 尝试拿锁10s后停止重试,返回false
                // 具有Watch Dog 自动延期机制 默认续30s
                boolean tryLock = lock.tryLock(10, TimeUnit.SECONDS);

                if (tryLock) {

                    Thread.sleep(60000);

                } else {
                    System.out.println("try lock failed");
                }

            } catch (InterruptedException e) {

            } finally {

                if (lock.isHeldByCurrentThread()) {
                    // 获取锁的线程才能解锁
                    lock.unlock();
                }

            }
        }, executors);


        while(true)
        {

                RLock lock1 = redissonClient.getLock(key);
                System.out.println(key+" is locked " + lock1.isLocked());

            Thread.sleep(2000);
        }

    }

}
