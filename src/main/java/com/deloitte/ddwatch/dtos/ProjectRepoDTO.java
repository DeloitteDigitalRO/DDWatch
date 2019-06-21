package com.deloitte.ddwatch.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class ProjectRepoDTO implements Serializable {
    private Long id;

    private Long projectId;

    private String name;

    private Boolean isDefault;

    @URL
    @Size(max = 1024)
    private String sonarQubeUrl;

    @URL
    @Size(max = 1024)
    private String url;

    @Size(max = 256)
    private String sonarComponentKey;

    private Set<@Valid QualityReportDTO> qualityReports;
}
