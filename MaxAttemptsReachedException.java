package application;

/**
 * Custom exception thrown when max number of invalid input attempts reaches
 *
 * @author AA
 * @version 1.0.0
 */
public class MaxAttemptsReachedException extends Exception {
    int maxAttempts;

    public MaxAttemptsReachedException(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}
