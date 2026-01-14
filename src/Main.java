import java.util.*;
import java.util.Scanner;
/*
    Wenn richtiger Buchstabe und richtige Stelle    G (Grün)
    Wenn richtiger Buchstabe und  falsch Stelle     Y (Gelb)
    Wenn Buchstabe nicht enthalten                  B (Grau)

    Ideen für 5 Buchstaben Wörter: AARAU, BASEL, BRUGG, DATEI, MODUL, LOGIK
 */


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] WOERTERBUCH = {"AARAU", "BASEL", "BRUGG", "DATEI", "MODUL", "LOGIK"};
        
        

        // System.out.println(WOERTERBUCH[2]); // output BRUGG
        System.out.print("erratenes Wort: ");
        String erratenesWort = scanner.nextLine();
        erratenesWort = erratenesWort.toUpperCase();
        System.out.println("input/output: " + erratenesWort);



    }
}