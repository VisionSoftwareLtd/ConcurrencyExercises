package temp;

public class DodgyCounter {
    private volatile int count;

    public void increment() throws InterruptedException {
        int newCount = count + 1;
        Thread.sleep(1);
        count = newCount;
    }

    public int get() {
        return count;
    }
}