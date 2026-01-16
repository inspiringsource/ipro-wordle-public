package main.java.app;

import io.javalin.Javalin;

public class WebApp {
        var app = Javalin.create(/*config*/)
        .get("/", ctx -> ctx.result("Hello World"))
        .start(7070);
}
