package com.deloitte.ddwatch.repositories;

import com.deloitte.ddwatch.model.DeliveryReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryReportRepository extends JpaRepository<DeliveryReport, Long> {
    List<DeliveryReport> findByProjectIdOrderByUpdateDateDesc(Long projectId);
}
