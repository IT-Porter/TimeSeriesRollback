package com.ping.timeseriesrollback.repository;

import com.ping.timeseriesrollback.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author freeh
 */
public interface ReportRepository extends JpaRepository<Report,Long> {
}
