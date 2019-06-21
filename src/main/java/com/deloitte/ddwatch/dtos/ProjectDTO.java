package com.deloitte.ddwatch.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.andreinc.jbvext.annotations.misc.OneOfStrings;
import net.andreinc.jbvext.annotations.str.Alphanumeric;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class ProjectDTO implements Serializable {

    private String id;

    @NotNull
    @Size(min = 2, max = 64)
    private String name;

    @Size(max=1024)
    private String description;

    @Size(max = 64)
    private String deliveryLead;

    @Email
    @Size(min = 6, max=128)
    private String deliveryLeadEmail;

    @Size(min =3, max=64)
    private String technicalLead;

    @Email
    @Size(min=6, max=128)
    private String technicalLeadEmail;

    @OneOfStrings({"RED", "GREEN", "AMBER"})
    private String deliveryStatus;

    @OneOfStrings({"RED", "GREEN", "AMBER"})
    private String qualityStatus;

    private LocalDateTime lastQualityReport;

    private List<@Valid DeliveryReportDTO> deliveryReports;

    private LocalDateTime lastDeliveryReport;

    private Set<@Valid ProjectRepoDTO> projectRepos;

    private Set<@Alphanumeric @Size(min = 2, max=32) String> tags;

}
