package application;

/**
 * Custom exception thrown when an invalid input is provided during the parsing process
 */
public class ParseValueException extends Exception {
    public ParseValueException(String msg) {
        super(msg);
    }
}
