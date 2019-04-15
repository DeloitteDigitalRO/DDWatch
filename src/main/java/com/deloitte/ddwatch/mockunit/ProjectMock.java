package com.deloitte.ddwatch.mockunit;

import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.Status;
import net.andreinc.mockneat.abstraction.MockUnit;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Supplier;

import static net.andreinc.mockneat.unit.objects.Filler.filler;
import static net.andreinc.mockneat.unit.objects.From.from;
import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static net.andreinc.mockneat.unit.user.Emails.emails;
import static net.andreinc.mockneat.unit.user.Names.names;

public class ProjectMock implements MockUnit<Project> {

    public static final String[] PROJECT_NAMES = { "Hornet", "Leap", "Blackjack"};

    @Override
    public Supplier<Project> supplier() {

        MockUnit<LocalDateTime> thisMonth = localDates()
                                            .thisMonth()
                                            .map(v -> LocalDateTime.of(v, LocalTime.of(0,0)));


        return filler(() -> new Project())
                .setter(Project::setName, from(PROJECT_NAMES))
                .setter(Project::setDeliveryLead, names().full())
                .setter(Project::setDeliveryLeadEmail, emails())
//                .setter(Project::setDescription, markovs().loremIpsum())
                .setter(Project::setDeliveryStatus, from(Status.class))
                .setter(Project::setQualityStatus, from(Status.class))
                .setter(Project::setLastQualityReport, thisMonth)
                .setter(Project::setLastDeliveryReport, thisMonth)
//                .setter(Project::setTags, words().nouns().list(10))
                .supplier();
    }

    public static void main(String[] args) {
        ProjectMock pm = new ProjectMock();

        Project p = pm.get();


        List<Project> plist = pm.list(10).get();

        pm.map(tp -> { tp.setName("ABC"); return tp;}).list(10).consume(System.out::println);
    }
}
