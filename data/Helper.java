package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// https://stackoverflow.com/questions/22728961/how-to-read-the-first-line-of-a-text-file-in-java-and-print-it-out
public class Helper {

    public static void main(String[] args) {
        String inputFile = "data/words";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            // print the number of lines
            // int lineCount = 0;
            
            // while (reader.readLine() != null) {
            //     lineCount++;
            // }
            // System.out.println("Number of lines: " + lineCount); // 685789

            for (int i = 0; i < 685789; i++) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (line.length() == 5)
                    System.out.println(line);
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}