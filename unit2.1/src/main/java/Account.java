public class Account
{
    private long money;
    private int accNumber;
    private boolean isBlocked = false;

    public Account(int accNumber, long money) {
        this.money = money;
        this.accNumber = accNumber;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public int getAccNumber() {
        return accNumber;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public synchronized void withdrawMoney(long amount) {
        if (money >= amount)
        {
            money -= amount;
        }
        else
        {
            throw new NotEnoughMoneyException(this.accNumber, amount);
        }
    }

    public synchronized void putMoney(long amount)
    {
        money += amount;
    }

    public synchronized void blockAccount() {
        isBlocked = true;
    }

    public synchronized void checkIsBlocked() throws BlockedAccException {
        if (isBlocked()) {
            throw new BlockedAccException(accNumber);
        }
    }
}
