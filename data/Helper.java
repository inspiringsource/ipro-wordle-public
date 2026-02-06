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
        Path inputFile = Path.of("data/German-words-1600000-words-multilines.json");
        Path outputFile = Path.of("src/main/resources/data/5_letter_wordsv2.txt");

        try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Remove JSON characters like quotes, commas, and brackets
                line = line.replaceAll("[\"\\[\\],]", "").trim();
                if (line.isEmpty()) continue;

                // erst normalisieren
                String upper = line.toUpperCase(Locale.ROOT);

                // Skip words containing German special characters (ß, Ä, Ö, Ü)
                if (line.contains("ß") || line.contains("ẞ")
                        || upper.contains("Ä") || upper.contains("Ö") || upper.contains("Ü")) {
                    continue;
                }

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