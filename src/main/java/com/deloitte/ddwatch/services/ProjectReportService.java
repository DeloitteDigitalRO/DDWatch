package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.repositories.MetricsReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProjectReportService {

    @Autowired
    private MetricsReportRepository metricsReportRepository;

    @Autowired
    private ModelMapper modelMapper;

}
