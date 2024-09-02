package codility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class LongestZigZagLengthTest {
    private LongestZigZagLength.Solution solution;

    @BeforeEach
    public void beforeAll() {
        solution = new LongestZigZagLength.Solution();
    }

    @ParameterizedTest(name = "Tree[{0}] should have ZigZag length of {1}")
    @MethodSource
    public void solution_calculatesExpectedZigZagLength(LongestZigZagLength.Tree tree, int expectedLength) {
        var longestZigZag = solution.solution(tree);

        assertThat("Unexpected longestZigZag value", longestZigZag, is(equalTo(expectedLength)));
    }

    private static Stream<Arguments> solution_calculatesExpectedZigZagLength() {
        return Stream.of(
                Arguments.of(TreeParser.parseTree(
                         "(5, (3, (20, (6, None, None), None), None), (10, (1, None, None), (15, (30, None, (9, None, None)), (8, None, None))))"
                ), 2)
        );
    }

    private static class TreeParser {
        public static LongestZigZagLength.Tree parseTree(String data) {
            if (data.equals("None")) {
                return null;
            }
            // Remove outer parentheses
            data = data.substring(1, data.length() - 1);

            return parseTreeHelper(data);
        }

        private static LongestZigZagLength.Tree parseTreeHelper(String data) {
            int firstComma = findCommaOutsideParentheses(data);
            int secondComma = findCommaOutsideParentheses(data.substring(firstComma + 1)) + firstComma + 1;

            String rootValue = data.substring(0, firstComma).trim();
            String leftTree = data.substring(firstComma + 1, secondComma).trim();
            String rightTree = data.substring(secondComma + 1).trim();

            LongestZigZagLength.Tree root = new LongestZigZagLength.Tree();
            root.x = Integer.parseInt(rootValue);
            root.l = parseTree(leftTree);
            root.r = parseTree(rightTree);

            return root;
        }

        private static int findCommaOutsideParentheses(String data) {
            int parenthesesDepth = 0;
            for (int i = 0; i < data.length(); i++) {
                if (data.charAt(i) == '(') {
                    parenthesesDepth++;
                } else if (data.charAt(i) == ')') {
                    parenthesesDepth--;
                } else if (data.charAt(i) == ',' && parenthesesDepth == 0) {
                    return i;
                }
            }
            return -1; // Should never reach here if input format is correct
        }
    }

}
