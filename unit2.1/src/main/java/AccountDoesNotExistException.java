public class AccountDoesNotExistException extends RuntimeException
{
    public AccountDoesNotExistException(int accNum)
    {
        String message = "Account [" + accNum + "] doesn't exist!";
    }
}
