package pkg;


public class Unsupported {

    int[] a;

    void arrays_alloc() {
        this.a = new int[]{1, 2, 3, 4, 5};
    }

    void arrays_read() {
        int i = this.a[0];
    }

    void arrays_write() {
        this.a[4] = 3;
    }
}