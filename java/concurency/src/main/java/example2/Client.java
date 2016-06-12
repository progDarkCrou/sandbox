package example2;

/**
 * Created by avorona on 03.11.15.
 */
public abstract class Client {

    private Account account;

    public Client(Account account) {
        this.account = account;
    }

    public void putAndCheck(int amount) {
        synchronized (account) {
            int a = this.account.getMoneyAmount();
            this.account.putMoney(amount);
            int b = this.account.getMoneyAmount();

            if (a + amount != b) {
                throw new IllegalArgumentException("Something went wrong. Start balance: " + a +
                        ", putted " + amount +  ", but the result is: " + b);
            }
        }
    }

    public void removeAndCheck(int amount) {
        synchronized (account) {
            int a = this.account.getMoneyAmount();
            this.account.removeMoney(amount);
            int b = this.account.getMoneyAmount();

            if (a - amount != b) {
                throw new IllegalArgumentException("Something went wrong. Start balance: " + a +
                        ", removed " + amount +  ", but the result is: " + b);
            }
        }
    }
}
