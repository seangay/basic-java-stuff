package codility;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;


/**
 * See <a href="https://app.codility.com/programmers/trainings/5/parking_bill/">the Parking Bill</a> exercise on
 * https://app.codility.com/programmers for details.
 * <br /><br />
 * You parked your car in a parking lot and want to compute the total cost of the ticket. The billing rules are as follows:
 * <br /><br />
 * The entrance fee of the car parking lot is 2;
 * The first full or partial hour costs 3;
 * Each successive full or partial hour (after the first) costs 4.
 * You entered the car parking lot at time E and left at time L. In this task, times are represented as strings in the format "HH:MM" (where "HH" is a two-digit number between 0 and 23, which stands for hours, and "MM" is a two-digit number between 0 and 59, which stands for minutes).
 * <br /><br />
 * Write a function:
 * <br /><br />
 * <code>class Solution { public int solution(String E, String L); }</code>
 * <br /><br />
 * that, given strings E and L specifying points in time in the format "HH:MM", returns the total cost of the parking bill from your entry at time E to your exit at time L. You can assume that E describes a time before L on the same day.
 * <br /><br />
 * For example, given "10:00" and "13:21" your function should return 17, because the entrance fee equals 2, the first hour costs 3 and there are two more full hours and part of a further hour, so the total cost is 2 + 3 + (3 * 4) = 17. Given "09:42" and "11:42" your function should return 9, because the entrance fee equals 2, the first hour costs 3 and the second hour costs 4, so the total cost is 2 + 3 + 4 = 9.
 * *<br /><br />
 * Assume that:
 * *<br /><br />
 * strings E and L follow the format "HH:MM" strictly;
 * string E describes a time before L on the same day.
 * In your solution, focus on correctness. The performance of your solution will not be the focus of the assessment. *
 */
public class ParkingBill {
    public static void main(String[] args) {
        var solution = new Solution();

        var testValues = new String[][]{
                new String[]{ "10:00", "13:21" }, // 17
                new String[]{ "09:42", "11:42" }, // 9
                new String[]{ "23:11", "23:59" }, // 5
                new String[]{ "00:00", "23:59" }, // 97
                new String[]{ "12:00", "12:59" }, // 5
                new String[]{ "12:00", "13:01" }, // 9
                new String[]{ "12:59", "16:37" }, // 17
                new String[]{ "03:03", "15:59" }, // 53

        };

        for (String[] testValue : testValues) {
            System.out.println("testValue = " + Arrays.toString(testValue));
            var totalCost = solution.solution(testValue[0], testValue[1]);
            System.out.println("totalCost = " + totalCost);
        }
    }

    private static class Solution {
        private static final int HOURLY_COST = 4;
        private static final int ENTRY_FEE = 2;
        private static final int FIRST_HOUR_COST = 3;
        private static final int INITIAL_FEE = ENTRY_FEE + FIRST_HOUR_COST;
        private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
        private static final float MINUTES_PER_HOUR = 60f;

        public int solution(String entryTime, String exitTime) {
            var totalHours = totalHours(entryTime, exitTime);
            return calculateCost(totalHours);
        }

        private float totalHours(String entryTimeAsString, String exitTimeAsString) {
            var entryTime = LocalTime.parse(entryTimeAsString, DATE_TIME_FORMATTER);
            var exitTime = LocalTime.parse(exitTimeAsString, DATE_TIME_FORMATTER);

            var totalDurationOfStay = Duration.between(entryTime, exitTime);
            return totalDurationOfStay.toMinutes() / MINUTES_PER_HOUR;
        }

        private int calculateCost(float totalHours) {
            var totalCost = INITIAL_FEE;

            //precision isn't necessary here as we are rounding up, so the cast is just for simplicity
            int totalBillableHours = (int) (Math.ceil(totalHours) - 1);
            if (totalBillableHours >= 1) {
                totalCost += totalBillableHours * HOURLY_COST;
            }
            return totalCost;
        }
    }
}
