package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.model.SonarQubeReport;
import com.deloitte.ddwatch.model.Status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QualityReportServiceTest {

    private static final String LOCALHOST_URL = "http://localhost:9000";
    private static final String COMPONENT_KEY = "com.deloitte:ddwatch";

    @InjectMocks
    private QualityReportService qualityReportService;

    @Mock
    private SonarQubeReportService sonarQubeReportService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createSonarQubeReport() {
        SonarQubeReport sonarQubeReport = new SonarQubeReport();
        sonarQubeReport.setOverallCoverage(2.5);
        sonarQubeReport.setDefectDensity(1D);

        when(sonarQubeReportService.createReportFromUrl(LOCALHOST_URL, COMPONENT_KEY))
                .thenReturn(sonarQubeReport);

        QualityReport qualityReport = qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
        assertEquals(qualityReport.getQualityStatus(), Status.RED);

        sonarQubeReport.setOverallCoverage(50D);
        qualityReport = qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
        assertEquals(qualityReport.getQualityStatus(), Status.AMBER);

        sonarQubeReport.setOverallCoverage(60D);
        qualityReport = qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
        assertEquals(qualityReport.getQualityStatus(), Status.GREEN);

        sonarQubeReport.setOverallCoverage(10D);
        sonarQubeReport.setDefectDensity(2D);
        qualityReport = qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
        assertEquals(qualityReport.getQualityStatus(), Status.RED);

        sonarQubeReport.setOverallCoverage(50D);
        sonarQubeReport.setDefectDensity(2D);
        qualityReport = qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
        assertEquals(qualityReport.getQualityStatus(), Status.AMBER);

        sonarQubeReport.setOverallCoverage(60D);
        sonarQubeReport.setDefectDensity(2D);
        qualityReport = qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
        assertEquals(qualityReport.getQualityStatus(), Status.AMBER);

        sonarQubeReport.setOverallCoverage(10D);
        sonarQubeReport.setDefectDensity(10D);
        qualityReport = qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
        assertEquals(qualityReport.getQualityStatus(), Status.RED);

        sonarQubeReport.setOverallCoverage(50D);
        sonarQubeReport.setDefectDensity(10D);
        qualityReport = qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
        assertEquals(qualityReport.getQualityStatus(), Status.RED);

        sonarQubeReport.setOverallCoverage(60D);
        sonarQubeReport.setDefectDensity(10D);
        qualityReport = qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
        assertEquals(qualityReport.getQualityStatus(), Status.RED);
    }

    @Test(expected = RuntimeException.class)
    public void createSonarQubeReporWhenCoverageBigShouldThrowException() {
        SonarQubeReport sonarQubeReport = new SonarQubeReport();
        sonarQubeReport.setOverallCoverage(100D);

        when(sonarQubeReportService.createReportFromUrl(LOCALHOST_URL, COMPONENT_KEY))
                .thenReturn(sonarQubeReport);

        qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
    }

    @Test(expected = RuntimeException.class)
    public void createSonarQubeReporWhenCoverageZeroShouldThrowException() {
        SonarQubeReport sonarQubeReport = new SonarQubeReport();
        sonarQubeReport.setOverallCoverage(0D);

        when(sonarQubeReportService.createReportFromUrl(LOCALHOST_URL, COMPONENT_KEY))
                .thenReturn(sonarQubeReport);

        qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
    }

    @Test(expected = RuntimeException.class)
    public void createSonarQubeReporWhenDensityBigShouldThrowException() {
        SonarQubeReport sonarQubeReport = new SonarQubeReport();
        sonarQubeReport.setDefectDensity(100D);

        when(sonarQubeReportService.createReportFromUrl(LOCALHOST_URL, COMPONENT_KEY))
                .thenReturn(sonarQubeReport);

        qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
    }

    @Test(expected = RuntimeException.class)
    public void createSonarQubeReporWhenDensityZeroShouldThrowException() {
        SonarQubeReport sonarQubeReport = new SonarQubeReport();
        sonarQubeReport.setDefectDensity(0D);

        when(sonarQubeReportService.createReportFromUrl(LOCALHOST_URL, COMPONENT_KEY))
                .thenReturn(sonarQubeReport);

        qualityReportService.create(LOCALHOST_URL, COMPONENT_KEY);
    }

    @Test
    public void createQualityReportWithStream() throws IOException {
        String file = "report.xls";
        InputStream fileStream = new ByteArrayInputStream(file.getBytes());
        QualityReport qualityReport = new QualityReport();
        SonarQubeReport sonarQubeReport = new SonarQubeReport();
        sonarQubeReport.setOverallCoverage(2.5);
        sonarQubeReport.setDefectDensity(1D);

        when(sonarQubeReportService.createReportFromFile(fileStream))
                .thenReturn(sonarQubeReport);

        qualityReport = qualityReportService.create(fileStream, qualityReport);
        assertEquals(qualityReport.getQualityStatus(), Status.RED);
    }
}
