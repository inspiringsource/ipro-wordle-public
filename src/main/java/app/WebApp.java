package app;

import io.javalin.Javalin;

public class WebApp {
        public static void main(String[] args) {
                var app = Javalin.create(config -> {
                        config.staticFiles.add("/public");
                }).start(7070);

                // health check
                app.get("/ping", ctx -> ctx.result("pong"));

                // Scooter stage: accept a postTest
                app.post("/postTest", ctx -> {
                        String postTest = ctx.formParam("postTest");
                        if (postTest == null)
                                postTest = "";

                        postTest = postTest.trim().toUpperCase();

                        ctx.result("You entered: " + postTest);
                });
        }
}
