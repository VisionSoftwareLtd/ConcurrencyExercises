package temp;

public class DeadlockDemo {
    private volatile int count;

    public synchronized int getCount() {
        System.out.println(Thread.currentThread().getName() + " - DeadlockDemo.getCount");
        return count;
    }

    public synchronized void setCount(DeadlockDemo dd) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " - DeadlockDemo.setCount");
        Thread.sleep(1);
        this.count = dd.getCount();
    }
}
