package com.deloitte.ddwatch.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ProjectRepoDTO implements Serializable {

    private Long projectId;

    String name;

    Boolean isDefault;

    @URL
    @Size(max = 1024)
    String sonarQubeUrl;

    @Size(max = 256)
    String sonarComponentKey;

    List<@Valid QualityReportDTO> qualityReports;

}
