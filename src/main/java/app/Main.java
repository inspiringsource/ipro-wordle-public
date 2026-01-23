package app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    // for week 4 we can create a auto generated word list
    private static final String[] WOERTERBUCH = { "AARAU", "BASEL", "BRUGG", "DATEI", "MODUL", "LOGIK" };
    private static final int MAX_ATTEMPTS = 6;

    public static String getFeedback(String erratenesWort, String zielwort) {
        String feedback = "";

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
        java.net.URL resource = Main.class.getResource("/data/5_letter_words.txt");
        if (resource == null)
            throw new java.io.FileNotFoundException("File not found: data/5_letter_words.txt");
        List<String> woerter = Files.readAllLines(Path.of(resource.toURI()));

        String zielwort = WOERTERBUCH[1];
        // WOERTERBUCH[(int) (Math.random() * WOERTERBUCH.length)];

        Scanner scanner = new Scanner(System.in);
        int versuche = 0;

        while (versuche < MAX_ATTEMPTS) {
            System.out.print("Versuch " + (versuche + 1) + ": ");
            String erratenesWort = scanner.nextLine().trim().toUpperCase();

            if (!woerter.contains(erratenesWort)) {
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