package example2;

/**
 * Created by avorona on 03.11.15.
 */
public class Account {

    private volatile int moneyAmount;

    public Account(int initMoneyAmount) {
        this.moneyAmount = initMoneyAmount;
    }

    public int getMoneyAmount() {
        return moneyAmount;
    }

    public void updateMoneyAmount(int delta) {
        moneyAmount += delta;
    }

    public void putMoney(int delta) {
        updateMoneyAmount(Math.abs(delta));
    }

    public void removeMoney(int delta) {
        updateMoneyAmount(-Math.abs(delta));
    }
}
