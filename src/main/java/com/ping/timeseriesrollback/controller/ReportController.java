package com.ping.timeseriesrollback.controller;

import com.ping.timeseriesrollback.entity.Report;
import com.ping.timeseriesrollback.repository.ReportRepository;
import com.ping.timeseriesrollback.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author freeh
 */
@RestController
@AllArgsConstructor
@RequestMapping("/report")
public class ReportController {
    private final ReportRepository reportRepository;




    @GetMapping("/test")
    @SneakyThrows
    public void test() {
        Report report = new Report();
        report.setSensorData("data");
        report.setSensorId(370);
        report.setSensorTime(System.currentTimeMillis());
        report.setStatus(0);
        reportRepository.saveAndFlush(report);
        report.setStatus(1);
        Thread.sleep(2000);
        reportRepository.save(report);
    }
}
