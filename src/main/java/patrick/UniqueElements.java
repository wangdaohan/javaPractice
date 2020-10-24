package patrick;

public class UniqueElements {
    public static int getUniqueElement(int[] a, int[] b) {
        int ret = 0;
        for (int i = 0; i < a.length; i++) {
            ret += a[i];
        }
        for (int i = 0; i < b.length; i++) {
            ret -= b[i];
        }
        return Math.abs(ret);
    }
    public static void main(String[] args) {
        int[] a = {6, 5, 6, 3, 4, 2};
        int[] b = {5, 7, 6, 6, 2, 3, 4, 6};
        System.out.println(UniqueElements.getUniqueElement(a,b));
    }
}
