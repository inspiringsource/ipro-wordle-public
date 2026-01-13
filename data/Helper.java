// https://stackoverflow.com/questions/22728961/how-to-read-the-first-line-of-a-text-file-in-java-and-print-it-out
package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class Helper {

    public static void main(String[] args) {
        Path inputFile = Path.of("data/words");
        Path outputFile = Path.of("data/5_letter_words.txt");

        try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // erst normalisieren
                String upper = line.toUpperCase(Locale.ROOT);

                // optional: ß ausschliessen (weil es zu SS wird)
                if (upper.contains("ß") || upper.contains("ẞ")) continue;

                // dann erst die 5-Buchstaben-Regel anwenden
                if (upper.length() == 5) {
                    writer.write(upper);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}