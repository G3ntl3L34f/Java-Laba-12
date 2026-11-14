public class Lab12Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Часть 1.1: Потоки через наследование Thread ===");
        MyThread t1 = new MyThread("Поток-1", 1);
        MyThread t2 = new MyThread("Поток-2", 2);
        MyThread t3 = new MyThread("Поток-3", 3);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("\n=== Часть 1.2: Потоки через Runnable ===");
        Thread r1 = new Thread(new MyRunnable("Runnable-1", 1));
        Thread r2 = new Thread(new MyRunnable("Runnable-2", 2));
        Thread r3 = new Thread(new MyRunnable("Runnable-3", 3));

        r1.start();
        r2.start();
        r3.start();

        r1.join();
        r2.join();
        r3.join();

        System.out.println("\n=== Часть 2: Синхронизированный Counter ===");
        Counter counter = new Counter();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.increment();
                }
            }, "CounterThread-" + (i + 1));
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        System.out.println("Ожидаемое: 10000, фактическое: " + counter.getCount());

        System.out.println("\n=== Часть 3: Counter с ReentrantLock ===");
        CounterLock counterLock = new CounterLock();
        Thread[] lockThreads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            lockThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counterLock.increment();
                }
            }, "LockThread-" + (i + 1));
            lockThreads[i].start();
        }
        for (Thread t : lockThreads) {
            t.join();
        }
        System.out.println("Ожидаемое: 10000, фактическое: " + counterLock.getCount());

        System.out.println("\n=== Банковский счёт ===");
        BankAccount account = new BankAccount(1000);
        System.out.println("Начальный баланс: " + account.getBalance());

        DepositThread deposit = new DepositThread(account, 200, "Пополнение");
        WithdrawThread withdraw = new WithdrawThread(account, 100, "Снятие");

        deposit.start();
        withdraw.start();

        deposit.join();
        withdraw.join();

        System.out.println("Итоговый баланс: " + account.getBalance() + " (ожидалось 1100)");

        System.out.println("\n=== Самостоятельная работа: два варианта счётчика ===");
        runCounterTests();
    }

    private static void runCounterTests() throws InterruptedException {
        // Проверка 1: 10 потоков × 1000 = 10000
        System.out.println("\n--- Проверка 1: 10 × 1000 ---");
        for (int test = 1; test <= 3; test++) {
            System.out.println("Попытка " + test + ":");

            Counter syncCounter = new Counter();
            Thread[] syncThreads = new Thread[10];
            for (int i = 0; i < 10; i++) {
                syncThreads[i] = new Thread(() -> {
                    for (int j = 0; j < 1000; j++) {
                        syncCounter.increment();
                    }
                });
                syncThreads[i].start();
            }
            for (Thread t : syncThreads) {
                t.join();
            }
            System.out.println("  synchronized: ожидаемое 10000, фактическое " + syncCounter.getCount());

            CounterAtomic atomicCounter = new CounterAtomic();
            Thread[] atomicThreads = new Thread[10];
            for (int i = 0; i < 10; i++) {
                atomicThreads[i] = new Thread(() -> {
                    for (int j = 0; j < 1000; j++) {
                        atomicCounter.increment();
                    }
                });
                atomicThreads[i].start();
            }
            for (Thread t : atomicThreads) {
                t.join();
            }
            System.out.println("  AtomicInteger: ожидаемое 10000, фактическое " + atomicCounter.getCount());
        }

        // Проверка 2: 3 потока × 7 = 21
        System.out.println("\n--- Проверка 2: 3 × 7 ---");
        for (int test = 1; test <= 3; test++) {
            System.out.println("Попытка " + test + ":");

            Counter syncCounter = new Counter();
            Thread[] syncThreads = new Thread[3];
            for (int i = 0; i < 3; i++) {
                syncThreads[i] = new Thread(() -> {
                    for (int j = 0; j < 7; j++) {
                        syncCounter.increment();
                    }
                });
                syncThreads[i].start();
            }
            for (Thread t : syncThreads) {
                t.join();
            }
            System.out.println("  synchronized: ожидаемое 21, фактическое " + syncCounter.getCount());

            CounterAtomic atomicCounter = new CounterAtomic();
            Thread[] atomicThreads = new Thread[3];
            for (int i = 0; i < 3; i++) {
                atomicThreads[i] = new Thread(() -> {
                    for (int j = 0; j < 7; j++) {
                        atomicCounter.increment();
                    }
                });
                atomicThreads[i].start();
            }
            for (Thread t : atomicThreads) {
                t.join();
            }
            System.out.println("  AtomicInteger: ожидаемое 21, фактическое " + atomicCounter.getCount());
        }

        // Проверка 3: новый объект — результат не накапливается
        System.out.println("\n--- Проверка 3: новый объект не накапливает ---");
        Counter c1 = new Counter();
        Thread t1 = new Thread(() -> {
            for (int j = 0; j < 5; j++) c1.increment();
        });
        t1.start();
        t1.join();
        System.out.println("Первый счётчик после 5: " + c1.getCount());

        Counter c2 = new Counter();
        Thread t2 = new Thread(() -> {
            for (int j = 0; j < 3; j++) c2.increment();
        });
        t2.start();
        t2.join();
        System.out.println("Второй счётчик после 3: " + c2.getCount() + " (должен быть 3, а не 8)");
    }
}
