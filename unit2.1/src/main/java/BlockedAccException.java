public class BlockedAccException extends RuntimeException
{
    public BlockedAccException(int accNumber)
    {
        String message = "Account [" + accNumber + "] is blocked!";
    }
}
