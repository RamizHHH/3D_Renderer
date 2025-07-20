import java.util.Comparator;

public class QuickSort {

    public static <T> void qsort(T[] arr, Comparator<? super T> comparator) {
        if (arr == null || arr.length < 2) {
            return; // No need to sort
        }
        QuickSort1(arr, 0, arr.length - 1, comparator);
    }

    private static <T> void QuickSort1(T[] arr, int low, int high, Comparator<? super T> comparator) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high, comparator);
            QuickSort1(arr, low, pivotIndex - 1, comparator);
            QuickSort1(arr, pivotIndex + 1, high, comparator);
        }
    }

    private static <T> int partition(T[] arr, int low, int high, Comparator<? super T> comparator) {
        T pivot = arr[high]; // Choose the last element as pivot
        int i = low - 1; // Pointer for the smaller element

        for (int j = low; j < high; j++) {
            if (comparator.compare(arr[j], pivot) <= 0) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high); // Place the pivot in the correct position
        return i + 1;
    }

    private static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
