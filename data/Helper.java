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
        Path inputFile = Path.of("data/1000-most-common-german-words.txt");
        Path outputFile = Path.of("src/main/resources/data/5_letter_common_words.txt");

        try (BufferedReader reader = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("[\"\\[\\],]", "").trim();

                if (!line.isEmpty()) {

                    String upper = line.toUpperCase(Locale.ROOT);

                    boolean hasSpecial =
                            line.contains("ß") || line.contains("ẞ") ||
                            upper.contains("Ä") || upper.contains("Ö") || upper.contains("Ü");

                    if (!hasSpecial && upper.length() == 5) {
                        writer.write(upper);
                        writer.newLine();
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}