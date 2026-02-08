package app;

import java.util.Scanner;

/**
 * Console-based Wordle game logic.
 * 
 * This class contains the core gameplay rules and feedback generation.
 *
 * @author Avi B
 */
public class Main {
    // for week 4 we can create a auto generated word list
    // private static final String[] WOERTERBUCH = { "AARAU", "BASEL", "BRUGG",
    // "DATEI", "MODUL", "LOGIK" };

    private static final int MAX_ATTEMPTS = 6; // maximum number of attempts

    public static String getFeedback(String guess, String target) {

        char[] result = {'B','B','B','B','B'}; // default feedback (B)
        char[] tempTarget = target.toCharArray();

        // if correct positions (G)
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == tempTarget[i]) {
                result[i] = 'G';
                tempTarget[i] = '_'; // mark letter as used
            }
        }

        // if present letters (Y)
        for (int i = 0; i < 5; i++) {
            if (result[i] == 'G') continue;

            for (int j = 0; j < 5; j++) {
                if (guess.charAt(i) == tempTarget[j]) {
                    result[i] = 'Y';
                    tempTarget[j] = '_'; // mark as used
                    break;
                }
            }
        }

        return new String(result);
    }

    /**
     * Entry point
     * Initializes the dictionary, selects a random target word,
     *
     * @param args command-line arguments (not used)
     * @throws Exception if loading the dictionary fails or an unexpected error
     *                   occurs
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Main game logic loaded");
        // Updated to read from resources

        // truly random word from dictionary
        String zielwort = Dictionary.randomSolutionWord();
        // String zielwort = WOERTERBUCH[1]; // for testing Basel
        // String zielwort = WOERTERBUCH[1]; // for testing Basel

        Scanner scanner = new Scanner(System.in);
        int versuche = 0;

        while (versuche < MAX_ATTEMPTS) {
            System.out.print("Versuch " + (versuche + 1) + ": ");
            String erratenesWort = scanner.nextLine().trim().toUpperCase();

            if (!Dictionary.isValidWord(erratenesWort)) {
                System.out.println("Kein gueltiges deutsches Wort oder Wort zu kurz.");
                continue;
            }

            versuche++;

            if (erratenesWort.equals(zielwort)) {
                System.out.println("Feedback: GGGGG");
                System.out.println("Gewonnen!");
                scanner.close();
                return;
            }
            String feedback = getFeedback(erratenesWort, zielwort);

            System.out.println("Feedback:  " + feedback);

        }

        System.out.println("Game Over! Zielwort war: " + zielwort);
        scanner.close();
    }

}