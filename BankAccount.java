public class BankAccount {
    private int balance;

    public BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    public synchronized void deposit(int amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println(Thread.currentThread().getName() + " пополнил на " + amount + ". Баланс: " + balance);
        }
    }

    public synchronized boolean withdraw(int amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + " снял " + amount + ". Баланс: " + balance);
            return true;
        } else {
            System.out.println(Thread.currentThread().getName() + " не смог снять " + amount + " (недостаточно средств). Баланс: " + balance);
            return false;
        }
    }

    public synchronized int getBalance() {
        return balance;
    }
}
