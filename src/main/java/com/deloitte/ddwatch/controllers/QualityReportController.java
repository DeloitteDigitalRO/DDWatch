package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.QualityReportDTO;
import com.deloitte.ddwatch.services.QualityReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/reports")
public class QualityReportController {

    @Autowired
    QualityReportService qualityReportService;

//    @GetMapping
//    public ResponseEntity<List<QualityReportDTO> > getAllReports() {
//        List<QualityReportDTO> reports = qualityReportService.createDemoReport();
//        return new ResponseEntity<>(reports, HttpStatus.OK);
//    }
//
//    @GetMapping("/{componentKey}")
//    public ResponseEntity<QualityReportDTO> getReport(@PathVariable String componentKey) {
//        return new ResponseEntity<>(qualityReportService.createReport(componentKey), HttpStatus.OK);
//    }
}
