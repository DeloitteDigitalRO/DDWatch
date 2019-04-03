package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.services.SonarQubeReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class QualityReportController {

    @Autowired
    SonarQubeReportService sonarQubeReportService;

//    @GetMapping
//    public ResponseEntity<List<SonarQubeReportDTO> > getAllReports() {
//        List<SonarQubeReportDTO> reports = qualityReportService.createDemoReport();
//        return new ResponseEntity<>(reports, HttpStatus.OK);
//    }
//
//    @GetMapping("/{componentKey}")
//    public ResponseEntity<SonarQubeReportDTO> getReport(@PathVariable String componentKey) {
//        return new ResponseEntity<>(qualityReportService.createReport(componentKey), HttpStatus.OK);
//    }
}
