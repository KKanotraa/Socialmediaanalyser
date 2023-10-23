package application;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is a library for taking valid inputs from user. It also has
 * methods to parse different types of data.
 *
 * @author AA
 * @version 1.0.0
 */
public class CustomScanner {
    static private int attempts = 0;
    static private int maxAttempts = 3;

    /**
     * Increment input attempts. Throws an error when the attempts reaches the
     * maximum.
     *
     * @throws MaxAttemptsReachedException when max attempts is reached
     */
    private static void incAttempt() throws MaxAttemptsReachedException {
        attempts++;
        if (attempts >= maxAttempts) {
            resetAttempts();
            throw new MaxAttemptsReachedException(maxAttempts);
        }
    }

    /**
     * Reset input attempts
     */
    private static void resetAttempts() {
        attempts = 0;
    }

    /**
     * Ask user for a valid option input from the provided list
     *
     * @param in Input stream for user input
     * @param prompt String that will be displayed in the stdout to the user
     * @param options List of valid options
     * @return String value input from list
     */
    public static String getOption(InputStream in, String prompt, ArrayList<String> options) {
        Scanner scan = new Scanner(in);
        String input;
        while (true) {
            // display prompts
            System.out.print(prompt);
            // get valid integer
            input = scan.nextLine();
            if (options.indexOf(input) != -1) {
                return input;
            } else {
                System.out.println("Invalid option! Please select option from the list!");
                input = null;
            }
        }
    }

    /**
     * Ask user for a valid integer input
     *
     * @param in Input stream for user input
     * @param prompt String that will be displayed in the stdout to the user
     * @param min Minimum accepted integer value
     * @return Valid integer value
     * @throws MaxAttemptsReachedException when max attempts is reached
     */
    public static int getInt(InputStream in, String prompt, int min) throws MaxAttemptsReachedException {
        Scanner scan = new Scanner(in);
        int input;
        while (true) {
            // display prompts
            System.out.print(prompt);
            // get valid integer
            try {
                input = parseInt(scan.nextLine(), min);
                break;
            } catch (ParseValueException e) {
                System.out.println(e.getMessage());
                incAttempt();
            }
        }
        resetAttempts();
        return input;
    }

    /**
     * Ask user for a valid string input
     *
     * @param in Input stream for user input
     * @param prompt String that will be displayed in the stdout to the user
     * @return Valid string value
     * @throws MaxAttemptsReachedException when max attempts is reached
     */
    public static String getStr(InputStream in, String prompt) throws MaxAttemptsReachedException {
        Scanner scan = new Scanner(in);
        String input;
        while (true) {
            // display prompts
            System.out.print(prompt);
            // get valid string
            try {
                input = parseStr(scan.nextLine());
                break;
            } catch (ParseValueException e) {
                System.out.println(e.getMessage());
                incAttempt();
            }
        }
        resetAttempts();
        return input;
    }

    /**
     * Ask user for a valid formatted date
     *
     * @param in Input stream for user input
     * @param prompt String that will be displayed in the stdout to the user
     * @return Valid Date Time value as string
     * @throws MaxAttemptsReachedException when max attempts is reached
     */
    public static String getDateTime(InputStream in, String prompt) throws MaxAttemptsReachedException {
        Scanner scan = new Scanner(in);
        String input;
        while (true) {
            // display prompts
            System.out.print(prompt);
            // get valid string
            try {
                input = parseDateTime(scan.nextLine());
                break;
            } catch (ParseValueException e) {
                System.out.println(e.getMessage());
                incAttempt();
            }
        }
        resetAttempts();
        return input;
    }

    /**
     * Parse date/time string
     *
     * @param dateTime date/time string to be parsed
     * @return Valid date/time as string
     * @throws ParseValueException when the fortmat of the input doesn't match the
     * expected format
     */
    public static String parseDateTime(String dateTime) throws ParseValueException {
        String dateTimeFormat = "dd/MM/yyyy HH:mm";
        DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateTime);
            return dateTime;
        } catch (ParseException e) {
            throw new ParseValueException(String.format("Invalid date/time! Expected format: %s", dateTimeFormat));
        }
    }

    /**
     * Parse integer string value
     *
     * @param integerStr integer string to be parsed
     * @param min minimum value of integer allowed
     * @return Parsed integer
     * @throws ParseValueException when an invalid integer string value is provided
     * or the parsed integer is less the the number specified
     */
    public static int parseInt(String integerStr, int min) throws ParseValueException {
        try {
            int validInt = Integer.parseInt(integerStr);
            if (validInt < min) {
                throw new ParseValueException("Invalid number! Value must be positive integer!");
            }
            return validInt;
        } catch (NumberFormatException e) {
            throw new ParseValueException("Invalid number! Value must be integer");
        }
    }

    /**
     * Parse string value. It trims whitespace at the start and the end of the
     * string
     *
     * @param str string value to be parsed
     * @return Parsed string
     * @throws ParseValueException when the parsed string is empty
     */
    public static String parseStr(String str) throws ParseValueException {
        if (str.trim().length() == 0) {
            throw new ParseValueException("Invalid string! Value cannot be empty!");
        }
        return str.trim();
    }
}
