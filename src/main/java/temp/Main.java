package temp;

import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        final String[] urls = new String[] {
                "https://www.google.com",
                "https://www.amazon.co.uk",
                "https://www.yahoo.com",
                "https://www.bt.com"
        };
        CompletableFuture<String>[] futures = new CompletableFuture[urls.length];
        for (int i = 0; i < urls.length; i++) {
            int finalI = i;
            futures[i] = CompletableFuture.supplyAsync(
                    () -> urls[finalI] + ": " + WebpageUtility.getWebpage(urls[finalI]).length()
            );
        }
        System.out.println("Getting all web pages");
        try {
            boolean isWaiting = true;
            while (isWaiting) {
                System.out.print(".");
                Thread.sleep(1000);
                isWaiting = false;
                for (CompletableFuture<String> future : futures) {
                    if (!future.isDone()) {
                        isWaiting = true;
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\nWeb pages retrieved");
        for (int i = 0; i < urls.length; i++) {
            try {
                System.out.println(futures[i].get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static void mainCompletableFutures(String[] args) {
        System.out.println(Thread.currentThread().getName());
        CompletableFuture<Void> completableFuture =
                CompletableFuture.supplyAsync(() -> {
                    System.out.println(Thread.currentThread().getName());
                    return "Hello";
                })
                .thenApplyAsync((s) -> {
                    System.out.println(Thread.currentThread().getName());
                    return s + " World";
                })
                .thenAccept((s) -> {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println("s = " + s);
                });
    }

    public static Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.complete("Hello");
            return null;
        });

        return completableFuture;
    }

    public static void mainExecutorService(String[] args) {
        final String[] urls = new String[] {
                "https://www.google.com",
                "https://www.amazon.co.uk",
                "https://www.yahoo.com",
                "https://www.bt.com"
        };
        ExecutorService pool = Executors.newFixedThreadPool(5);
        Runnable[] runnables = new Runnable[urls.length];
        for (int i = 0; i < urls.length; i++) {
            int finalI = i;
            runnables[i] =
                    () -> System.out.println(urls[finalI] + ": " + WebpageUtility.getWebpage(urls[finalI]).length());
        }
        for (Runnable runnable : runnables) {
            pool.execute(runnable);
        }
        System.out.println("Started all threads");
        pool.shutdown();
        try {
            pool.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Shutdown complete");
    }

    public static void mainAtomicInteger(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            AtomicInteger myInt = new AtomicInteger(1);
            Runnable myrunnable = () -> {
                myInt.incrementAndGet();
            };
            Thread t1 = new Thread(myrunnable);
            Thread t2 = new Thread(myrunnable);
            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (myInt.get() == 1)
                System.out.println("myInt = " + myInt.get());
            if (i % 10000 == 0)
                System.out.println("i = " + i);
        }
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start) / 10);
    }

    public static void mainDodgyCounter(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            DodgyCounter counter = new DodgyCounter();
            Runnable myrunnable = () -> {
                try {
                    counter.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            Thread dodgyThread = new Thread(myrunnable);
            Thread dodgierThread = new Thread(myrunnable);
            dodgyThread.start();
            dodgierThread.start();
            try {
                dodgyThread.join();
                dodgierThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (counter.get() == 1)
                System.out.println("myInt = " + counter.get());
//            if (i % 10000 == 0)
                System.out.println("i = " + i);
        }
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start) / 10);
    }

    public static void mainDeadlock(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println("i = " + i);
            DeadlockDemo dd1 = new DeadlockDemo();
            DeadlockDemo dd2 = new DeadlockDemo();
            Runnable run1 = () -> {
                try {
                    dd1.setCount(dd2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            Runnable run2 = () -> {
                try {
                    dd2.setCount(dd1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            Thread t1 = new Thread(run1, "t1");
            Thread t2 = new Thread(run2, "t2");
            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void mainThreadName(String[] args) {
        Runnable myRunnable = () -> {
            System.out.println(Thread.currentThread().getName());
        };
        Thread myThread = new Thread(myRunnable);
        myThread.run();
        myThread.start();
    }

    private static String readLine() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
