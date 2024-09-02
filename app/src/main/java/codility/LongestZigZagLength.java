package codility;

import java.util.ArrayList;
import java.util.List;

/**
 * See <a href="https://app.codility.com/programmers/trainings/7/tree_longest_zig_zag/">the Tree Longest ZigZag</a>
 * exercise on <a href="https://app.codility.com/programmers">https://app.codility.com/programmers</a> for details.
 */
public class LongestZigZagLength {
    public static class Tree {
        int x;
        Tree l;
        Tree r;

        @Override
        public String toString() {
            return toStringHelper(this, 0);
        }

        private String toStringHelper(Tree node, int depth) {
            if (node == null) {
                return "----";
            }

            var sb = new StringBuilder();

            // Indentation based on the depth of the tree
            var indent = "  ".repeat(depth);

            sb.append(indent).append(node.x).append("\n");

            // Left subtree
            if (node.l != null) {
                sb.append(indent).append("L: ").append(toStringHelper(node.l, depth + 1));
            }

            // Right subtree
            if (node.r != null) {
                sb.append(indent).append("R: ").append(toStringHelper(node.r, depth + 1));
            }

            return sb.toString();
        }
    }

    static class Solution {
        public int solution(Tree T) {
            var treeWalker = new TreeWalker(T, false);
            treeWalker.walkTree();

            return treeWalker.getLongestPath().maxZigZagLength();
        }

        private static class TreeWalker {
            private final Tree tree;
            private final Path path;
            private Path longestPath;
            private final boolean debugEnabled;

            public TreeWalker(Tree tree, boolean debugEnabled) {
                this.tree = tree;
                this.path = new Path();
                this.debugEnabled = debugEnabled;
            }

            public void walkTree() {
                walkTree(tree, path);
            }

            private void walkTree(Tree tree, Path currentPath) {
                Tree leftSide = tree.l;
                Tree rightSide = tree.r;

                if (debugEnabled) {
                    System.out.println("Current node:" + tree.x);
                    System.out.println("Path:" + currentPath);
                }

                if (leftSide != null) {
                    currentPath.addStep(Direction.LEFT);
                    if (debugEnabled) {
                        System.out.println("left navigation");
                    }
                    walkTree(leftSide, currentPath);
                }

                if (rightSide != null) {
                    currentPath.addStep(Direction.RIGHT);
                    if (debugEnabled) {
                        System.out.println("right navigation");
                    }
                    walkTree(rightSide, currentPath);
                }

                if (rightSide == null && leftSide == null) {
                    if (longestPath == null ||
                            currentPath.maxZigZagLength() > longestPath.maxZigZagLength()) {
                        longestPath = new Path(currentPath);
                    }
                }
                path.removeStep();
            }

            public Path getLongestPath() {
                return longestPath;
            }
        }

        private static class Path {
            private static final int MAX_PATH_DEPTH = 800;
            private final List<Solution.Step> steps = new ArrayList<>(MAX_PATH_DEPTH);

            public Path() {
            }

            private Path(Path path) {
                this();
                this.steps.addAll(path.steps);
            }

            public void addStep(Direction direction) {
                Solution.Step last = steps.isEmpty() ? null : getLastStep();
                steps.add(new Step(last, direction));
            }

            private Step getLastStep() {
                return (steps.isEmpty()) ? null : steps.get(steps.size() - 1);
            }

            public void removeStep() {
                if (!steps.isEmpty()) {
                    steps.remove(steps.size() - 1);
                }
            }

            public int maxZigZagLength() {
                var lastStep = getLastStep();
                return lastStep != null ? getLastStep().getZigZagLength() : 0;
            }

            public String toString() {
                return steps + " zigZagLength=" + maxZigZagLength();
            }
        }

        private static class Step {
            private int zigZagLength = 0;
            private final Direction direction;

            private Step(Step previous, Direction direction) {
                this.direction = direction;
                if (previous != null) {
                    var previousLength = previous.zigZagLength;
                    this.zigZagLength = previous.matches(direction) ? previousLength : previousLength + 1;
                }
            }

            public boolean matches(Direction direction) {
                return this.direction.equals(direction);
            }

            public int getZigZagLength() {
                return this.zigZagLength;
            }
        }

        private enum Direction {
            LEFT,
            RIGHT
        }
    }

}
