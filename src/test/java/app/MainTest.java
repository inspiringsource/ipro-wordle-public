package app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void getFeedback() {
        String erratenesWort = "BRUGG";
        String zielwort = "BASEL";
        String erwartetesResultat = "GBBBB";
        assertEquals(erwartetesResultat, Main.getFeedback(erratenesWort, zielwort));
    }
}