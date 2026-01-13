# Projektmethodik und Vorgehen – ipro Wordle (Java)

## Projektkontext
Dieses Projekt entsteht im Rahmen des Moduls **Individuelles Softwareprojekt (ipro)** an der FHNW.
Ziel ist die schrittweise Entwicklung eines Wordle-Spiels in Java, beginnend mit einer Konsolenanwendung
und fokussiert auf saubere Logik und inkrementelle Umsetzung.

**Projektstart:** 12.01.2026  
**Geplantes Projektende:** ca. 06.02.2026  

---

## Angewandte ICT Research Methods (Auswahl)

### Library Phase
**Problem Analysis**
- Analyse der Wordle-Spielregeln (5 Buchstaben, max. 6 Versuche, Feedback G/Y/B)
- Abgleich mit Aufgabenbeschreibung des ipro-Moduls

**Best Good and Bad Practices**
- Orientierung am Original Wordle (NYT)
- Analyse typischer Wordle-Implementierungen (Java, Console)

**Literature / Online Study**
- Fachartikel und Tutorials zur Wordle-Logik in Java
- Nutzung externer Referenzen (Regex, Input-Validierung, Zeichenvergleich)

---

### Workshop Phase
**Decomposition**
Das Problem wird in klar abgegrenzte Teilbereiche zerlegt:
- Wörterbuch / gültige Wörter
- Benutzereingabe
- Validierung (Länge, Zeichen, Wörterbuch)
- Vergleichslogik (G / Y / B)
- Versuchszähler
- Spielabbruch / Gewinn
- Statistik (persistent)

**Prototyping**
- Start mit einer minimalen Konsolenanwendung (`Main.java`)
- Schrittweiser Ausbau der Funktionalität
- Fokus auf funktionierende Logik vor UI-Erweiterungen

**IT Architecture Sketching (leichtgewichtig)**
- Trennung zwischen:
  - Hilfslogik (z.B. Wörterbuch-Aufbereitung)
  - Spiellogik (Vergleich, Regeln)
  - Ein- und Ausgabe (Konsole)

---

### Lab Phase
**System Test (manuell)**
- Durchspielen kompletter Spielrunden
- Test von Randfällen (ungueltige Eingaben, max. Versuche)

**Component Test (informell)**
- Test einzelner Logikbausteine (z.B. Wortvergleich, Wörterbuchprüfung)

**Non-functional Test (Usability, basic)**
- Verständliche Ausgaben
- Klarer Spielfluss in der Konsole

---

### Showroom Phase
**Peer Review**
- Code-Reviews im Austausch mit Mitstudierenden
- Feedback zur Verständlichkeit und Struktur

**Product Review**
- Vergleich des eigenen Spiels mit bestehenden Wordle-Varianten

---

## Geplantes technisches Vorgehen

### Wörterbuch / Datenbasis
- Verwendung einer externen deutschen Wortliste  
  Quelle: https://github.com/enz/german-wordlist
- Entwicklung `Helper.java`, das:
  - Wörter auf genau 5 Buchstaben filtert
  - Alle Wörter in Grossbuchstaben umwandelt
  - Eine bereinigte Wortliste für das Wordle-Spiel erzeugt

Diese Wortliste dient später als:
- gültige Eingabemenge
- Basis für zufällige Lösungswörter

---

### Implementierungsstrategie
1. Definition zentraler Variablen (Wörterbuch, Zielwort, Versuche)
2. Validierung der Benutzereingabe
3. Wörterbuchprüfung
4. Vergleichslogik (G / Y / B)
5. Spielschleife (max. 6 Versuche)
6. Gewinn- und Abbruchbedingungen
7. Erweiterung um Statistik (persistent)
8. Refactoring und Tests

Die Implementierung erfolgt **inkrementell**, jeweils mit Tests nach jedem Schritt.

---

## Zeitplanung (grobe Schaetzung)
- **Woche 1:** Kernlogik, Wörterbuch, funktionierendes Konsolenspiel
- **Woche 2:** Tests, Refactoring, Statistik, optionale Erweiterungen

---

## Aktueller Stand (Stand: 12.01.2026)
- GitHub-Repository erstellt
- README mit Spielregeln, Logik und Referenzen angelegt
- Erste `Main.java` mit Grundstruktur und Konsolen-Ein-/Ausgabe implementiert
- Projektmethodik definiert

---

## Ausblick
Nach Abschluss der Konsolenversion ist eine Erweiterung zu einer grafischen oder webbasierten Version denkbar.
Diese ist jedoch **nicht Teil der initialen Kernanforderungen** und wird erst nach stabiler Logik evaluiert.