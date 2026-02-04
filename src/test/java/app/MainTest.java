package app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testOneCharCorrect() {
        String erratenesWort = "BRUGG";
        String zielwort = "BASEL";
        String erwartetesResultat = "GBBBB";
        assertEquals(erwartetesResultat, Main.getFeedback(erratenesWort, zielwort));
    }
    @Test
    void testTwoLastCharCorrect() {
        String erratenesWort = "LOGIK";
        String zielwort = "MUSIK";
        String erwartetesResultat = "BBBGG";
        assertEquals(erwartetesResultat, Main.getFeedback(erratenesWort, zielwort));
    }
    @Test
    void testCharCorrect() {
        String erratenesWort = "Stark";
        String zielwort = "Stark";
        String erwartetesResultat = "GGGGG";
        assertEquals(erwartetesResultat, Main.getFeedback(erratenesWort, zielwort));
    }
    @Test
    void testCharIncorrect() {
        String erratenesWort = "STARK";
        String zielwort = "BLUME";
        String erwartetesResultat = "BBBBB";
        assertEquals(erwartetesResultat, Main.getFeedback(erratenesWort, zielwort));
    }

}