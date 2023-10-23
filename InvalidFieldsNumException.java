package application;

/**
 * Custom exception for invalid number of fields in CSV string
 *
 * @author AA
 * @version 1.0.0
 */
public class InvalidFieldsNumException extends Exception {
    int expectedFieldNum;
    int numOfFields;

    public InvalidFieldsNumException(int expectedFieldNum, int numOfFields) {
        this.expectedFieldNum = expectedFieldNum;
        this.numOfFields = numOfFields;
    }
}
