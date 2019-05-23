package com.deloitte.ddwatch.repositories;

import com.deloitte.ddwatch.model.MetricsReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricsReportRepository extends JpaRepository<MetricsReport, Long> {
    List<MetricsReport> findByProjectIdOrderByCreatedOnDesc(Long projectId);
}
