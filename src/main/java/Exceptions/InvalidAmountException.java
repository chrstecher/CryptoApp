package Exceptions;

public class InvalidAmountException extends Exception {
    public InvalidAmountException()
    {
        super("Invalid Amount < 0!");
    }
}
