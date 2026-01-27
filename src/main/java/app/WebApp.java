package app;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WebApp {
    private final List<String> woerterbuch;
    private String zielwort;

    public WebApp() {
        this.woerterbuch = Dictionary.load5LetterWords();
        Random random = new Random();
        // Here we pick a random word from the dictionary `5_letter_words.txt`
        // this.zielwort = woerterbuch.get(random.nextInt(woerterbuch.size()));
        this.zielwort = woerterbuch.get(1); // for testing Basel

        var app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7070);

        app.get("/ping", ctx -> ctx.result("pong"));

        app.post("/guess", ctx -> {
            String erratenesWort = ctx.formParam("guess");
            if (erratenesWort == null)
                erratenesWort = "";
            erratenesWort = erratenesWort.trim().toUpperCase();

            if (!Dictionary.contains(woerterbuch, erratenesWort)) {
                ctx.status(400).json(Map.of(
                        "error", "Kein gueltiges deutsches Wort oder Wort zu kurz."));
                return;
            }

            String feedback = Main.getFeedback(erratenesWort, zielwort);

            ctx.json(Map.of(
                    "word", erratenesWort,
                    "feedback", feedback));
        });

        app.post("/postTest", ctx -> {
            String erratenesWort = ctx.formParam("postTest");
            if (erratenesWort == null) {
                erratenesWort = "";
            }

            erratenesWort = erratenesWort.trim().toUpperCase();

            if (erratenesWort.length() == 5) {
                String feedback = Main.getFeedback(erratenesWort, zielwort);
                ctx.json(Map.of("word", erratenesWort, "feedback", feedback));
            } else {
                ctx.json(Map.of("word", erratenesWort));
            }
        });
    }

    public static void main(String[] args) {
        new WebApp();
    }
}
