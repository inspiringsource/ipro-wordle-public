package app;

import io.javalin.Javalin;

public class WebApp {

        public static void main(String[] args) {
                Javalin app = Javalin.create(config -> {
                        config.staticFiles.add("/public");
                }).start(7070);

                app.get("/", ctx -> {
                        var is = WebApp.class.getResourceAsStream("/public/index.html");
                        if (is == null) {
                                ctx.status(404).result("Template not found");
                                return;
                        }
                        String html = new String(is.readAllBytes());
                        html = html.replace("{{MESSAGE}}", "Hello World from WebApp.java");
                        ctx.contentType("text/html");
                        ctx.result(html);
                });
        }
}
