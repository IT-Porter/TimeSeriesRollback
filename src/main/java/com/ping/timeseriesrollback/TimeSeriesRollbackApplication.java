package com.ping.timeseriesrollback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication()
@EnableJpaAuditing
public class TimeSeriesRollbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeSeriesRollbackApplication.class, args);
    }

}
