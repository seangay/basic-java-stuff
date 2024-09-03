package seek;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.Resource;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TrafficCounter {
    private final List<Pair<LocalDateTime, Integer>> trafficData = new ArrayList<>();
    private int totalCount = 0;
    private int dailyCount = 0;
    private LocalDateTime lastProcessedDate;
    private final List<Pair<LocalDate, Integer>> dailyCounts = new ArrayList<>();


    public TrafficCounter() {
    }

    public void process(@Nonnull final Resource resource) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
        ) {
            var records = CSVFormat.TDF.parse(reader);
            for (CSVRecord record : records) {
                var countDate = convertIsoStringToDate(record.get(0));
                var count = Integer.parseInt(record.get(1));
                addRecord(countDate, count);
            }
        } catch (IOException e) {
            throw new TrafficCounterException("Unable to read data.");
        }
    }

    private LocalDateTime convertIsoStringToDate(@Nonnull final String isoString) {
        return LocalDateTime.parse(isoString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * There is an assumption that records will be added in date-order as supplied by the machine.
     */
    void addRecord(LocalDateTime date, int count) {
        trafficData.add(Pair.of(date, count));
        totalCount += count;
        dailyCount += count;

        if (lastProcessedDate != null && date.getDayOfMonth() != lastProcessedDate.getDayOfMonth()) {
            dailyCounts.add(Pair.of(lastProcessedDate.toLocalDate(), dailyCount));
            dailyCount = 0;
        }
        lastProcessedDate = date;
    }

    public List<Pair<LocalDateTime, Integer>> getTopPeriods(int recordsToReturn) {
        var sortedByCount = new ArrayList<>(trafficData);
        sortedByCount.sort((pair1, pair2) -> pair2.getRight().compareTo(pair1.getRight()));

        if (recordsToReturn > sortedByCount.size()) {
            return sortedByCount;
        }
        return sortedByCount.subList(0, recordsToReturn);
    }

    public Pair<LocalDateTime, Integer> getPeriodWithLeastTraffic() {
        int currentMin = Integer.MAX_VALUE;
        Pair<LocalDateTime, Integer> lowestPeriod = null;
        var trafficDataSize = trafficData.size();

        for (int i = 0; i < trafficDataSize; i++) {
            LocalDateTime startDate = trafficData.get(i).getLeft();
            LocalDateTime endDate = startDate.plusMinutes(90);
            int currentCount = 0;

            for (int j = i; j < trafficDataSize; j++) {
                var slidingWindowDate = trafficData.get(j).getLeft();
                if (slidingWindowDate.equals(endDate) || slidingWindowDate.isBefore(endDate)) {
                    currentCount += trafficData.get(j).getRight();
                } else {
                    break;
                }
            }

            if (currentCount < currentMin) {
                currentMin = currentCount;
                lowestPeriod = Pair.of(startDate, currentMin);
            }
        }
        return lowestPeriod;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public List<Pair<LocalDate, Integer>> getDailyCounts() {
        return dailyCounts;
    }

    public static class TrafficCounterException extends RuntimeException {
        public TrafficCounterException(String message) {
            super(message);
        }
    }
}
