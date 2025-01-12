package tech.underoaks.coldcase.helper;

/**
 * This class provides helper functions that are used throughout the project.
 *
 * @author mabe.edu
 */
public class HelperFunctions {

    /**
     * Maps an integer to a letter.
     *
     * @param number     The number to map to a letter. Must be between 1 and 26. 1 corresponds to 'a' or 'A', 2 to 'b' or 'B', etc.
     * @param isUpperCase Whether the letter should be uppercase.
     * @return The letter corresponding to the number.
     * @throws IllegalArgumentException If the number is not between 1 and 26.
     */
    public static char mapIntToLetter(int number, boolean isUpperCase) {
        if (number < 1 || number > 26) {
            throw new IllegalArgumentException("Number must be between 1 and 26");
        }
        if (isUpperCase) {
            return (char) ('A' + number - 1);
        } else {
            return (char) ('a' + number - 1);
        }
    }
}
