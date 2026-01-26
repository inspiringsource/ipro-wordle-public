package app;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    // for week 4 we can create a auto generated word list
    private static final String[] WOERTERBUCH = { "AARAU", "BASEL", "BRUGG", "DATEI", "MODUL", "LOGIK" };
    private static final int MAX_ATTEMPTS = 6;

    public static String getFeedback(String erratenesWort, String zielwort) {
        String feedback = "";
        /*
         * Wenn richtiger Buchstabe und richtige Stelle G (Grün)
         * Wenn richtiger Buchstabe und falsch Stelle Y (Gelb)
         * Wenn Buchstabe nicht enthalten B (Grau)
        */
        for (int i = 0; i < 5; i++) {
            if (erratenesWort.charAt(i) == zielwort.charAt(i)) {
                feedback += "G";
            } else {
                boolean found = false;

                for (int j = 0; j < 5; j++) {
                    if (erratenesWort.charAt(i) == zielwort.charAt(j)) {
                        found = true;
                        break;
                    }
                }

                if (found)
                    feedback += "Y";
                else
                    feedback += "B";
            }
        }

        return feedback;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Main game logic loaded");
        // Updated to read from resources
        
        Random random = new Random();
        List<String> woerterbuch = Dictionary.load5LetterWords();
        // truly random word from dictionary
        //String zielwort = woerterbuch.get(random.nextInt(woerterbuch.size()));
        String zielwort = WOERTERBUCH[1]; // for testing Basel

        Scanner scanner = new Scanner(System.in);
        int versuche = 0;

        while (versuche < MAX_ATTEMPTS) {
            System.out.print("Versuch " + (versuche + 1) + ": ");
            String erratenesWort = scanner.nextLine().trim().toUpperCase();

            if (!Dictionary.contains(woerterbuch, erratenesWort)) {
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