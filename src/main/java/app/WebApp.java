package app;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WebApp {
    private final List<String> woerterbuch;
    private final Random random;
    private String zielwort;

    public WebApp() {
        this.woerterbuch = Dictionary.load5LetterWords();
        this.random = new Random();
        // Here we pick a random word from the dictionary `5_letter_words.txt`
        this.zielwort = woerterbuch.get(random.nextInt(woerterbuch.size()));
        //this.zielwort = woerterbuch.get(1); // for testing Basel

        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "7070"));

        var app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
            // Render (and most cloud platforms) require binding to 0.0.0.0 and the provided PORT
            config.jetty.defaultHost = "0.0.0.0";
            config.jetty.defaultPort = port;
        }).start();

        app.get("/ping", ctx -> ctx.result("pong"));

        app.post("/new-game", ctx -> {
            String oldZielwort = this.zielwort;
            int maxTries = 10;
            int tries = 0;
            // Guarantee different word (reroll until different, max 10 tries)
            do {
                this.zielwort = woerterbuch.get(random.nextInt(woerterbuch.size()));
                tries++;
            } while (this.zielwort.equals(oldZielwort) && tries < maxTries);
            ctx.json(Map.of("status", "ok"));
        });

        app.post("/guess", ctx -> {
            String erratenesWort = ctx.formParam("guess");
            if (erratenesWort == null)
                erratenesWort = "";
            erratenesWort = erratenesWort.trim().toUpperCase();

            if (!Dictionary.contains(woerterbuch, erratenesWort)) {
                ctx.status(400).json(Map.of(
                        "error", "Kein gueltiges deutsches Wort."));
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

        // Endpoint to reveal the current solution word
        app.get("/solution", ctx -> {
            ctx.json(Map.of("zielwort", zielwort));
        });
    }

    public static void main(String[] args) {
        new WebApp();
    }
}
