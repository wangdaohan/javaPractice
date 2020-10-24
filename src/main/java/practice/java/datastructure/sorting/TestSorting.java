package practice.java.datastructure.sorting;

/**
 * Created by patrick on 6/15/2015.
 */
public class TestSorting {
    public static void main(String[] args){
        int[] datas={9,3,2,7,4,5,1,10,11};


    }

    public static void quickSort(int[] unsorted, int left, int right){
        int pivot;
        while(left<right){
        pivot = partition(unsorted,left,right);
        partition(unsorted,left,pivot);
        partition(unsorted,pivot+1,right);
    }
    }


    public  static  int partition(int[] unsorted, int left, int right){
        int pivotKey = unsorted[left];
        while(left<right) {
            while (left < right && unsorted[right] >= pivotKey) --right;
            unsorted[left] = unsorted[right];

            while (left < right && unsorted[left] < pivotKey) ++left;
            unsorted[right] = unsorted[left];
        }
        unsorted[left] = pivotKey;
        return left;

    }

}
