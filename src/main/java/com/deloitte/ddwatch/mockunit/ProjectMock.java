package com.deloitte.ddwatch.mockunit;

import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.model.SonarQubeReport;
import com.deloitte.ddwatch.model.Status;
import net.andreinc.mockneat.abstraction.MockUnit;
import net.andreinc.mockneat.unit.seq.LongSeq;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.function.Supplier;

import static net.andreinc.mockneat.types.enums.StringType.LETTERS;
import static net.andreinc.mockneat.unit.networking.URLs.urls;
import static net.andreinc.mockneat.unit.objects.Filler.filler;
import static net.andreinc.mockneat.unit.objects.From.from;
import static net.andreinc.mockneat.unit.text.Formatter.fmt;
import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static net.andreinc.mockneat.unit.user.Emails.emails;
import static net.andreinc.mockneat.unit.user.Names.names;

public class ProjectMock implements MockUnit<Project> {

    public static final LongSeq projectIdGen = LongSeq.longSeq().increment(10);
    public static final LongSeq qualityReportIdGen = LongSeq.longSeq().increment(10);
    public static final LongSeq sonarReportIdGen = LongSeq.longSeq().increment(10);


    @Override
    public Supplier<Project> supplier() {

        MockUnit<LocalDateTime> thisMonth = localDates()
                                            .thisMonth()
                                            .map(v -> LocalDateTime.of(v, LocalTime.of(0,0)));

        MockUnit<LocalDateTime> thisYear = localDates()
                                            .thisYear()
                                            .map(v -> LocalDateTime.of(v, LocalTime.of(0,0)));

        long pId = projectIdGen.get();

        return filler(Project::new)
                .setter(Project::setId, () -> () -> pId)
                .setter(Project::setName,
                        fmt("P#{number}#{code}")
                                .param("number", ints().range(0, 1000))
                                .param("code", strings().size(3).type(LETTERS))
                )
                .setter(Project::setDeliveryLead, names().full())
                .setter(Project::setDeliveryLeadEmail, emails())
//                .setter(Project::setDescription, markovs().loremIpsum())
                .setter(Project::setDeliveryStatus, from(Status.class))
                .setter(Project::setQualityStatus, from(Status.class))
                .setter(Project::setLastQualityReport, thisMonth)
                .setter(Project::setLastDeliveryReport, thisMonth)
                .setter(Project::setSonarQubeUrl, urls().append("/sonarqube/"))
                .map(p -> {
                    p.setSonarQubeUrl(p.getName());
                    return p;
                })
                .map(p -> {
                    final long grId = qualityReportIdGen.get();
                    Set<QualityReport> qualityReports = filler(QualityReport::new)
                                                            .constant(QualityReport::setProject, p)
                                                            .constant(QualityReport::setId, grId)
                                                            .setter(QualityReport::setUpdateDate, thisYear)
                                                            .map(qr -> {
                                                                qr.setSonarQubeReport(
                                                                        filler(SonarQubeReport::new)
                                                                                .setter(SonarQubeReport::setId, sonarReportIdGen)
                                                                                .constant(SonarQubeReport::setQualityReport, qr)
                                                                                .constant(SonarQubeReport::setName, p.getName())
                                                                                .constant(SonarQubeReport::setKey, p.getSonarComponentKey())
                                                                                .setter(SonarQubeReport::setBlockerBugs, ints().range(0, 5))
                                                                                .setter(SonarQubeReport::setCriticalBugs, ints().range(0, 10))
                                                                                .setter(SonarQubeReport::setMajorBugs, ints().range(0, 40))
                                                                                .setter(SonarQubeReport::setMinorBugs, ints().range(0, 50))
                                                                                .setter(SonarQubeReport::setOtherBugs, ints().range(0, 60))
                                                                                .setter(SonarQubeReport::setLinesOfCode, ints().range(10000, 50000))
                                                                                .setter(SonarQubeReport::setBlockerVulnerabilities, ints().range(0, 5))
                                                                                .setter(SonarQubeReport::setBlockerVulnerabilities, ints().range(0, 10))
                                                                                .setter(SonarQubeReport::setCriticalVulnerabilities, ints().range(0, 40))
                                                                                .setter(SonarQubeReport::setMajorVulnerabilities, ints().range(0, 50))
                                                                                .map(sqr -> {
                                                                                    // Return total number of bugs
                                                                                    // Return total number of vulnerabilities
                                                                                    // Return total number of issues
                                                                                    return sqr;

                                                                                })
                                                                                .get()
                                                                );
                                                                return qr;
                                                            })
                                                            .set(10)
                                                            .get();
                    p.setQualityReports(qualityReports);
                    return p;
                })
                .supplier();
    }



    public static void main(String[] args) {
        ProjectMock pm = new ProjectMock();

        pm.list(10).consume(System.out::println);
    }
}
