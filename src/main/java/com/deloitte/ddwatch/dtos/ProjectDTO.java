package com.deloitte.ddwatch.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.andreinc.jbvext.annotations.misc.OneOfStrings;
import net.andreinc.jbvext.annotations.str.Alphanumeric;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ProjectDTO implements Serializable {
    String id;

    @NotNull
    @Size(min = 2, max = 64)
    String name;

    @Size(max=1024)
    String description;

    @Size(max=64)
    String deliveryLead;

    @Email
    @Size(max=128)
    String deliveryLeadEmail;

    @Size(max=64)
    String technicalLead;

    @Email
    @Size(max=128)
    String technicalLeadEmail;

    @OneOfStrings({"R", "G", "A"})
    String deliveryStatus;

    @OneOfStrings({"R", "G", "A"})
    String qualityStatus;

    List<SonarQubeReportDTO> sonarQubeReports;
    LocalDateTime lastQualityReport;

//    Set<DeliveryReportDTO> deliveryReports;
    LocalDateTime lastDeliveryReport;

    List<@Alphanumeric @Size(max=32) String> tagNames;

    @URL
    @Size(max = 1024)
    String sonarQubeUrl;

    @Size(max = 256)
    String sonarComponentKey;
}
