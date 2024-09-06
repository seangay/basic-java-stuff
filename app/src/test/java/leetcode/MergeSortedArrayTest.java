package leetcode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MergeSortedArrayTest {
    private MergeSortedArray mergeSortedArray;

    @BeforeEach
    public void beforeEach() {
        mergeSortedArray = new MergeSortedArray();
    }

    @ParameterizedTest(name = "nums1 = {0} | m = {1} | nums2 = {2} | n = {3}  || expected = {4}")
    @MethodSource
    public void merge_matches_expected(int[] nums1, int m, int[] nums2, int n, int[] expectedOutput) {
        mergeSortedArray.merge(nums1, m, nums2, n);
        assertThat("nums1 contained unexpected values", nums1, is(equalTo(expectedOutput)));
    }

    private static Stream<Arguments> merge_matches_expected() {
        return Stream.of(
                Arguments.of(new int[]{ 1, 2, 3, 0, 0, 0 },
                        3,
                        new int[]{ 2, 5, 6 },
                        3,
                        new int[]{ 1, 2, 2, 3, 5, 6 }
                ),
                Arguments.of(new int[]{ 1 },
                        1,
                        new int[]{ },
                        0,
                        new int[]{ 1 }
                ),
                Arguments.of(new int[]{ 0 },
                        0,
                        new int[]{ 1 },
                        1,
                        new int[]{ 1 }
                ),
                Arguments.of(new int[]{ 0, 0, 3, 0, 0, 0, 0, 0, 0 },
                        3,
                        new int[]{ -1, 1, 1, 1, 2, 3 },
                        6,
                        new int[]{ -1, 0, 0, 1, 1, 1, 2, 3, 3 }
                )
        );
    }
}
