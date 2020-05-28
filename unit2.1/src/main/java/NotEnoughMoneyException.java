public class NotEnoughMoneyException extends RuntimeException
{
    public NotEnoughMoneyException(int accNumber, long money)
    {
        String message = "Account [" + accNumber + "] doesn't have enough  money - [" + money + "]";
    }


}
