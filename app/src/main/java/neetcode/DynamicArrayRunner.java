package neetcode;

import java.util.Arrays;

public class DynamicArrayRunner {

    public static void main(String[] args) {
        testCase1();
        testCase3();
    }

    private static void testCase1() {
        var testArray = new DynamicArray(1);
        System.out.println("testArray.numbers = " + Arrays.toString(testArray.numbers));
        System.out.println("testArray.getSize() = " + testArray.getSize());
        System.out.println("testArray.getCapacity() = " + testArray.getCapacity());
    }

    private static void testCase3() {
        var testArray = new DynamicArray(1);
        System.out.println("null");
        System.out.println(testArray.getSize());
        System.out.println(testArray.getCapacity());
        testArray.pushback(1);
        System.out.println("null");
        System.out.println(testArray.getSize());
        System.out.println(testArray.getCapacity());
        testArray.pushback(2);
        System.out.println("null");
        System.out.println(testArray.getSize());
        System.out.println(testArray.getCapacity());
        System.out.println(testArray.get(1));
        testArray.set(1, 3);
        System.out.println("null");
        System.out.println(testArray.get(1));
            System.out.println(testArray.popback());
        System.out.println(testArray.getSize());
        System.out.println(testArray.getCapacity());

        // ["Array", 1,
        // "getSize",
        // "getCapacity",
        // "pushback", 1,
        // "getSize",
        // "getCapacity",
        // "pushback",
        // 2,
        // "getSize",
        // "getCapacity",
        // "get", 1,
        // "set", 1, 3,
        // "get", 1,
        // "popback",
        // "getSize",
        // "getCapacity"]
    }


    private static class DynamicArray {
        private int[] numbers;
        private int size;

        // O(n) - n = capacity
        public DynamicArray(int capacity) {
            this.numbers = new int[capacity];
        }

        // O(1)
        public int get(int i) {
            return numbers[i];
        }

        // O(1)
        public void set(int i, int n) {
            numbers[i] = n;
        }

        // O_1+ - Avg case / Amortized
        public void pushback(int n) {
            optionalResize();
            numbers[size] = n;
            size++;
        }

        // O(1)
        public int popback() {
            var lastValue = numbers[size - 1];
            size--;
            return lastValue;
        }


        private void optionalResize() {
            if (getSize() == getCapacity()) {
                resize();
            }
        }
        // O(n)
        private void resize() {
            int[] resizedNumbers = new int[numbers.length * 2];
            System.arraycopy(numbers, 0, resizedNumbers, 0, numbers.length);
            this.numbers = resizedNumbers;
        }

        // O(1)
        public int getSize() {
            return size;
        }

        // O(1)
        public int getCapacity() {
            return numbers.length;
        }
    }
}