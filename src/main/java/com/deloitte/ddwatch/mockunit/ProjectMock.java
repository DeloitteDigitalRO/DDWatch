package com.deloitte.ddwatch.mockunit;

import com.deloitte.ddwatch.model.*;
import net.andreinc.mockneat.abstraction.MockUnit;
import net.andreinc.mockneat.abstraction.MockUnitDouble;
import net.andreinc.mockneat.abstraction.MockUnitInt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.*;
import java.util.function.Supplier;

import static net.andreinc.mockneat.types.enums.StringType.LETTERS;
import static net.andreinc.mockneat.unit.objects.Filler.filler;
import static net.andreinc.mockneat.unit.objects.From.from;
import static net.andreinc.mockneat.unit.text.Formatter.fmt;
import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static net.andreinc.mockneat.unit.types.Doubles.doubles;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static net.andreinc.mockneat.unit.user.Emails.emails;
import static net.andreinc.mockneat.unit.user.Names.names;

public class ProjectMock implements MockUnit<Project> {
    // Dates related
    public static final MockUnit<LocalDateTime> thisMonth = localDates()
            .past(LocalDate.now().minus(Period.ofMonths(1)))
            .map(v -> LocalDateTime.of(v, LocalTime.of(0,0)));

    public static final LocalDateTime lastMonth = LocalDateTime.now().minus(Period.ofMonths(1));

    public static final MockUnit<LocalDateTime> thisYear = localDates()
            .thisYear()
            .map(v -> LocalDateTime.of(v, LocalTime.of(0,0)));

    // Code issues related
    public static final MockUnitInt other = ints().range(0, 120);
    public static final MockUnitInt minor = ints().range(0, 100);
    public static final MockUnitInt major = ints().range(0, 50);
    public static final MockUnitInt critical = ints().range(0, 25);
    public static final MockUnitInt blocker = ints().range(0, 5);
    public static final List<String> possibleTags = Arrays.asList("hybris", "backend", "frontend", "scrum", "kanban", "uk", "de");

    // Metrics values
    private static final MockUnitDouble numericValues = doubles().range(30, 101);
    private static final List<Status> metricStatuses = Arrays.asList(Status.AMBER, Status.GREEN, Status.RED);
    private static final List<String> invoicingValues = Arrays.asList(
            "Invoices outstanding for more than 60 days",
            "Invoices outstanding between 31-60 days",
            "Invoices outstanding between 31-60 days"
    );
    private static final List<String> changeOrderValues = Arrays.asList(
            "Covers the current day",
            "Expire in the following 30 days",
            "Is expired"
    );

    @Override
    public Supplier<Project> supplier() {

        return filler(Project::new)
                .setter(Project::setName,
                        fmt("P#{number}#{code}")
                                .param("number", ints().range(0, 1000))
                                .param("code", strings().size(3).type(LETTERS))
                )
                .setter(Project::setDeliveryLead, names().full())
                .setter(Project::setDeliveryLeadEmail, emails().domain("deloittece.com"))
                .setter(Project::setTechnicalLead, names().full())
                .setter(Project::setTechnicalLeadEmail, emails().domain("deloittece.com"))
                .constant(Project::setDescription, "Project description.")
                .setter(Project::setDeliveryStatus, from(Status.class))
                .setter(Project::setQualityStatus, from(Status.class))
                .setter(Project::setLastQualityReport, thisMonth)
                .setter(Project::setLastDeliveryReport, thisMonth)
                .map(p -> {
                    List<DeliveryReport> deliveryReports = filler(DeliveryReport::new)
                            .constant(DeliveryReport::setProject, p)
                            .setter(DeliveryReport::setUpdateDate, thisMonth)
                            .map(thisDeliveryReport -> {
                                thisDeliveryReport.setMetricsReport(filler(MetricsReport::new)
                                        .constant(MetricsReport::setCreatedOn, lastMonth)
                                        .setter(MetricsReport::setDeliveryValue, numericValues)
                                        .setter(MetricsReport::setDeliveryStatus, from(metricStatuses))
                                        .setter(MetricsReport::setInvoicingValue, from(invoicingValues))
                                        .setter(MetricsReport::setInvoicingStatus, from(metricStatuses))
                                        .setter(MetricsReport::setChangeOrderValue, from(changeOrderValues))
                                        .setter(MetricsReport::setChangeOrderStatus, from(metricStatuses))
                                        .get());

                                return thisDeliveryReport;

                            })
                            .list(5)
                            .get();
                    p.setDeliveryReports(deliveryReports);


                    Set<ProjectRepo> projectRepos = filler(ProjectRepo::new)
                            .constant(ProjectRepo::setProject, p)
                            .constant(ProjectRepo::setName, "Project Repo")
                            .constant(ProjectRepo::setIsDefault, false)
                            .constant(ProjectRepo::setSonarQubeUrl,"http://localhost:9000")
                            .constant(ProjectRepo::setSonarComponentKey, "com.deloitte:ddwatch")
                            .constant(ProjectRepo::setUrl, "https://github.com/DeloitteDigitalRO/DDWatch/")
                            .map(projectRepo -> {
                                Set<QualityReport> qualityReports = filler(QualityReport::new)
                                    .constant(QualityReport::setProjectRepo, projectRepo)
                                    .setter(QualityReport::setUpdateDate, thisYear)
                                    .map(thisQualityReport -> {
                                        thisQualityReport.setSonarQubeReport(
                                            filler(SonarQubeReport::new)
                                                .constant(SonarQubeReport::setQualityReport, thisQualityReport)
                                                .constant(SonarQubeReport::setName, p.getName())
                                                .constant(SonarQubeReport::setKey, projectRepo.getSonarComponentKey())
                                                .setter(SonarQubeReport::setLinesOfCode, ints().range(10000, 50000))

                                                // Bugs
                                                .setter(SonarQubeReport::setBlockerBugs, blocker)
                                                .setter(SonarQubeReport::setCriticalBugs, critical)
                                                .setter(SonarQubeReport::setMajorBugs, major)
                                                .setter(SonarQubeReport::setMinorBugs, minor)
                                                .setter(SonarQubeReport::setOtherBugs, other)

                                                // Vulnerabilities
                                                .setter(SonarQubeReport::setBlockerVulnerabilities, blocker)
                                                .setter(SonarQubeReport::setCriticalVulnerabilities, critical)
                                                .setter(SonarQubeReport::setMajorVulnerabilities, major)
                                                .setter(SonarQubeReport::setMinorVulnerabilities, minor)
                                                .setter(SonarQubeReport::setOtherVulnerabilities, other)

                                                // Code Smells
                                                .setter(SonarQubeReport::setBlockerCodeSmells, blocker)
                                                .setter(SonarQubeReport::setCriticalCodeSmells, critical)
                                                .setter(SonarQubeReport::setMajorCodeSmells, major)
                                                .setter(SonarQubeReport::setMinorCodeSmells, minor)
                                                .setter(SonarQubeReport::setOtherCodeSmells, other)

                                                // Duplication
                                                .setter(SonarQubeReport::setDuplicatedLines, ints().range(500, 5000))
                                                .setter(SonarQubeReport::setDuplicatedBlocks, ints().range(10, 20))
                                                .setter(SonarQubeReport::setDefectDensity, doubles().range(1,2))

                                                // Complexities
                                                .setter(SonarQubeReport::setCyclomaticComplexity, ints().range(1000, 5000))
                                                .setter(SonarQubeReport::setCognitiveComplexity, ints().range(1000, 5000))

                                                // Coverage
                                                .setter(SonarQubeReport::setOverallCoverage, doubles().range(10, 90))
                                                .setter(SonarQubeReport::setLineCoverage, doubles().range(10, 90))
                                                .setter(SonarQubeReport::setConditionsCoverage, doubles().range(10, 90))

                                                .map(sqr -> {
                                                    // Return total number of bugs
                                                    sqr.setTotalBugs(
                                                            sqr.getBlockerBugs() +
                                                                    sqr.getCriticalBugs() +
                                                                    sqr.getMajorBugs() +
                                                                    sqr.getMinorBugs() +
                                                                    sqr.getOtherBugs()
                                                    );
                                                    // Return total number of vulnerabilities
                                                    sqr.setTotalVulnerabilities(
                                                            sqr.getBlockerVulnerabilities() +
                                                                    sqr.getCriticalVulnerabilities() +
                                                                    sqr.getMajorVulnerabilities() +
                                                                    sqr.getMinorVulnerabilities() +
                                                                    sqr.getOtherVulnerabilities()
                                                    );
                                                    // Return total number of issues
                                                    sqr.setTotalCodeSmells(
                                                            sqr.getBlockerCodeSmells() +
                                                                    sqr.getCriticalCodeSmells() +
                                                                    sqr.getMajorCodeSmells() +
                                                                    sqr.getMinorCodeSmells() +
                                                                    sqr.getOtherBugs()
                                                    );
                                                    // Rentru total number of issues
                                                    sqr.setTotalIssues(
                                                            sqr.getTotalBugs() +
                                                                    sqr.getTotalVulnerabilities() +
                                                                    sqr.getTotalCodeSmells()
                                                    );

                                                    return sqr;

                                                })
                                                .get()
                                            );
                                        return thisQualityReport;
                                        })
                                        .set(10)
                                        .get();

                                projectRepo.setQualityReports(qualityReports);
                                return projectRepo;
                            })
                            .set(5)
                            .get();

                    p.setProjectRepos(projectRepos);
                    return p;
                })
                .supplier();
    }

    public static Set<Tag> generateTags() {
        return filler(Tag::new).setter(Tag::setName, from(possibleTags))
                .set(4)
                .get();
    }
}
