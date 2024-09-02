package codility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ParkingBillTest {
    private ParkingBill.Solution solution;

    @BeforeEach
    public void beforeAll() {
        solution = new ParkingBill.Solution();
    }

    @ParameterizedTest(name = "Duration[{0} - {1}] should cost {2}")
    @MethodSource
    public void solution_calculatesCostCorrectly(String entryTime, String exitTime, int expectedCost) {
        var totalCost = solution.solution(entryTime, exitTime);

        assertThat("Unexpected total cost", totalCost, is(equalTo(expectedCost)));
    }

    private static Stream<Arguments> solution_calculatesCostCorrectly() {
        return Stream.of(
                Arguments.of("10:00", "13:21", 17),
                Arguments.of("09:42", "11:42", 9),
                Arguments.of("23:11", "23:59", 5),
                Arguments.of("00:00", "23:59", 97),
                Arguments.of("12:00", "12:59", 5),
                Arguments.of("12:00", "13:01", 9),
                Arguments.of("12:59", "16:37", 17),
                Arguments.of("03:03", "15:59", 53)
        );
    }
}
