package app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class Dictionary {

    public static List<String> load5LetterWords() {
        try (InputStream is = Dictionary.class.getClassLoader().getResourceAsStream("data/5_letter_words.txt")) {
            if (is == null) {
                throw new RuntimeException("Could not load 5_letter_words.txt");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines()
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .filter(line -> !line.isEmpty())
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not load 5_letter_words.txt", e);
        }
    }

    public static boolean contains(List<String> dict, String guess) {
        return dict.contains(guess);
    }
}
