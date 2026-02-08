package app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.Collectors;

public class Dictionary {
    private static Set<String> validWords = new HashSet<>();
    private static List<String> solutionWords = new ArrayList<>();
    private static final Random random = new Random();

    // Static block to load words when class is initialized
    static {
        loadWords();
    }

    private static void loadWords() {
        try {
            // Load validation words (bigger set)
            try (InputStream isHandler = Dictionary.class.getClassLoader()
                    .getResourceAsStream("data/5_letter_wordsv2.txt");
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(isHandler, StandardCharsets.UTF_8))) {
                validWords = reader.lines()
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .filter(line -> !line.isEmpty())
                        .collect(Collectors.toSet());
            }

            // Load solution words (common words)
            try (InputStream isHandler = Dictionary.class.getClassLoader()
                    .getResourceAsStream("data/5_letter_common_words.txt");
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(isHandler, StandardCharsets.UTF_8))) {
                solutionWords = reader.lines()
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .filter(line -> !line.isEmpty())
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not load word dictionaries", e);
        }
    }

    // Deprecated: Kept for compatibility if necessary, but returning validWords as
    // list is expensive/weird.
    // Better to remove if we update all callers. The plan says I will update all
    // callers.
    // So I will remove load5LetterWords.

    public static boolean isValidWord(String word) {
        return validWords.contains(word.toUpperCase());
    }

    public static String randomSolutionWord() {
        if (solutionWords.isEmpty()) {
            throw new IllegalStateException("Solution words list is empty");
        }
        return solutionWords.get(random.nextInt(solutionWords.size()));
    }

    // Helper for validation (legacy/refactored)
    public static boolean contains(String guess) {
        return isValidWord(guess);
    }
}
