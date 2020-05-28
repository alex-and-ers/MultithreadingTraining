import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class BankTest
{
    @Test(expected = AccountDoesNotExistException.class)
    public void testAccountDoesNotExistException() throws Exception {
        new Bank().getBalance(7777777);
    }
    @Test(expected = NotEnoughMoneyException.class)
    public void testTransfer() throws Exception {
        Bank bank = new Bank();

        /**
         * В этом тесте проверяем последовательно
         * 1. Соответствие на старте
         * 2. и 3. успешные переводы
         * 4. ошибочный перевод, который должен привести к исключению
         */
        assert bank.getBalance(1) == 100000;
        assert bank.getBalance(2) == 100000;
        assert !bank.isBlocked(1);
        assert !bank.isBlocked(2);

        bank.transfer(1, 2, 500);
        assert bank.getBalance(1) == 99500;
        assert bank.getBalance(2) == 100500;
        assert !bank.isBlocked(1);
        assert !bank.isBlocked(2);

        bank.transfer(2, 1, 500);
        assert bank.getBalance(1) == 100000;
        assert bank.getBalance(2) == 100000;
        assert !bank.isBlocked(1);
        assert !bank.isBlocked(2);

        bank.transfer(1, 2, 200000);
        assert bank.getBalance(1) == 101000;
        assert bank.getBalance(2) == 99000;
        assert !bank.isBlocked(1);
        assert !bank.isBlocked(2);
    }

    @Test
    public void testConcurrent() throws Exception
    {
        Random random = new Random();
        Bank bank = new Bank();
        long totalBalance = bank.getTotalBalance();

        /**
         * Здесь запускаем 1000 потоков с переводами между случайными счетами,
         * затем проверяем сумму всех средств на счетах, т.к. не появляется новых аккаунтов
         * сумма не должна меняться
         */

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                for (int i1 = 0; i1 < 10; i1++) {
                    int from = random.nextInt(1000);
                    int to = random.nextInt(1000);
                    try {
                        bank.transfer(from, to, random.nextInt(70000));
                    } catch (Exception e) {
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        long totalBalance1 = bank.getTotalBalance();
        int blockedCount = 0;
        for (Account account : bank.getAccounts().values()) {
            if (bank.isBlocked(account.getAccNumber())) {
                blockedCount++;
            }
        }

        assert totalBalance == totalBalance1;

        System.out.println("Number of blocked accounts: " + blockedCount);
        System.out.println("Total balance: " + totalBalance);
    }
}
