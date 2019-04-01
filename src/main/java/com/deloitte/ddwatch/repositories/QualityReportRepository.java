package com.deloitte.ddwatch.repositories;

import com.deloitte.ddwatch.model.QualityReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualityReportRepository extends JpaRepository<QualityReport, Long> {
}
