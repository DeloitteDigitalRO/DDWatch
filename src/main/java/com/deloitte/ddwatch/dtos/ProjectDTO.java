package com.deloitte.ddwatch.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.andreinc.jbvext.annotations.misc.OneOfStrings;
import net.andreinc.jbvext.annotations.str.Alphanumeric;
import org.hibernate.validator.constraints.URL;

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
    String id;

    @NotNull
    @Size(min = 2, max = 64)
    String name;

    @Size(max=1024)
    String description;

    @Size(max = 64)
    String deliveryLead;

    @Email
    @Size(min = 6, max=128)
    String deliveryLeadEmail;

    @Size(min =3, max=64)
    String technicalLead;

    @Email
    @Size(min=6, max=128)
    String technicalLeadEmail;

    @OneOfStrings({"RED", "GREEN", "AMBER"})
    String deliveryStatus;

    @OneOfStrings({"RED", "GREEN", "AMBER"})
    String qualityStatus;

    List<@Valid QualityReportDTO> qualityReports;
    LocalDateTime lastQualityReport;

    Set<@Valid DeliveryReportDTO> deliveryReports;
    LocalDateTime lastDeliveryReport;

    Set<@Valid MetricsReportDTO> metricsReports;

    Set<@Alphanumeric @Size(min = 2, max=32) String> tags;

    @URL
    @Size(max = 1024)
    String sonarQubeUrl;

    @Size(max = 256)
    String sonarComponentKey;
}
