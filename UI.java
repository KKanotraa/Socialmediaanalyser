package application;

import java.util.HashMap;

/**
 * Library of the UI element for console applications.
 *
 * @author AA
 * @version 1.0.0
 */
public class UI {
    /**
     * Print heading in console
     *
     * @param str heading to be displayed
     * @param width width of the heading
     */
    static public void heading(String str, int width) {
        System.out.println();
        System.out.println("-".repeat(width));
        System.out.println("> " + str);
        System.out.println("-".repeat(width));
    }

    /**
     * Print menu in console
     *
     * @param options Hashmap of the options to be displayed
     */
    public static void menu(HashMap<String, String> options) {
        // print menu
        for (String key : options.keySet()) {
            System.out.printf("%s) %s\n", key, options.get(key));
        }
    }
}