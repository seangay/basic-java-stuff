package seek;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringJUnitConfig
@ContextConfiguration(classes = { TrafficCounterTest.Config.class })
public class TrafficCounterTest {
    @Configuration
    static class Config {
        /**
         * Known data set here that will contain expected data values as indicated below:
         * <br /><br />
         * <ul>
         *     <li>total count = 398</li>
         *     <li>Daily counts of [(2021-12-01,197), (2021-12-05,96), (2021-12-08,105)]</li>
         *     <li>Top periods [(2021-12-01T23:30,0), (2021-12-09T00:00,4), (2021-12-01T05:00,5)]</li>
         *     <li>Lowest 1.5 hour block starts with 2021-12-01T23:30 and has 0 cars</li>
         * </ul>
         *
         */
        @Bean
        public Resource trafficCounterTestData() {
            return new ClassPathResource("seek/CodingChallengeDataFile.csv");
        }
    }

    @Autowired
    private Resource trafficCounterTestData;

    private final TrafficCounter trafficCounter = new TrafficCounter();

    @Test
    public void processFileAndInterrogateReportableValues() {
        trafficCounter.process(trafficCounterTestData);
        var expectedTotal = 398;

        var totalCount = trafficCounter.getTotalCount();
        assertThat(
                "Incorrect total number of cars processed",
                totalCount,
                equalTo(expectedTotal)
        );

        var periodWithLeastTraffic = trafficCounter.getPeriodWithLeastTraffic();
        assertThat("Shouldn't get a null result based on test data", periodWithLeastTraffic, notNullValue());
        assertThat(
                "Incorrect count for least traffic period",
                periodWithLeastTraffic.getValue(),
                equalTo(0)
        );
        assertThat(
                "Incorrect Date for least traffic period",
                periodWithLeastTraffic.getKey().toString(),
                equalTo("2021-12-01T23:30")
        );

        var topPeriods = trafficCounter.getTopPeriods(3);
        assertThat("Top periods shouldn't be null", topPeriods, notNullValue());
        assertThat("Top periods returned the wrong number of items", topPeriods, hasSize(3));
        assertThat("Mismatch on top periods", topPeriods, hasItems(
                Pair.of(toLocalDateTime("2021-12-08T18:00:00"), 33),
                Pair.of(toLocalDateTime("2021-12-01T07:30:00"), 46),
                Pair.of(toLocalDateTime("2021-12-01T08:00:00"), 42)
        ));

        var dailyCounts = trafficCounter.getDailyCounts();
        assertThat("Daily counts shouldn't be null", dailyCounts, notNullValue());
        assertThat("Incorrect number of days processed", dailyCounts.size(), equalTo(3));
        assertThat("Mismatch on daily counts", dailyCounts, hasItems(
                Pair.of(toLocalDate("2021-12-01"), 197),
                Pair.of(toLocalDate("2021-12-05"), 96),
                Pair.of(toLocalDate("2021-12-08"), 105)
        ));
    }

    private LocalDateTime toLocalDateTime(String value) {
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private LocalDate toLocalDate(String value) {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
