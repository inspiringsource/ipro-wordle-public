package app;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String[] WOERTERBUCH = {"AARAU", "BASEL", "BRUGG", "DATEI", "MODUL", "LOGIK"};
    private static final int MAX_ATTEMPTS = 6;

    public static void main(String[] args) throws Exception {
        // Updated to read from resources
        java.net.URL resource = Main.class.getResource("/data/5_letter_words.txt");
        if (resource == null) throw new java.io.FileNotFoundException("File not found: data/5_letter_words.txt");
        List<String> woerter = Files.readAllLines(Path.of(resource.toURI()));

        String zielwort = WOERTERBUCH[(int) (Math.random() * WOERTERBUCH.length)];

        Scanner scanner = new Scanner(System.in);
        int versuche = 0;

        while (versuche < MAX_ATTEMPTS) {
            System.out.print("Versuch " + (versuche + 1) + ": ");
            String guess = scanner.nextLine().trim().toUpperCase();


            if (!woerter.contains(guess)) {
                System.out.println("Kein gueltiges deutsches Wort oder Wort zu kurz.");
                continue;
            }

            versuche++;

            if (guess.equals(zielwort)) {
                System.out.println("Feedback: GGGGG");
                System.out.println("Gewonnen!");
                scanner.close();
                return;
            }
            String feedback = "";

            for (int i = 0; i < 5; i++) {
                /*
                    Wenn richtiger Buchstabe und richtige Stelle    G (Grün)
                    Wenn richtiger Buchstabe und  falsch Stelle     Y (Gelb)
                    Wenn Buchstabe nicht enthalten                  B (Grau)

                    Ideen für 5 Buchstaben Wörter: AARAU, BASEL, BRUGG, DATEI, MODUL, LOGIK
                */
                if (guess.charAt(i) == zielwort.charAt(i)) {
                    feedback += "G";
                } else {
                    boolean found = false;

                    for (int j = 0; j < 5; j++) {
                        if (guess.charAt(i) == zielwort.charAt(j)) {
                            found = true;
                        }
                    }

                    if (found) {
                        feedback += "Y";
                    } else {
                        feedback += "B";
                    }
                }
            }

            System.out.println("Feedback:  " + feedback);
        }

        System.out.println("Game Over! Zielwort war: " + zielwort);
        scanner.close();
    }

}