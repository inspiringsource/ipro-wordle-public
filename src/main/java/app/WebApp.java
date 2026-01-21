package app;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.util.Map;

public class WebApp {
    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7070);

        app.get("/ping", ctx -> ctx.result("pong"));

        app.post("/postTest", ctx -> {
            String postTest = ctx.formParam("postTest");
            if (postTest == null) {
                postTest = "";
            }

            postTest = postTest.trim().toUpperCase();

            // Minimal validation: simple echo back in JSON
            ctx.json(Map.of("word", postTest));
        });
    }
}
