import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bank
{
    private HashMap<Integer, Account> accounts = createAccs();
    private final Random random = new Random();
    private final long suspiciousAmount = 50000;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    public synchronized boolean isFraud(int fromAccountNum, int toAccountNum, long amount)
        throws InterruptedException
    {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public void transfer(int fromAccountNum, int toAccountNum, long amount)
    {
        if (fromAccountNum == toAccountNum)
        {
            return;
        }

        Account accFrom = getAccount(fromAccountNum);
        Account accTo = getAccount(toAccountNum);

        accFrom.checkIsBlocked();
        accTo.checkIsBlocked();

        securityCheck(accFrom, accTo, amount);

        accFrom.withdrawMoney(amount);

        try
        {
            accTo.putMoney(amount);
        }
        catch (BlockedAccException ex)
        {
            accFrom.putMoney(amount);
            throw ex;
        }

    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(int accountNum)
    {
        return getAccount(accountNum).getMoney();
    }

    /**
     * Метод isBlocked и getTotalBalance сделан только для тестирования
     */

    public boolean isBlocked(int accountNum)
    {
        return getAccount(accountNum).isBlocked();
    }

    public long getTotalBalance()
    {
        long sum = 0;
        for (Account acc : accounts.values())
            sum += acc.getMoney();
            return sum;

    }

    private static HashMap<Integer, Account> createAccs()
    {
        HashMap<Integer, Account> accountMap = new HashMap<>();
        for (int i = 1; i <= 1000; i++)
        {
            long money = 100000;
            Account account = new Account(i, money);
            accountMap.put(i, account);
        }
        return accountMap;
    }

    private void securityCheck(Account fromAccount, Account toAccount, long amount)
    {
        if (amount > suspiciousAmount)
        {
                try
                {
                    if (isFraud(fromAccount.getAccNumber(), toAccount.getAccNumber(), amount))
                    {
                        fromAccount.blockAccount();
                        toAccount.blockAccount();
                    }
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
        }
    }


    public HashMap<Integer, Account> getAccounts() {
        return accounts;
    }

    private Account getAccount(int accountNum) {
        Account account = accounts.get(accountNum);
        if (account == null)
        {
            throw new AccountDoesNotExistException(accountNum);
        }
        return account;
    }
}
