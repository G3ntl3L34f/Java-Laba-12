public class MyRunnable implements Runnable {
    private String name;
    private int threadNumber;

    public MyRunnable(String name, int threadNumber) {
        this.name = name;
        this.threadNumber = threadNumber;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(name + " - итерация " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
