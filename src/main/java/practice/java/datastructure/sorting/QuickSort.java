package practice.java.datastructure.sorting;

import javax.swing.tree.TreeNode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by patrick on 6/14/2015.
 *
 * quick sorting
 *
 * time complexity: O(nlogn) average
 * space complexity: O(logn)
 */
public class QuickSort {

    public static void main(String [] args){
        Queue<TreeNode> test = new LinkedList<TreeNode>();
        int [] unsorted = {49,38,65,97,76,13,27};
        //int[] unsorted = {23,43,11,25,50,65,67,55};
        quickSort(unsorted,0,unsorted.length-1);
        for(int i=0;i<unsorted.length;i++){
            System.out.print(unsorted[i]+"; ");
        }
    }

    public static void quickSort(int[] unsorted, int left, int right){
        int pivot;
        if(left < right){
            pivot = partition(unsorted,left,right);
            quickSort(unsorted,left,pivot-1);
            quickSort(unsorted,pivot+1, right);
        }
    }

    public static int partition(int[] unsorted, int left, int right){
        int pivotKey  = unsorted[left];
        while(left < right) {
            while (left < right && unsorted[right] >= pivotKey) --right;
            unsorted[left] = unsorted[right];

            while (left < right && unsorted[left] <= pivotKey) ++left;
            unsorted[right] = unsorted[left];
        }
        System.out.printf("unsort array after pass %s: %n",  Arrays.toString(unsorted));
        unsorted[left] = pivotKey;
        return left;
    }

    public void quicksort2(int[] unsorted, int left ,int right){
        if(left<right){
            int partition = partition2(unsorted,left,right);
            quicksort2(unsorted,left,partition);
            quicksort2(unsorted,partition+1, right);
        }
    }

    public int partition2(int[] unsorted, int left, int right){
        int pivotkey = unsorted[left];
        while(left<right){
            while(left<right && unsorted[right]>=pivotkey) --right;
            unsorted[left] = unsorted[right];
            while(left<right && unsorted[left] <=pivotkey) ++ left;
            unsorted[right] = unsorted[left];
        }
        unsorted[left] = pivotkey;
        return left;

    }
}
