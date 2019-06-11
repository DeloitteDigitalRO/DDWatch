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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QualityReportServiceTest {

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
        sonarQubeReport.setDefectDensity(3.5);

        when(sonarQubeReportService.createReportFromUrl("http://localhost:9000", "com.deloitte:ddwatch"))
                .thenReturn(sonarQubeReport);

        QualityReport qualityReport = qualityReportService.create("http://localhost:9000", "com.deloitte:ddwatch");
        assertEquals(qualityReport.getQualityStatus(), Status.RED);
    }



}
