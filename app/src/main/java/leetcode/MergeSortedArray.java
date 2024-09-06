package leetcode;

/**
 * See <a href="https://leetcode.com/problems/merge-sorted-array/description/">88. Merge Sorted Array</a> for details.
 */
public class MergeSortedArray {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int nIndex = n - 1;
        int mIndex = m - 1;

        for (int i = nums1.length - 1; i >= 0; i--) {
            if (nIndex == -1) {
                break;
            }

            if (mIndex == -1) {
                nums1[i] = nums2[nIndex];
                nIndex--;
                continue;
            }

            int nValue = nums2[nIndex];
            int mValue = nums1[mIndex];

            if (nValue >= mValue) {
                nums1[i] = nValue;
                nIndex--;
            } else {
                nums1[i] = mValue;
                mIndex--;
            }
        }
    }
}
