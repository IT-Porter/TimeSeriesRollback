package com.ping.timeseriesrollback.config;//spring依赖包

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class GlobalThreadPoolConfig {

    /**
     * 默认线程池线程池
     *
     * @return Executor
     */
    @Bean
    public ThreadPoolTaskExecutor defaultThreadPool() {
        Integer availibleNum = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor =new ThreadPoolTaskExecutor();
        //核心线程数目
        executor.setCorePoolSize(availibleNum*2);
        //指定最大线程数
        executor.setMaxPoolSize(availibleNum*2);
        //队列中最大的数目
        executor.setQueueCapacity(2147483647);
        //线程名称前缀
        executor.setThreadNamePrefix("defaultIOThreadPool_");
        //rejection-policy：当pool已经达到max size的时候，如何处理新任务
        //CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        //对拒绝task的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(60);
        //加载
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor defaultCPUThreadPool() {
        Integer availibleNum = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor =new ThreadPoolTaskExecutor();
        //核心线程数目
        executor.setCorePoolSize(availibleNum);
        //指定最大线程数
        executor.setMaxPoolSize(availibleNum);
        //队列中最大的数目
        executor.setQueueCapacity(2147483647);
        //线程名称前缀
        executor.setThreadNamePrefix("defaultCpuThreadPool_");
        //rejection-policy：当pool已经达到max size的时候，如何处理新任务
        //CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        //对拒绝task的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(60);
        //加载
        executor.initialize();
        return executor;
    }
}