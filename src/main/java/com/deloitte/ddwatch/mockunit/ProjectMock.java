package com.deloitte.ddwatch.mockunit;

import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.ProjectStatus;
import jdk.nashorn.internal.ir.PropertyKey;
import net.andreinc.mockneat.abstraction.MockUnit;
import net.andreinc.mockneat.abstraction.MockUnitLocalDate;
import net.andreinc.mockneat.unit.objects.Froms;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Supplier;

import static net.andreinc.mockneat.unit.objects.Filler.filler;
import static net.andreinc.mockneat.unit.objects.From.from;
import static net.andreinc.mockneat.unit.text.Markovs.markovs;
import static net.andreinc.mockneat.unit.text.Words.words;
import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static net.andreinc.mockneat.unit.user.Emails.emails;
import static net.andreinc.mockneat.unit.user.Names.names;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.regex;

public class ProjectMock implements MockUnit<Project> {

    public static final String[] PROJECT_NAMES = { "Hornet", "Leap", "Blackjack"};

    @Override
    public Supplier<Project> supplier() {

        MockUnit<LocalDateTime> thisMonth = localDates()
                                            .thisMonth()
                                            .map(v -> LocalDateTime.of(v, LocalTime.of(0,0))


        return filler(() -> new Project())
                .setter(Project::setName, from(PROJECT_NAMES))
                .setter(Project::setDeliveryLead, names().full())
                .setter(Project::setDeliveryLead, emails())
                .setter(Project::setDescription, markovs().loremIpsum())
                .setter(Project::setDeliveryStatus, from(ProjectStatus.class))
                .setter(Project::setQualityStatus, from(ProjectStatus.class))
                .setter(Project::setLastQualityReport, thisMonth)
                .setter(Project::setLastDeliveryReport, thisMonth)
                .setter(Project::setTags, words().nouns().list(10))
                .supplier();
    }

}
