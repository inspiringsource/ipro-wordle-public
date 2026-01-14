# ipro-Wordle

Wordle game in Java Individuelles Softwareprojekt (ipro)

```markdown
| Nachname  | Vorname | Projektname | Betreuung           |
|-----------|---------|-------------|---------------------|
| B.        | Avi     | Wordle      | A. A.               |
```

## Spielregeln und Konventionen

### Buchstaben-Feedback

- Richtiger Buchstabe an richtiger Stelle: **G** (Gruen)
- Richtiger Buchstabe an falscher Stelle: **Y** (Gelb)
- Buchstabe kommt nicht im Wort vor: **B** (Grau)

### Beispiel-Woerterbuch (5 Buchstaben)

Ideen für 5 Buchstaben Wörter: AARAU, BASEL, BRUGG, DATEI, MODUL, LOGIK

### Logik (English)

1. Define a dictionary of valid 5 letter words

2. Read user input

3. Check if input is correct and valid:
   - Exactly 5 characters and real word
   - Letters A–Z only (using Regex: https://stackoverflow.com/a/11949550 and https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html)
   https://www.geeksforgeeks.org/java/java-user-input-scanner-class/
      - `matches("[A-Z]{5}")` uses the built in feature matches
   - If invalid, request input again

4. Check input (word) against the dictionary
   - If the word is valid, compare char
https://github.com/enz/german-wordlist 
This repo provides German words

We will create a helper java code to prepare the words.
Requirements: 
   - 5 characters words only
   - Inlcude city names (like AARAU, BASEL, BRUGG)
   - Letters (A-Z) we also should include Umlaute (Ä, Ö, Ü)
We can loop through the list of words and create a new list with filtered {5} characters only words we also should convert to uppercase.

5. Compare characters:
   - Correct letter, correct position → G (Green)
   - Correct letter, wrong position → Y   (Yellow)
   - Letter not contained → B             (Black [we use Grey])

can we output different colors in terminal?
https://intellij-support.jetbrains.com/hc/en-us/community/posts/360006477540-Is-there-any-way-that-i-can-change-the-color-of-the-text-output-in-the-console-in-the-program

6. Output feedback per character

7. Increment attempt counter
   - 6 attempts maximum
   - Request input again

8. Check for win or termination: I think a while loop here makes most sense...

## Random Wort aus dem Woerterbuch (Idee fuer spaeter)

Referenz (Stack Overflow):  
https://stackoverflow.com/a/7923141

Beispiel:

```java
String zufallsWort = WOERTERBUCH[(int)(Math.random() * WOERTERBUCH.length)];
```

## Learning Resources & References

Resources used to understand the problem

- **Build a Wordle Clone in Java**  
  https://medium.com/strategio/build-a-wordle-clone-in-java-c7b7b924fb8d

- **Leverage Java 17 New Features to Create Your Wordle Checker – JEP Café #10**  
  Video with José Paumard (February 22, 2022)  
  https://inside.java/2022/02/22/jepcafe10/

*Whilst the video is based on Java 17 I still use this as a practice.

## Resources

Woerterliste (Quelle)

Fuer die gueltigen deutschen Woerter verwende ich die Woerterliste aus dem Repository **enz/german-wordlist** (Lizenz: **CC0-1.0**).

Quelle:
- https://github.com/enz/german-wordlist
- Datei: `words` (UTF-8, ein Wort pro Zeile)

Verwendung im Projekt:
- Die Rohdatei `words` wird mit `data/Helper.java` gefiltert (genau 5 Buchstaben) und in Grossbuchstaben normalisiert.
- Das Ergebnis wird als eigene Liste fuer das Wordle-Woerterbuch genutzt.