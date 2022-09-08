package com.ping.timeseriesrollback.service;

import com.ping.timeseriesrollback.entity.Message;
import com.ping.timeseriesrollback.entity.Report;

public interface ReportService {
    void save(Report report);
    void handle(String senorId);
}
