package app;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.Map;

public class WebApp {
    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7070);

        String zielwort = "BASEL";

        app.get("/ping", ctx -> ctx.result("pong"));

        app.post("/guess", ctx -> {
            String erratenesWort = ctx.formParam("guess");
            if (erratenesWort == null)
                erratenesWort = "";
            erratenesWort = erratenesWort.trim().toUpperCase();

            if (erratenesWort.length() != 5) {
                ctx.status(400).json(Map.of(
                        "word", erratenesWort,
                        "feedback", "BBBBB",
                        "error", "Guess must be exactly 5 letters"));
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
}
