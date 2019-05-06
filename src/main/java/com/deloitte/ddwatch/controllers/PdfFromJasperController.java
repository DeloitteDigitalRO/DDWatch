package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.services.ProjectService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class PdfFromJasperController {
    private static Logger logger = LoggerFactory.getLogger(PdfFromJasperController.class.getCanonicalName());
    private JasperDesign jasperDesign;
    private JasperReport jasperReport;
    private JasperPrint jasperPrint;
    private Map<String, Object> params = new HashMap<>();

    @Autowired
    private ProjectService projectService;
    @Value("${jasperReport}")
    private String reportPath;
    @Value("${pdfFile}")
    private String savedPdfPath;

    @GetMapping("/pdfReport")
    public void generateReport() {
        try {
            jasperDesign = JRXmlLoader.load(reportPath);
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, params, getDataSource());
            JasperExportManager.exportReportToPdfFile(jasperPrint, savedPdfPath);
        } catch (JRException e) {
            logger.error(("Something went wrong when generating the pdf: " + e.getMessage()));
        }
    }

    private JRDataSource getDataSource() {
        return new JRBeanCollectionDataSource(projectService.findAll());
    }
}
