package com.deloitte.ddwatch.repositories;

import com.deloitte.ddwatch.model.SonarQubeReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SonarQubeReportRepository extends JpaRepository<SonarQubeReport, Long> {
}
