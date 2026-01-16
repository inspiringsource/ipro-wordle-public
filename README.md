# ipro-Wordle

Wordle Game in Java
Individuelles Softwareprojekt (ipro)

| Nachname | Vorname | Projektname | Betreuung |
|----------|---------|-------------|-----------|
| B.       | Avi     | Wordle      | A. A.     |

⸻

1. Project Overview

This project implements a simplified Wordle-style game in Java as part of the Individuelles Softwareprojekt (ipro).

The project follows an incremental approach:
	1.	implement and validate the complete game logic as a console application
	2.	reuse the existing logic in a simple web application using Javalin, HTML, CSS, and JavaScript

The main focus is on:
	•	clear and correct game logic
	•	input validation
	•	incremental development
	•	separation of logic and presentation

Advanced features such as multiplayer are intentionally out of scope.

⸻

2. Game Rules & Conventions

2.1 Letter Feedback

Symbol	Meaning
G	Correct letter in the correct position
Y	Correct letter in the wrong position
B	Letter not contained in the target word

2.2 Example Dictionary (5-letter words)

Examples used during development:

AARAU, BASEL, BRUGG, DATEI, MODUL, LOGIK


⸻

3. Game Logic (Conceptual Overview)

The Wordle game logic follows these steps:
	1.	Define a dictionary containing valid 5-letter German words
	2.	Read user input
	3.	Validate input:
	•	exactly 5 characters
	•	word must exist in the dictionary (5_letter_words.txt)
	4.	Compare the input word with the target word
	5.	Generate feedback per character (G, Y, B)
	6.	Output feedback
	7.	Increase the attempt counter
	8.	End the game on success or after 6 attempts

Maximum number of attempts: 6

⸻

4. Diagrams & Screenshots

4.1 Game Logic Draft (Console Version)

The following diagram illustrates the control flow of the console-based
Wordle implementation, including input validation, feedback generation,
and termination conditions.

<img src="myImages/logic_draft.jpg" alt="Game Logic Draft" width="60%" />


4.2 Web Application Architecture (Planned)

This diagram shows the planned interaction between the web frontend
(HTML, CSS, JavaScript) and the Java backend implemented using Javalin.

<img src="myImages/web_app_logic.jpg" alt="Web App Logic Draft" width="60%" />



⸻

5. Core Variables

The following variables are used in the game logic:
	•	String[] WOERTERBUCH – list of valid words
	•	String erratenesWort – user input word
	•	int versuche – attempt counter
	•	String feedback – feedback string (G, Y, B)

⸻

6. Random Word Selection

A random target word is selected from the dictionary:

String zufallsWort =
    WOERTERBUCH[(int)(Math.random() * WOERTERBUCH.length)];

Reference:
https://stackoverflow.com/a/7923141

⸻

7. Console vs Web Version

7.1 Console Version
	•	Uses Scanner for user input
	•	Outputs feedback as characters (G, Y, B)
	•	Focuses exclusively on correct game logic

7.2 Web Version (Planned / In Progress)
	•	Java backend implemented with Javalin
	•	Frontend using HTML, CSS, JavaScript
	•	Visual feedback (green/yellow/grey) handled via CSS
	•	Game logic reused from the console version

⸻

8. Project Structure (Web Version)

```tree
src/
 ├─ main/
 │  ├─ java/
 │  │  └─ app/
 │  │     └─ Main.java        (Game logic)
 │  │     └─ WebApp.java      (Javalin server setup)
 │  └─ resources/
 │     ├─ public/             (HTML, CSS, JS)
 │     └─ data/
 │        └─ 5_letter_words.txt
pom.xml
```

⸻

9. Word List Source

The list of valid German words is based on:

enz/german-wordlist
License: CC0-1.0

Source:
	•	https://github.com/enz/german-wordlist
	•	File: words (UTF-8, one word per line)

Usage in this project:
	•	Filter words with exactly 5 letters
	•	Convert all words to uppercase
	•	Store the result in 5_letter_words.txt

⸻

10. Learning Resources & References
	•	Build a Wordle Clone in Java
https://medium.com/strategio/build-a-wordle-clone-in-java-c7b7b924fb8d
	•	Leverage Java 17 New Features to Create Your Wordle Checker – JEP Café #10
https://inside.java/2022/02/22/jepcafe10/

Note:
Although the video uses Java 17 features, it was mainly used for conceptual understanding.

⸻

11. Project Goal

The goal of this project is to demonstrate:
	•	understanding of basic Java programming
	•	incremental software development
	•	clean and maintainable code
	•	transition from a console application to a simple web application

The project scope is intentionally limited to ensure reliability and
clarity within a pass/fail evaluation context.